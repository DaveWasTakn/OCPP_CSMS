package com.dave.OcppProtocol;

import com.dave.Exception.OcppProtocolException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public record CallErrorMsg(
        int messageTypeId,
        String uniqueId,
        String errorCode,
        String errorDescription,
        JsonNode errorDetails
) implements OcppMessage {
    // CallError: [<MessageTypeId>, "<UniqueId>", "<errorCode>", "<errorDescription>", {<errorDetails>}]

    public static CallErrorMsg fromMessage(String msg) throws OcppProtocolException {
        List<String> items = OcppMessage.getMsgItems(msg);
        if (items.size() != 5) {
            throw new OcppProtocolException("CallErrorMsg is malformed");
        }
        return new CallErrorMsg(
                Integer.parseInt(items.get(0)),
                items.get(1),
                items.get(2),
                items.get(3),
                new ObjectMapper().readTree(items.get(4))
        );
    }
}
