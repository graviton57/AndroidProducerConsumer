package com.havrylyuk.producerconsumer.bucket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Havrylyuk on 29.03.2017.
 */

public class Bucket {

    private List<String> lollipopBucket = new ArrayList<>();
    private boolean producerFinished = false;


    public boolean isProducerFinished() {
        return producerFinished;
    }

    public void setProducerFinished(boolean producerFinished) {
        this.producerFinished = producerFinished;
    }

    public int getBucketSize() {
        return lollipopBucket.size();
    }

    public void addLollipop(String fruit) {
        lollipopBucket.add(fruit);
    }

    public String getLollipop() {
        if (lollipopBucket.size() == 0)
            return null;
        return lollipopBucket.remove(lollipopBucket.size() - 1);
    }
}
