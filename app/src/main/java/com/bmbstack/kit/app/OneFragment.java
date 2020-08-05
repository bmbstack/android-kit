package com.bmbstack.kit.app;

import android.os.Bundle;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ColorUtils;
import com.bmbstack.kit.app.databinding.FragmentOneBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class OneFragment extends BaseFragment<FragmentOneBinding> {

    @Override
    protected void initCreateView(ViewGroup container, Bundle savedInstanceState) {
        mTitleBar.setBgColor(ColorUtils.setAlphaComponent(getColorValue(R.color.black), 0.8f));
        mTitleBar.setTextColor(getColorValue(R.color.white));
        mTitleBar.setStatusBarLightMode(false);

        mBinding.setData(this);

        requireSmart().setEnableRefresh(true);
        requireSmart().setEnableLoadMore(true);

        List<OneEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new OneEntity(String.valueOf(i + 1)));
        }

        items.clear();
        items.addAll(list);
    }

    @Override
    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.FLOAT_SMART;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one;
    }

    //=================================================================================
    //
    //                                 Data binding
    //
    //=================================================================================
    public final ObservableList<OneEntity> items = new ObservableArrayList<>();
    public final OnItemBind<OneEntity> itemBinding = new OnItemBind<OneEntity>() {
        @Override
        public void onItemBind(@NonNull ItemBinding itemBinding, int position, OneEntity item) {
            itemBinding.set(BR.data, R.layout.item_one);
        }
    };
}
