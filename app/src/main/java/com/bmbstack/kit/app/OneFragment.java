package com.bmbstack.kit.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ColorUtils;
import com.bmbstack.kit.app.databinding.FragmentOneBinding;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

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

        mBinding.smart.setEnableRefresh(true);
        mBinding.smart.setEnableLoadMore(true);

        items.clear();
        items.addAll(createList(0));

        mBinding.smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();

                    items.clear();
                    items.addAll(createList(0));
                }, 1500);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();

                    items.addAll(createList(items.size()));
                }, 1500);
            }
        });
    }

    @Override
    protected TitleBarMode getTitleBarMode() {
        return TitleBarMode.FLOAT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one;
    }

    private List<OneEntity> createList(int from) {
        List<OneEntity> list = new ArrayList<>();
        for (int i = from; i < from + 40; i++) {
            list.add(new OneEntity(String.valueOf(i + 1)));
        }
        return list;
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
