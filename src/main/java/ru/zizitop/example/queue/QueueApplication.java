package ru.zizitop.example.queue;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import ru.zizitop.example.queue.message.RequestMessage;
import ru.zizitop.example.queue.message.ResponseMessage;
import ru.zizitop.example.queue.message.SimpleMessage;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class QueueApplication {


    private  Queue<RequestMessage> bankInputQueue;
    private  Queue<ResponseMessage> bankOutputQueue;

    private  Queue<ResponseMessage> agencyInputQueue;
    private  Queue<RequestMessage> agencyOutputQueue;


    private final Queue<SimpleMessage> innerBankQueue;
    private final Queue<ResponseMessage> innerAgencyQueue;

    private final Hashtable<Long, SimpleMessage> messageStorage;

    public Queue<RequestMessage> getBankInputQueue() {
        return bankInputQueue;
    }

    public Queue<ResponseMessage> getBankOutputQueue() {
        return bankOutputQueue;
    }

    public Queue<ResponseMessage> getAgencyInputQueue() {
        return agencyInputQueue;
    }

    public Queue<RequestMessage> getAgencyOutputQueue() {
        return agencyOutputQueue;
    }

    public void setBankInputQueue(Queue<RequestMessage> bankInputQueue) {
        this.bankInputQueue = bankInputQueue;
    }

    public void setBankOutputQueue(Queue<ResponseMessage> bankOutputQueue) {
        this.bankOutputQueue = bankOutputQueue;
    }

    public void setAgencyInputQueue(Queue<ResponseMessage> agencyInputQueue) {
        this.agencyInputQueue = agencyInputQueue;
    }

    public void setAgencyOutputQueue(Queue<RequestMessage> agencyOutputQueue) {
        this.agencyOutputQueue = agencyOutputQueue;
    }

    public QueueApplication(Hashtable<Long, SimpleMessage> messageStorage) throws InterruptedException {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        innerBankQueue = instance.getQueue("innerBankQueue");
        innerAgencyQueue = instance.getQueue("innerAgencyQueue");
        this.messageStorage = messageStorage;
    }

    public void start()  {
        onBankMessage();
        handleBankMessage();
        onAgencyMessage();
        handleAgencyMessage();
    }

    public void printState() {

        StringBuilder sb = new StringBuilder();
        sb.append("-------------  " + new Date().toString() + "  -------------\n");
        sb.append("bankInputQueue: " + bankInputQueue.size() + "\n");
        sb.append("bankOutputQueue: " + bankOutputQueue.size() + "\n");
        sb.append("\n");
        sb.append("agencyInputQueue: " + agencyInputQueue.size() + "\n");
        sb.append("agencyOutputQueue: " + agencyOutputQueue.size() + "\n");
        sb.append("\n");
        sb.append("innerBankQueue: " + innerBankQueue.size() + "\n");
        sb.append("innerAgencyQueue: " + innerAgencyQueue.size() + "\n");

        System.out.println(sb.toString());
    }

    private void onBankMessage() {

        Runnable runnable = () -> {
            while (true) {
                synchronized (bankInputQueue) {
                    if (bankInputQueue.size() > 0) {
                        RequestMessage request = bankInputQueue.poll();
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        innerBankQueue.add(request);
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

    private void handleBankMessage() {

        Runnable runnable = () -> {
            while (true) {
                synchronized (innerBankQueue) {
                    if (innerBankQueue.size() > 0) {
                        RequestMessage request = (RequestMessage) innerBankQueue.poll();

                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        messageStorage.put(request.getId(), request);

                        RequestMessage systemRequest = new RequestMessage(request.getId());
                        systemRequest.setService(request.getService() + ": system");
                        systemRequest.setPayload("<system>\n " + request.getPayload() + " \n</system>");
                        synchronized (agencyOutputQueue) {
                            agencyOutputQueue.add(systemRequest);
                        }
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);

        thread.start();
    }


    private void onAgencyMessage() {
        Runnable runnable = () -> {
            while (true) {
                synchronized (agencyInputQueue) {
                    if (agencyInputQueue.size() > 0) {
                        ResponseMessage responseMessage = agencyInputQueue.poll();
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        innerAgencyQueue.add(responseMessage);
                    }
                }
            }

        };

        Thread thread = new Thread(runnable);

        thread.start();
    }


    private void handleAgencyMessage() {

        Runnable runnable = () -> {
            while (true) {
                synchronized (innerAgencyQueue) {
                    if (innerAgencyQueue.size() > 0) {
                        ResponseMessage response = innerAgencyQueue.poll();
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        messageStorage.put(response.getId(), response);

                        ResponseMessage systemResponse = new ResponseMessage(response.getId());
                        systemResponse.setPayload("<system>\n " + response.getPayload() + " \n</system>");
                        systemResponse.setRequestId(response.getRequestId());

                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        RequestMessage requestMessage = (RequestMessage) messageStorage.get(response.getRequestId());
                        requestMessage.setAnswered(true);

                        bankOutputQueue.add(systemResponse);
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);

        thread.start();
    }


}
