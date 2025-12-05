package com.dave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class OcppHandler implements Runnable {
    final Socket clientSocket;
    final String clientInetAddress;
    boolean protocolUpgraded = false;

    final InputStream clientIn;
    final OutputStream clientOut;

    final String ocppVersion = "ocpp1.6"; // TODO refactor!

    public OcppHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientInetAddress = clientSocket.getInetAddress().toString();
        try {
            this.clientIn = clientSocket.getInputStream();
            this.clientOut = clientSocket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Connected to " + clientInetAddress + "\n");
    }

    private void printIncoming(String req) {
        printReq(req, " Receiving from: " + this.clientInetAddress + ":");
    }

    private void printOutgoing(String req) {
        printReq(req, "---- Sending to: " + this.clientInetAddress + ":");
    }

    private void printReq(String req, String info) {
        System.out.println("-----------------------------------" + info);
        System.out.println(req);
        System.out.println("-------------------------------------------------------------------\n");
    }

    @Override
    public void run() {
        Scanner s = new Scanner(this.clientIn, StandardCharsets.UTF_8);
        s.useDelimiter("\\r\\n\\r\\n");

        while (clientSocket.isConnected()) {
            if (!protocolUpgraded) { // if not upgraded to websocket -> still HTTP
                try {
                    String msg = s.next();
                    printIncoming(msg);
                    this.handleReq(msg);
                } catch (NoSuchElementException e) {
                    closeClientSocket();
                    break;
                }
            } else { // websocket protocol -> parse Websocket frames
                // https://datatracker.ietf.org/doc/html/rfc6455#section-5
                try {
                    this.parseWebsocketFrames(this.clientIn);
                } catch (OCPPProtocolException e) {
                    closeClientSocket();
                    break;
                }
                // TODO handle complete message by appropriate methods
            }
        }
    }

    private void closeClientSocket() {
        this.protocolUpgraded = false;
        try {
            System.out.println("Closing connection to " + clientInetAddress + "\n");
            clientSocket.close();
        } catch (IOException ex) {
            this.protocolUpgraded = false;
            throw new RuntimeException(ex);
        }
    }

    private byte[] parseWebsocketFrames(InputStream clientIn) throws OCPPProtocolException {
        // https://datatracker.ietf.org/doc/html/rfc6455#section-5
        try {
            byte byte1 = clientIn.readNBytes(1)[0];
            boolean FIN = bitAt(byte1, 0);
            boolean RSV1 = bitAt(byte1, 1);
            boolean RSV2 = bitAt(byte1, 2);
            boolean RSV3 = bitAt(byte1, 3);
            int OPCODE = byte1 & ((1 << 5) - 1);

            byte byte2 = clientIn.readNBytes(1)[0];
            boolean MASK = bitAt(byte2, 0);

            if (!MASK) {
                throw new OCPPProtocolException("Client requests need to be masked.");
            }

            long PAYLOAD_LEN = byte2 & 0x7F;
            if (PAYLOAD_LEN == 126) {
                byte[] extendedLength = clientIn.readNBytes(2);
                PAYLOAD_LEN = ((extendedLength[0] & 0xFF) << 8) | (extendedLength[1] & 0xFF);
            } else if (PAYLOAD_LEN == 127) {
                byte[] extendedLength = clientIn.readNBytes(8);
                PAYLOAD_LEN = ByteBuffer.wrap(extendedLength).getLong(); // ByteOrder.BIG_ENDIAN by default ?!
            }

            byte[] MASKING_KEY = clientIn.readNBytes(4);

            // no extension data -> only application data
            if (PAYLOAD_LEN > Integer.MAX_VALUE) {
                // very unlikely > 2GB payload ==> just cast to int for now
                // TODO theoretically need to read in chunks
            }
            byte[] payload = clientIn.readNBytes((int) PAYLOAD_LEN);
            unmaskPayload(payload, MASKING_KEY);

            if (FIN) {
                return payload;
            } else { // instead of recursion could use while !FIN and ByteArrayOutputStream for better efficiency
                byte[] nextPayload = parseWebsocketFrames(clientIn);
                return ByteBuffer.allocate(payload.length + nextPayload.length).put(payload).put(nextPayload).array();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO handle fragmentation, FIN
        // TODO return total message!
        // TODO handle opcodes i.e., ping pong
    }

    private void unmaskPayload(byte[] payloadBytes, byte[] maskingKey) { // unmask payloadBytes inPlace
        byte[] unmasked = new byte[payloadBytes.length];
        for (int i = 0; i < payloadBytes.length; i++) {
            payloadBytes[i] = (byte) (payloadBytes[i] ^ maskingKey[i % 4]);
        }
    }

    private static boolean bitAt(byte b, int pos) { // MSB to LSB;
        if (pos < 0 || pos > 7) {
            throw new IllegalArgumentException("pos must be 0..7");
        }
        return (b & (1 << (7 - pos))) != 0;
    }

    private void handleReq(String msg) {
        if (!protocolUpgraded) {
            handleUpgradeReq(msg);
        }

    }

    private void handleUpgradeReq(String msg) {
        HTTPReq req;
        try {
            req = HTTPReq.parse(msg);
        } catch (HttpParseException e) { // TODO log that it will be disregarded
            this.protocolUpgraded = false;
            throw new RuntimeException(e);
        }
        if (
                req.headers().getOrDefault("Upgrade", "").equals("websocket")
                        && req.headers().getOrDefault("Connection", "").equals("Upgrade")
                        && req.headers().containsKey("Sec-WebSocket-Key")
                        && req.headers().containsKey("Sec-WebSocket-Protocol")
                        && req.headers().containsKey("Sec-WebSocket-Version")
        ) {
            this.confirmUpgradeReq(req.headers().get("Sec-WebSocket-Key"));
        }
    }

    private void confirmUpgradeReq(String secWebSocketKey) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Upgrade", "websocket");
        headers.put("Connection", "Upgrade");
        headers.put("Sec-WebSocket-Accept", calcSecWebSocketAccept(secWebSocketKey));
        headers.put("Sec-WebSocket-Protocol", ocppVersion);

        String upgradeConf = new HTTPReq(
                new String[]{"HTTP/1.1", "101", "Switching Protocols"},
                headers,
                ""
        ).toString();

        printOutgoing(upgradeConf);
        try {
            this.clientOut.write(upgradeConf.getBytes(StandardCharsets.UTF_8));
            this.clientOut.flush();
        } catch (IOException e) {
            System.out.println("Could not send upgrade request to " + this.clientInetAddress + "\n");
            throw new RuntimeException(e);
        }
        this.protocolUpgraded = true;
    }

    public String calcSecWebSocketAccept(String secWebSocketKey) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashed = sha1.digest((secWebSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            this.protocolUpgraded = false;
            throw new RuntimeException("Failed to compute Sec-WebSocket-Accept", e);
        }
    }

}
