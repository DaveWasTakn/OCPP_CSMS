package com.dave.OcppProtocol;

import com.dave.Exception.OcppProtocolException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public record CallResultMsg(
        int messageTypeId,
        String uniqueId,
        JsonNode payload
) implements OcppMessage {
    // CallResult: [<MessageTypeId>, "<UniqueId>", {<Payload>}]

    public  static CallResultMsg fromMessage(String msg) throws OcppProtocolException {
        List<String> items = OcppMessage.getMsgItems(msg);
        if (items.size() != 3) {
            throw new OcppProtocolException("CallResultMsg is malformed");
        }
        return new CallResultMsg(
                Integer.parseInt(items.get(0)),
                items.get(1),
                new ObjectMapper().readTree(items.get(2))
        );
    }
}
