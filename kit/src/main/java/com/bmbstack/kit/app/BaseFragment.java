package com.bmbstack.kit.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.blankj.utilcode.util.ColorUtils;
import com.bmbstack.kit.R;
import com.bmbstack.kit.databinding.LayoutBaseFloatBinding;
import com.bmbstack.kit.databinding.LayoutBaseNormalBinding;
import com.bmbstack.kit.widget.LoadingLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment {
    private static final String TAG = "PageHook";

    protected V mBinding;
    protected TitleBarView mTitleBar;
    protected LoadingLayout mLoadingLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onCreateView");
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        if (getTitleBarMode() == null) {
            throw new NullPointerException("Title bar mode is null");
        }
        View rootView;
        TitleBarMode mode = getTitleBarMode();
        if (mode == TitleBarMode.GONE) {
            LayoutBaseNormalBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_base_normal, container, false);
            binding.titleBar.setVisibility(View.GONE);
            binding.container.addView(mBinding.getRoot(), params);

            mTitleBar = binding.titleBar;
            rootView = binding.getRoot();
        } else if (mode == TitleBarMode.GONE_WITH_TOP) {
            LayoutBaseNormalBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_base_normal, container, false);
            binding.titleBar.setVisibility(View.GONE);
            binding.container.addView(mBinding.getRoot(), params);

            LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) binding.container.getLayoutParams();
            contentParams.topMargin = StatusBarUtil.getStatusBarHeight();
            binding.container.setLayoutParams(contentParams);

            mTitleBar = binding.titleBar;
            rootView = binding.getRoot();
        } else if (mode == TitleBarMode.NORMAL) {
            LayoutBaseNormalBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_base_normal, container, false);
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.container.addView(mBinding.getRoot(), params);

            mTitleBar = binding.titleBar;
            rootView = binding.getRoot();
        } else if (mode == TitleBarMode.FLOAT) {
            LayoutBaseFloatBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_base_float, container, false);
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.container.addView(mBinding.getRoot(), params);

            mTitleBar = binding.titleBar;
            rootView = binding.getRoot();
        } else {
            throw new RuntimeException("Title bar mode is error");
        }

        mTitleBar.setImmersible(requireActivity(), true, true, true);

        mLoadingLayout = LoadingLayout.wrap(mBinding.getRoot());
        mLoadingLayout.setLoading(R.layout.layout_loading_default);
        mLoadingLayout.setEmpty(R.layout.layout_empty_default);
        mLoadingLayout.setError(R.layout.layout_error_default);
        mLoadingLayout.setRetryListener(v -> onRetry());
        mLoadingLayout.showContent();

        initCreateView((ViewGroup) mBinding.getRoot(), savedInstanceState);
        return rootView;
    }

    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.GONE;
    }

    protected abstract void initCreateView(ViewGroup container, Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected void onRetry() {
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onRetry");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "[" + getClass().getCanonicalName() + "] onDetach");
    }

    /**
     * 兼容OPhone
     */
    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.startActivityIfNeeded(intent, -1);
            }
        }
    }

    /**
     * 兼容OPhone
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.startActivityIfNeeded(intent, requestCode);
            }
        }
    }

    protected int getColorValue(int resId) {
        return getResources().getColor(resId);
    }

    protected Drawable getDrawableValue(@DrawableRes int resId) {
        return ResourcesCompat.getDrawable(getResources(), resId, requireActivity().getTheme());
    }

    protected int getAlphaColorValue(int resId, @FloatRange(from = 0, to = 1) float alpha) {
        return ColorUtils.setAlphaComponent(getColorValue(resId), alpha);
    }

    protected int getAlphaColorValue(int resId, @IntRange(from = 0x0, to = 0xFF) int alpha) {
        return ColorUtils.setAlphaComponent(getColorValue(resId), alpha);
    }

    protected boolean isValid() {
        return !requireActivity().isFinishing();
    }
}