package com.bmbstack.kit.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bmbstack.kit.app.databinding.ActivityOneBinding;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class OneActivity extends BaseActivity<ActivityOneBinding> {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, OneActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new OneFragment());
        transaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_one;
    }
}
