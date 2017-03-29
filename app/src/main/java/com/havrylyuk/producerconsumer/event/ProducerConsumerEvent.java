package com.havrylyuk.producerconsumer.event;


/**
 * Created by Igor Havrylyuk on 29.03.2017.
 */

public class ProducerConsumerEvent {

    private String message;
    private EventType type;
    private int id;

    public ProducerConsumerEvent(EventType type, int id, String message ) {
        this.message = message;
        this.type = type;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public EventType getType() {
        return type;
    }
}
