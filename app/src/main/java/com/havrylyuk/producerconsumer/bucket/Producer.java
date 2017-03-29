package com.havrylyuk.producerconsumer.bucket;

import com.havrylyuk.producerconsumer.event.EventType;
import com.havrylyuk.producerconsumer.event.ProducerConsumerEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Igor Havrylyuk on 29.03.2017.
 */

public class Producer extends Thread {

   public final static int PRODUCER_DELAY = 2000; //1.5c
   private final static int BUCKET_SIZE = 30;//50;

   private final Bucket bucket;
   private final int imageId;

    public Producer(Bucket b, int resId) {
        bucket = b;
        imageId = resId;
    }

    @Override
    public void run() {
        String message;
        int fruitCount = 0;
        while (fruitCount < BUCKET_SIZE) {
            synchronized (bucket) {
                message = "Produced:Lollipop " + fruitCount;
                System.out.println(message);
                bucket.addLollipop("Lollipop " + fruitCount++);
                EventBus.getDefault().post(new ProducerConsumerEvent(EventType.PRODUCE, imageId, message));
                bucket.notifyAll();

            }
            try {
                Thread.sleep(PRODUCER_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (bucket) {
            bucket.setProducerFinished(true);
            bucket.notifyAll();
        }
        message = "Producer finished.";
        EventBus.getDefault().post(new ProducerConsumerEvent(EventType.FINISH, imageId, message));
        System.out.println(message );

    }

}
