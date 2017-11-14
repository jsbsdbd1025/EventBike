package com.jiang.eventbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private TextView textView;
    private Button btnSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_hello);

        btnSecond = findViewById(R.id.btn_second);

        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);

        EventBike.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

        EventBike.getDefault().unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(MessageEvent event) {
        textView.setText("Hello EventBus!");
        Log.e(TAG, Thread.currentThread().getName());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(MessageEvent event) {
        textView.setText("Hello EventBus!");
        Log.e(TAG, Thread.currentThread().getName());
    }

}
