package com.dave.OcppProtocol;

import com.dave.Exception.OcppProtocolException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public record CallMsg(
        int messageTypeId,
        String uniqueId,
        String action,
        JsonNode payload
) implements OcppMessage {
    // Call: [<MessageTypeId>, "<UniqueId>", "<Action>", {<Payload>}]

    public static CallMsg fromMessage(String msg) throws OcppProtocolException {
        List<String> items = OcppMessage.getMsgItems(msg);
        if (items.size() != 4) {
            throw new OcppProtocolException("CallMsg is malformed");
        }
        return new CallMsg(
                Integer.parseInt(items.get(0)),
                items.get(1),
                items.get(2),
                new ObjectMapper().readTree(items.get(3))
        );
    }
}
