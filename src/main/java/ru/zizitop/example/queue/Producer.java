package ru.zizitop.example.queue;

import java.util.Random;

public class Producer {

    private static Random random = new Random(System.currentTimeMillis());

    public SimpleMessage createResponse(RequestMessage requestMessage){
        ResponseMessage response = new ResponseMessage(random.nextLong());
        response.setRequestId(requestMessage.getId());
        return requestMessage;
    }
}
