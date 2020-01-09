package ru.zizitop.example.queue;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Queue;

public class QueueApplication {

    private final Queue<SimpleMessage> messages;

    public QueueApplication(){
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();

        messages = instance.getQueue("messages");
    }
}
