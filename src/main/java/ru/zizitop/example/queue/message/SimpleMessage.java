package ru.zizitop.example.queue.message;

import java.io.Serializable;
import java.util.Objects;

public abstract class SimpleMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    Long id;
    String payload;

    public SimpleMessage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleMessage that = (SimpleMessage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
