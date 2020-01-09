package ru.zizitop.example.queue;

public class RequestMessage extends SimpleMessage {

    public String service;

    public RequestMessage(Long id) {
        super(id);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
