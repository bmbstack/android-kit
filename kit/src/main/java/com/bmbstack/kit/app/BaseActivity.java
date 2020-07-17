package com.bmbstack.kit.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.blankj.utilcode.util.LogUtils;
import com.bmbstack.kit.R;
import com.bmbstack.kit.databinding.LayoutBaseFloatBinding;
import com.bmbstack.kit.databinding.LayoutBaseNormalBinding;
import com.bmbstack.kit.databinding.LayoutBaseScrollBinding;
import com.bmbstack.kit.widget.LoadingLayout;

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected V mBinding;
    protected TitleBarView mTitleBar;
    protected LoadingLayout mLoadingLayout;

    private boolean useAnimation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutId(), null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        if (getTitleBarMode() == null) {
            throw new NullPointerException("Title bar mode is null");
        }
        TitleBarMode mode = getTitleBarMode();
        if (mode == TitleBarMode.GONE) {
            LayoutBaseNormalBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_base_normal);
            binding.titleBar.setVisibility(View.GONE);
            binding.flContent.addView(mBinding.getRoot(), params);

            mTitleBar = binding.titleBar;
        } else if (mode == TitleBarMode.GONE_WITH_TOP) {
            LayoutBaseNormalBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_base_normal);
            binding.titleBar.setVisibility(View.GONE);
            binding.flContent.addView(mBinding.getRoot(), params);

            LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) binding.flContent.getLayoutParams();
            contentParams.topMargin = StatusBarUtil.getStatusBarHeight();
            binding.flContent.setLayoutParams(contentParams);

            mTitleBar = binding.titleBar;
        } else if (mode == TitleBarMode.NORMAL) {
            LayoutBaseNormalBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_base_normal);
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.flContent.addView(mBinding.getRoot(), params);

            mTitleBar = binding.titleBar;
        } else if (mode == TitleBarMode.FLOAT) {
            LayoutBaseFloatBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_base_float);
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.flContent.addView(mBinding.getRoot(), params);

            binding.titleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) binding.flContent.getLayoutParams();
                    contentParams.topMargin = binding.titleBar.getMeasuredHeight();
                    binding.flContent.setLayoutParams(contentParams);
                    binding.flContent.requestLayout();

                    binding.titleBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            mTitleBar = binding.titleBar;
        } else if (mode == TitleBarMode.SCROLL) {
            LayoutBaseScrollBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_base_scroll);
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.flContent.addView(mBinding.getRoot(), params);

            mTitleBar = binding.titleBar;
        } else {
            throw new RuntimeException("Title bar mode is error");
        }

        mTitleBar.setImmersible(this, true, true, true);

        mLoadingLayout = LoadingLayout.wrap(mBinding.getRoot());
        mLoadingLayout.setLoading(R.layout.layout_loading_default);
        mLoadingLayout.setEmpty(R.layout.layout_empty_default);
        mLoadingLayout.setError(R.layout.layout_error_default);
        mLoadingLayout.setRetryListener(v -> onRetry());
        mLoadingLayout.showContent();
    }

    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.GONE;
    }

    protected abstract int getLayoutId();

    protected void onRetry() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void useAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    /**
     * 兼容OPHONE
     */
    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
            if (useAnimation) {
                overridePendingTransition(R.anim.slide_open_enter, R.anim.slide_open_exit);
            }
        } catch (Exception e) {
            LogUtils.eTag(TAG, "startActivity.error=" + e.toString());
            e.printStackTrace();
            try {
                startActivityIfNeeded(intent, -1);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (useAnimation) {
            overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit);
        }
    }

    /**
     * 兼容OPHONE
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
            if (useAnimation) {
                overridePendingTransition(R.anim.slide_open_enter, R.anim.slide_open_exit);
            }
        } catch (Exception e) {
            LogUtils.eTag(TAG, "startActivity.error=" + e.toString());
            e.printStackTrace();
            try {
                startActivityIfNeeded(intent, -1);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    protected int getStatusBarColor() {
        return getColorPrimaryDark();
    }

    protected int getColorPrimaryDark() {
        return getResources().getColor(R.color.colorPrimaryDark);
    }

    protected int getColorValue(int resId) {
        return getResources().getColor(resId);
    }

    protected boolean isValid() {
        return !this.isFinishing();
    }
}
