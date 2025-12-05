package com.dave.OcppProtocol;

import com.dave.Exception.OcppProtocolException;

import java.util.Arrays;
import java.util.List;

public sealed interface OcppMessage permits CallMsg, CallResultMsg, CallErrorMsg {

    static OcppMessage fromMessage(String msg) throws OcppProtocolException {
        throw new UnsupportedOperationException("Only callable on actual implementations");
    }

    static List<String> getMsgItems(String msg) throws OcppProtocolException {
        if (msg.length() < 3) {
            throw new OcppProtocolException("Ocpp request is too short");
        }
        List<String> split = Arrays.stream(msg.substring(1, msg.length() - 1).split(","))
                .map(String::trim)
                .map(x -> x.startsWith("\"") && x.endsWith("\"") ? x.substring(1, x.length() - 1) : x)
                .toList();
        if (split.size() < 3) {
            throw new OcppProtocolException("Message received is no valid OCPP call");
        }
        return split;
    }

}
