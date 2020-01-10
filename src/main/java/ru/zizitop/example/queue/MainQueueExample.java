package ru.zizitop.example.queue;

import ru.zizitop.example.queue.message.RequestMessage;
import ru.zizitop.example.queue.message.ResponseMessage;
import ru.zizitop.example.queue.message.SimpleMessage;
import ru.zizitop.example.queue.producer.AgencyProducer;
import ru.zizitop.example.queue.producer.BankProducer;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class MainQueueExample {

    public static void main(String[] args) throws InterruptedException {


        final Hashtable<Long, SimpleMessage> storage = new Hashtable<>();

        Queue<RequestMessage> bankInputQueue = new LinkedList<>();
        Queue<ResponseMessage> bankOutputQueue = new LinkedList<>();

        Queue<ResponseMessage> agencyInputQueue = new LinkedList<>();
        Queue<RequestMessage> agencyOutputQueue = new LinkedList<>();

        QueueApplication queueApplication = new QueueApplication(storage);
        queueApplication.setBankInputQueue(bankInputQueue);
        queueApplication.setBankOutputQueue(bankOutputQueue);
        queueApplication.setAgencyInputQueue(agencyInputQueue);
        queueApplication.setAgencyOutputQueue(agencyOutputQueue);

        BankProducer bankProducer = new BankProducer();
        AgencyProducer agencyProducer = new AgencyProducer();


        int size = 100;
        for(int i =0; i < size; i ++){
            bankInputQueue.add(bankProducer.createRequest());
        }


        Runnable runnable = () -> {
            while (true) {
                synchronized (agencyOutputQueue) {
                    if (agencyOutputQueue.size() > 0) {
                        RequestMessage
                                request = agencyOutputQueue.poll();

                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        agencyInputQueue.add(agencyProducer.createResponse(request));
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        queueApplication.start();



        Long start = System.currentTimeMillis();
        while(bankOutputQueue.size() != size){
            queueApplication.printState();
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("Time:" +  (System.currentTimeMillis() - start)/1000);


    }
}
