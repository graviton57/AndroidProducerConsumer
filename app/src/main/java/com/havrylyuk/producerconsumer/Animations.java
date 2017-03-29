package com.havrylyuk.producerconsumer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.widget.ImageView;

import com.havrylyuk.producerconsumer.bucket.Producer;

import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

/**
 * Created by Igor Havrylyuk on 29.03.2017.
 */

public class Animations {

    private static final int CONSUMER_DURATION = Producer.PRODUCER_DELAY / 4;//do not change

    private static AnimatorListenerAdapter consumerAnimationListener(final ImageView producer,
                                                                     final ImageView consumer) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                consumer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                producer.setImageResource(R.drawable.empty_basket);
                consumer.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        };
    }

    public static void animateConsumer(final ImageView producer, final ImageView consumer,
                                       int startX, int startY, int endX, int endY){
        if (consumer != null) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(TRANSLATION_X, endX - startX );
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(TRANSLATION_Y, endY - startY  );
            ObjectAnimator animatorUp = ObjectAnimator.ofPropertyValuesHolder(consumer, pvhX, pvhY);
            animatorUp.setRepeatCount(1);
            animatorUp.setRepeatMode(ObjectAnimator.REVERSE);
            animatorUp.setDuration(CONSUMER_DURATION);
            animatorUp.addListener(consumerAnimationListener(producer, consumer));
            animatorUp.start();
        }
    }
}
