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
        mTitleBar.setBgColor(ColorUtils.setAlphaComponent(getColorValue(android.R.color.holo_blue_light), 0.92f));
        mTitleBar.setTextColor(getColorValue(R.color.white));
        mTitleBar.setStatusBarLightMode(false);

        mBinding.setData(this);

        List<OneEntity> list = new ArrayList<>();
        list.add(new OneEntity("1"));
        list.add(new OneEntity("2"));
        list.add(new OneEntity("3"));
        list.add(new OneEntity("4"));

        items.clear();
        items.addAll(list);

//        mLoadingLayout.showLoading();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mLoadingLayout.showContent();
//            }
//        }, 2000);
    }

    @Override
    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.FLOAT;
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
