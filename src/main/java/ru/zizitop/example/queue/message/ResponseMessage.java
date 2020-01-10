package ru.zizitop.example.queue.message;

public class ResponseMessage extends SimpleMessage {

    Long requestId;

    public ResponseMessage(Long id) {
        super(id);
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "requestId=" + requestId +
                ", id=" + id +
                ", payload='" + payload + '\'' +
                '}';
    }
}
