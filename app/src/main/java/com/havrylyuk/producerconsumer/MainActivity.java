package com.havrylyuk.producerconsumer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.havrylyuk.producerconsumer.bucket.Bucket;
import com.havrylyuk.producerconsumer.bucket.Consumer;
import com.havrylyuk.producerconsumer.bucket.Producer;
import com.havrylyuk.producerconsumer.event.ProducerConsumerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ProducerConsumerAdapter adapter;
    private FloatingActionButton startButton;
    private ImageView consumer1Image;
    private ImageView producerImage;
    private ImageView consumer2Image;
    private ImageView consumer3Image;
    private int xBucket;
    private int yBucket;
    private RecyclerView recyclerView;
    private int xConsumer1, xConsumer2, xConsumer3;
    private int yConsumer1, yConsumer2, yConsumer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupRecyclerView();
        setupFabStart();
        initProducerUI();
        initConsumersUI();
    }

    private void initialize() {
        setStartVisible(false);
        Bucket b = new Bucket();
        Producer producer = new Producer(b, R.id.bucket_image);
        Consumer consumer1 = new Consumer(b,getString(R.string.consumer_1), R.id.consumer1_image);
        Consumer consumer2 = new Consumer(b,getString(R.string.consumer_2), R.id.consumer2_image);
        Consumer consumer3 = new Consumer(b,getString(R.string.consumer_3), R.id.consumer3_image);
        producer.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();
    }

    private void initProducerUI(){
        producerImage = (ImageView) findViewById(R.id.bucket_image);
        if (producerImage != null ) {
            producerImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int[] locationsFrom = new int[2];
                    producerImage.getLocationInWindow(locationsFrom);
                    xBucket = locationsFrom[0];
                    yBucket = locationsFrom[1];
                }
            });
        }
    }

    private void initConsumersUI(){
        consumer1Image = (ImageView) findViewById(R.id.consumer1_image);
        if (consumer1Image!=null){
            consumer1Image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int[] locationsFrom = new int[2];
                    consumer1Image.getLocationInWindow(locationsFrom);
                    xConsumer1 = locationsFrom[0];
                    yConsumer1 = locationsFrom[1];
                }
            });
        }
        consumer2Image = (ImageView) findViewById(R.id.consumer2_image);
        if (consumer2Image!=null){
            consumer2Image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int[] locationsFrom = new int[2];
                    consumer2Image.getLocationInWindow(locationsFrom);
                    xConsumer2 = locationsFrom[0];
                    yConsumer2 = locationsFrom[1];
                }
            });
        }
        consumer3Image = (ImageView) findViewById(R.id.consumer3_image);
        if (consumer3Image!=null){
            consumer3Image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int[] locationsFrom = new int[2];
                    consumer3Image.getLocationInWindow(locationsFrom);
                    xConsumer3 = locationsFrom[0];
                    yConsumer3 = locationsFrom[1];
                }
            });
        }
    }

    private void setupFabStart(){
        startButton = (FloatingActionButton) findViewById(R.id.fab_start);
        if (startButton != null) {
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,"Start",Toast.LENGTH_SHORT).show();
                    if (adapter!=null){
                        adapter.clear();
                    }
                    initialize();
                }
            });
        }
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new ProducerConsumerAdapter(new ProducerConsumerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Toast.makeText(MainActivity.this, "itemId=" + id, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ProducerConsumerEvent event) {
        Log.d(LOG_TAG, event.getMessage());
        switch (event.getType()){
            case WAIT:

                break;
            case FINISH:
                animateProducer(R.drawable.empty_basket);
                setStartVisible(true);
                break;
            case PRODUCE:
                animateProducer(R.drawable.basket);
                break;
            case CONSUME:
                animateConsumer(event.getId());
                break;
        }
       if (adapter!=null){
           adapter.addItem(event.getMessage());
           recyclerView.scrollToPosition(adapter.getItemCount()-1);
       }
    }

    private void animateProducer(int resId) {
        if (producerImage !=null){
            producerImage.setImageResource(resId);
        }
    }

    private void animateConsumer( int consumerId){
        switch (consumerId){
            case R.id.consumer1_image:
                Animations.animateConsumer(producerImage, consumer1Image,
                        xConsumer1, yConsumer1, xBucket, yBucket);
                break;
            case R.id.consumer2_image:
                Animations.animateConsumer(producerImage, consumer2Image,
                        xConsumer2, yConsumer2, xBucket, yBucket);
                break;
            case R.id.consumer3_image:
                Animations.animateConsumer(producerImage, consumer3Image,
                        xConsumer3, yConsumer3, xBucket, yBucket);
                break;
        }
    }

    private void setStartVisible(boolean visible){
        if (startButton != null) {
            startButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasWindowFocus()){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
