package ru.zizitop.example.queue.message;

public class RequestMessage extends SimpleMessage {

    private String service;

    private boolean answered;

    public RequestMessage(Long id) {
        super(id);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "service='" + service + '\'' +
                ", answered=" + answered +
                ", id=" + id +
                ", payload='" + payload + '\'' +
                '}';
    }
}
