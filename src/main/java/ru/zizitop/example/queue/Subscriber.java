package ru.zizitop.example.queue;

import java.util.Random;

public class Subscriber {

    private static final String[] services = {"First", "Second", "Third"};

    private static Random random = new Random(System.currentTimeMillis());

    public SimpleMessage createRequset(){
        RequestMessage requestMessage = new RequestMessage(random.nextLong());
        String service = services[random.nextInt(services.length)];
        requestMessage.setPayload("<service>" +service+ "</service>");
        requestMessage.setService(service);
        return requestMessage;

    }
}
