package com.bmbstack.kit.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bmbstack.kit.app.databinding.ActivityTwoBinding;

public class TwoActivity extends BaseActivity<ActivityTwoBinding> {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, TwoActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_from_bottom_enter, R.anim.slide_in_from_bottom_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_two;
    }

    @Override
    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.GONE_WITH_TOP;
    }
}
