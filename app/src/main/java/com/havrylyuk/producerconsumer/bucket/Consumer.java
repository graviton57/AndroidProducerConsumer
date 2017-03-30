package com.havrylyuk.producerconsumer.bucket;

import com.havrylyuk.producerconsumer.event.EventType;
import com.havrylyuk.producerconsumer.event.ProducerConsumerEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Igor Havrylyuk on 29.03.2017.
 */

public class Consumer extends Thread {

    private final Bucket bucket;
    private String consumerName;
    private int lollipopCount;
    private int id;

    public Consumer(Bucket b, String consName, int resId) {
        bucket = b;
        id = resId;
        consumerName =consName;
    }

    @Override
    public void run() {
        String message;
        while (!bucket.isProducerFinished()) {
            synchronized (bucket) {
                String lollipop = bucket.getLollipop();
                if (lollipop == null) {
                    try {
                        bucket.wait();
                        message = consumerName + "  waiting...";
                        EventBus.getDefault().post(new ProducerConsumerEvent(EventType.WAIT, id, message ));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    lollipopCount++;
                    message = "Consumed lollipop:" + lollipop +" by "+consumerName;
                    EventBus.getDefault().post(new ProducerConsumerEvent(EventType.CONSUME, id, message));
                }
            }
        }
        synchronized (bucket) {
            bucket.notifyAll();
            message = consumerName +" finished. Lollipops:"+ lollipopCount;
            EventBus.getDefault().post(new ProducerConsumerEvent(EventType.FINISH, id, message));
        }
    }
}
