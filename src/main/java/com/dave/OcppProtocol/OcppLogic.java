package com.dave.OcppProtocol;

import com.dave.Exception.OcppProtocolException;

public interface OcppLogic {

    default void onMsg(String msg) throws OcppProtocolException {
        if (msg.isBlank()) {
            throw new OcppProtocolException("Message received is blank");
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

    void onCall(CallMsg message);

    void onCallResult(CallResultMsg message);

    void onCallError(CallErrorMsg message);

}
