package ru.zizitop.example.queue.producer;

import ru.zizitop.example.queue.message.RequestMessage;
import ru.zizitop.example.queue.message.ResponseMessage;
import ru.zizitop.example.queue.message.SimpleMessage;

import java.util.Random;

public abstract class MessageProducer {

    protected Random random;

    public MessageProducer(){
        this.random = new Random(System.currentTimeMillis());
    }

    protected Long createId(){
        return random.nextLong();
    }

    public abstract RequestMessage createRequest();

    public abstract ResponseMessage createResponse(RequestMessage simpleMessage);
}
