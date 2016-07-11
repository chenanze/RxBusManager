package com.chenanze.rxbusmanager_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenanze.rxbusmanager.RxBusManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import navgnss.com.rxbusmanager_sample.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.status_1_tv)
    TextView status1Tv;
    @BindView(R.id.status_2_tv)
    TextView status2Tv;
    @BindView(R.id.test1_bt)
    Button test1Bt;
    @BindView(R.id.test2_bt)
    Button test2Bt;
    @BindView(R.id.post_bt)
    Button postBt;

    private Boolean status1 = false;
    private Boolean status2 = false;

    @OnClick(R.id.test1_bt)
    void OnClick1(View v) {
        Log.d(TAG, "pre status1: " + status1);
        status1 = reverse(status1);
        Log.d(TAG, "status1: " + status1);
        if (status1) {
            RxBusManager.getInstance().t(TAG).on(C.EVENT_TEST, o -> {
                Log.d(TAG, (String) o);
                status1Tv.setText((String) o);
            });
            test1Bt.setText("unregister");
        } else {
            RxBusManager.getInstance().unregister(C.EVENT_TEST, TAG);
            test1Bt.setText("register");
        }

    }

    @OnClick(R.id.test2_bt)
    void OnClick2(View v) {
        status2 = reverse(status2);
        if (status2) {
            RxBusManager.getInstance().t(TAG).on(C.EVENT_TEST, o -> {
                Log.d(TAG, (String) o);
                status2Tv.setText((String) o);
            });
            test2Bt.setText("unregister");
        } else {
            RxBusManager.getInstance().unregister(C.EVENT_TEST, TAG);
            test2Bt.setText("register");
        }
    }

    @OnClick(R.id.post_bt)
    void OnClickPost(View v) {
        RxBusManager.getInstance().t(TAG).post(C.EVENT_TEST, "test");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public boolean reverse(Boolean status) {
        if (status == false)
            return true;
        else
            return false;
    }
}
