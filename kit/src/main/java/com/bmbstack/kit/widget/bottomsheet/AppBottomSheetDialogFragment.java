package com.bmbstack.kit.widget.bottomsheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bmbstack.kit.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class AppBottomSheetDialogFragment<V extends ViewDataBinding> extends BottomSheetDialogFragment {
    private LockableBottomSheetBehavior lockableBottomSheetBehavior;
    private DialogDismissListener dismissListener;

    protected V mBinding;

    protected abstract int getLayoutId();

    protected abstract int getDefaultHeight();

    protected abstract void onDialogLoaded();

    protected boolean draggableLocked() {
        return true;
    }

    protected boolean cancelable() {
        return true;
    }

    protected boolean canceledOnTouchOutside() {
        return true;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;
            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) bottomSheet.getParent();

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getDefaultHeight();
            bottomSheet.requestLayout();

            lockableBottomSheetBehavior = new LockableBottomSheetBehavior();
            lockableBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            lockableBottomSheetBehavior.setLocked(draggableLocked());
            lockableBottomSheetBehavior.setSkipCollapsed(true);
            layoutParams.setBehavior(lockableBottomSheetBehavior);

            coordinatorLayout.getParent().requestLayout();
        });
        dialog.setCancelable(cancelable());
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside());

        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        onDialogLoaded();
        return mBinding.getRoot();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }

    public void setDismissListener(DialogDismissListener listener) {
        dismissListener = listener;
    }
}
