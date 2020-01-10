package ru.zizitop.example.queue.producer;

import ru.zizitop.example.queue.message.RequestMessage;
import ru.zizitop.example.queue.message.ResponseMessage;
import ru.zizitop.example.queue.message.SimpleMessage;

public class BankProducer extends MessageProducer {

    final String[] services = {"FirstBank", "SecondBank", "ThirdBank"};

    @Override
    public RequestMessage createRequest() {
        RequestMessage requestMessage = new RequestMessage(createId());
        String service = services[random.nextInt(services.length)];
        requestMessage.setPayload(
                "<bank>\n" +
                " <request>\n" +
                "  <service>" +service+ "  </service>\n" +
                " </request>\n" +
                "</bank>");
        requestMessage.setService(service);
        return requestMessage;
    }

    @Override
    public ResponseMessage createResponse(RequestMessage simpleMessage) {
        ResponseMessage responseMessage = new ResponseMessage(createId());
        responseMessage.setRequestId(simpleMessage.getId());
        responseMessage.setPayload(
                "<bank>\n" +
                "  <response>\n" +
                        simpleMessage.getPayload() +
                "  </response>\n" +
                "\n</bank>");
        return responseMessage;
    }
}
