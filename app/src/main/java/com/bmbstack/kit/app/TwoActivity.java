package com.bmbstack.kit.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bmbstack.kit.app.databinding.ActivityTwoBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TwoActivity extends BaseActivity<ActivityTwoBinding> {
    private Timer mTimer = new Timer();
    private Handler mHandler = new Handler(message -> {
        mBinding.tvContent.setText("Dragger " + DateFormat.getDateTimeInstance().format(new Date()));
        return true;
    });

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, TwoActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.open_enter_from_bottom, R.anim.open_exit_to_bottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_two;
    }

    @Override
    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.GONE;
    }
}
