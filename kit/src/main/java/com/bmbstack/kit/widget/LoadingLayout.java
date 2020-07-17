package com.bmbstack.kit.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.bmbstack.kit.R;

import java.util.HashMap;
import java.util.Map;

public class LoadingLayout extends FrameLayout {
    public interface OnInflateListener {
        void onInflate(View inflated);
    }

    public static LoadingLayout wrap(Activity activity) {
        return wrap(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    public static LoadingLayout wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }

    public static LoadingLayout wrap(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (parent == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        LoadingLayout layout = new LoadingLayout(view.getContext());
        layout.addView(view);
        layout.setContentView(view);

        parent.addView(layout, index, params);
        return layout;
    }

    int mEmptyImage;
    CharSequence mEmptyText;

    int mErrorImage;
    CharSequence mErrorText, mRetryText;
    View.OnClickListener mRetryButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRetryListener != null) {
                mRetryListener.onClick(v);
            }
        }
    };
    View.OnClickListener mRetryListener;

    OnInflateListener mOnEmptyInflateListener;
    OnInflateListener mOnErrorInflateListener;

    int mTextColor, mTextSize;
    int mButtonTextColor, mButtonTextSize;
    Drawable mButtonBackground;
    int mEmptyResId = NO_ID, mLoadingResId = NO_ID, mErrorResId = NO_ID;
    int mContentId = NO_ID;

    Map<Integer, View> mLayouts = new HashMap<>();


    public LoadingLayout(Context context) {
        this(context, null, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayout_Style);
        mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, NO_ID);
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText);

        mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, NO_ID);
        mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText);
        mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText);

        mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, 0xff999999);
        mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, dp2px(13));

        mButtonTextColor = a.getColor(R.styleable.LoadingLayout_llButtonTextColor, 0xffffffff);
        mButtonTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llButtonTextSize, dp2px(13));
        mButtonBackground = a.getDrawable(R.styleable.LoadingLayout_llButtonBackground);

        mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout.layout_loading_default);
        mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout.layout_empty_default);
        mErrorResId = a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout.layout_error_default);
        a.recycle();
    }

    int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }


    LayoutInflater mInflater;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
        showLoading();
    }

    private void setContentView(View view) {
        mContentId = view.getId();
        mLayouts.put(mContentId, view);
    }

    public LoadingLayout setLoading(@LayoutRes int id) {
        if (mLoadingResId != id) {
            remove(mLoadingResId);
            mLoadingResId = id;
        }
        return this;
    }

    public LoadingLayout setEmpty(@LayoutRes int id) {
        if (mEmptyResId != id) {
            remove(mEmptyResId);
            mEmptyResId = id;
        }
        return this;
    }

    public LoadingLayout setError(@LayoutRes int id) {
        if (mErrorResId != id) {
            remove(mErrorResId);
            mErrorResId = id;
        }
        return this;
    }

    public LoadingLayout setOnEmptyInflateListener(OnInflateListener listener) {
        mOnEmptyInflateListener = listener;
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts.get(mEmptyResId));
        }
        return this;
    }
    public LoadingLayout setOnErrorInflateListener(OnInflateListener listener) {
        mOnErrorInflateListener = listener;
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts.get(mErrorResId));
        }
        return this;
    }

    public LoadingLayout setEmptyImage(@DrawableRes int resId) {
        mEmptyImage = resId;
        image(mEmptyResId, R.id.empty_image, mEmptyImage);
        return this;
    }
    public LoadingLayout setEmptyText(String value) {
        mEmptyText = value;
        text(mEmptyResId, R.id.empty_text, mEmptyText);
        return this;
    }
    public LoadingLayout setErrorImage(@DrawableRes int resId) {
        mErrorImage = resId;
        image(mErrorResId, R.id.error_image, mErrorImage);
        return this;
    }
    public LoadingLayout setErrorText(String value) {
        mErrorText = value;
        text(mErrorResId, R.id.error_text, mErrorText);
        return this;
    }

    public LoadingLayout setRetryText(String text) {
        mRetryText = text;
        text(mErrorResId, R.id.retry_button, mRetryText);
        return this;
    }

    public LoadingLayout setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
        return this;
    }


    public LoadingLayout setTextColor(@ColorInt int color) {
        mTextColor = color;
        return this;
    }
    public LoadingLayout setTextSize(@ColorInt int dp) {
        mTextSize = dp2px(dp);
        return this;
    }
    public LoadingLayout setButtonTextColor(@ColorInt int color) {
        mButtonTextColor = color;
        return this;
    }
    public LoadingLayout setButtonTextSize(@ColorInt int dp) {
        mButtonTextSize = dp2px(dp);
        return this;
    }
    public LoadingLayout setButtonBackground(Drawable drawable) {
        mButtonBackground = drawable;
        return this;
    }

    public void showLoading() {
        show(mLoadingResId);
    }

    public void showEmpty() {
        show(mEmptyResId);
    }

    public void showError() {
        show(mErrorResId);
    }

    public void showContent() {
        show(mContentId);
    }

    private void show(int layoutId) {
        for (View view : mLayouts.values()) {
            view.setVisibility(GONE);
        }
        layout(layoutId).setVisibility(VISIBLE);
    }

    private void remove(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            View vg = mLayouts.remove(layoutId);
            removeView(vg);
        }
    }

    private View layout(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts.get(layoutId);
        }
        View layout = mInflater.inflate(layoutId, this, false);
        layout.setVisibility(GONE);
        addView(layout);
        mLayouts.put(layoutId, layout);

        if (layoutId == mEmptyResId) {
            ImageView img = layout.findViewById(R.id.empty_image);
            if (img != null && mEmptyImage != -1) {
                img.setImageResource(mEmptyImage);
            }
            TextView view = layout.findViewById(R.id.empty_text);
            if (view != null) {
                view.setText(mEmptyText);
                view.setTextColor(mTextColor);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener.onInflate(layout);
            }
        } else if (layoutId == mErrorResId) {
            ImageView img = layout.findViewById(R.id.error_image);
            if (img != null && mErrorImage != -1) {
                img.setImageResource(mErrorImage);
            }
            TextView txt = layout.findViewById(R.id.error_text);
            if (txt != null) {
                txt.setText(mErrorText);
                txt.setTextColor(mTextColor);
                txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            TextView btn = layout.findViewById(R.id.retry_button);
            if (btn != null) {
                btn.setText(mRetryText);
                btn.setTextColor(mButtonTextColor);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize);
                btn.setBackground(mButtonBackground);
                btn.setOnClickListener(mRetryButtonClickListener);
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener.onInflate(layout);
            }
        }
        return layout;
    }

    private void text(int layoutId, int ctrlId, CharSequence value) {
        if (mLayouts.containsKey(layoutId)) {
            TextView view = (TextView) mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setText(value);
            }
        }
    }

    private void image(int layoutId, int ctrlId, int resId) {
        if (mLayouts.containsKey(layoutId)) {
            ImageView view = (ImageView) mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setImageResource(resId);
            }
        }
    }
}