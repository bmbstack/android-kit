package com.bmbstack.kit.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.bmbstack.kit.R;
import com.bmbstack.kit.databinding.LayoutBaseFloatBinding;
import com.bmbstack.kit.databinding.LayoutBaseFloatSmartBinding;
import com.bmbstack.kit.databinding.LayoutBaseNormalBinding;
import com.bmbstack.kit.widget.LoadingLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected V mBinding;
    protected TitleBarView mTitleBar;
    protected LoadingLayout mLoadingLayout;

    @Nullable
    private SmartRefreshLayout mSmart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

            binding.titleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    binding.titleBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) binding.container.getLayoutParams();
                    contentParams.topMargin = binding.titleBar.getMeasuredHeight();
                    binding.container.setLayoutParams(contentParams);
                    binding.container.requestLayout();
                }
            });

            mTitleBar = binding.titleBar;
            rootView = binding.getRoot();
        } else if (mode == TitleBarMode.FLOAT_SMART) {
            LayoutBaseFloatSmartBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_base_float_smart, container, false);
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.container.addView(mBinding.getRoot(), params);

            binding.titleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    binding.titleBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) binding.container.getLayoutParams();
                    contentParams.topMargin = binding.titleBar.getMeasuredHeight();
                    binding.container.setLayoutParams(contentParams);
                    binding.container.requestLayout();
                }
            });

            mTitleBar = binding.titleBar;
            mSmart = binding.smart;
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

    }

    @NonNull
    public SmartRefreshLayout requireSmart() {
        if (mSmart == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + "'s TitleBarMode must be smart type.");
        }
        return mSmart;
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected int getColorValue(int resId) {
        return getResources().getColor(resId);
    }

    protected boolean isValid() {
        return !requireActivity().isFinishing();
    }
}