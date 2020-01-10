package ru.zizitop.example.queue.producer;

import ru.zizitop.example.queue.message.RequestMessage;
import ru.zizitop.example.queue.message.ResponseMessage;
import ru.zizitop.example.queue.message.SimpleMessage;

public class AgencyProducer extends MessageProducer {

    @Override
    public RequestMessage createRequest() {
        return new RequestMessage(createId());
    }

    @Override
    public ResponseMessage createResponse(RequestMessage simpleMessage) {
        ResponseMessage responseMessage = new ResponseMessage(createId());
        responseMessage.setRequestId(simpleMessage.getId());
        responseMessage.setPayload(
                        "<agency>\n" +
                        "  <response>\n" +
                            simpleMessage.getPayload() +
                        "  </response>\n" +
                        "\n</agency>");
        return responseMessage;
    }
}
