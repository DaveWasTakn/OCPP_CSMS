package com.dave.Ocpp;

import com.dave.Exception.OcppProtocolException;
import com.dave.StreamProcessor.StreamProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class OcppProtocol {

    protected final StreamProcessor streamProcessor;

    public OcppProtocol(StreamProcessor streamProcessor) {
        this.streamProcessor = streamProcessor;
    }

    // TODO need some sort of STATE

    public void onMsg(String msg) throws OcppProtocolException {
        if (msg.isBlank() || msg.length() <= 2) {
            throw new OcppProtocolException("Message received is malformed");
        }
        int messageTypeId = Integer.parseInt(msg.substring(1, msg.indexOf(',')).trim()); // TODO exception on eg disconnect, need to handle that in websocketStreamProcessor with opcodes (probably)

        switch (messageTypeId) {
            case 2:
                onCall(CallMsg.fromMessage(msg));
                break;
            case 3:
                onCallResult(CallResultMsg.fromMessage(msg));
                break;
            case 4:
                onCallError(CallErrorMsg.fromMessage(msg));
                break;
        }
    }

    private void onCall(CallMsg message) throws OcppProtocolException {
        Method m = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("onCall_" + message.action()))
                .findAny()
                .orElseThrow(() -> new OcppProtocolException("No method declared to handle action: " + message.action()));
        m.setAccessible(true);
        try {
            m.invoke(this, message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new OcppProtocolException("Could not invoke method: " + m.getName());
        }
    }


    private void onCallResult(CallResultMsg message) {
        // TODO
    }

    private void onCallError(CallErrorMsg message) {
        // TODO
    }

}
