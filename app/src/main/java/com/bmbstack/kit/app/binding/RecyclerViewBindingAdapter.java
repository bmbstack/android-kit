package com.bmbstack.kit.app.binding;

import com.blankj.utilcode.util.SizeUtils;
import com.bmbstack.kit.decoration.SpaceItemDecoration;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBindingAdapter {

    @BindingAdapter("recycledViewPool")
    public static void bindRecycledViewPool(RecyclerView recyclerView, RecyclerView.RecycledViewPool recycledViewPool) {
        recyclerView.setRecycledViewPool(recycledViewPool);
    }

    @BindingAdapter({"horizontalSpace", "verticalSpace"})
    public static void bindRecycledViewItemDecoration(RecyclerView recyclerView,
                                                      int horizontalSpace, int verticalSpace) {
        recyclerView.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(horizontalSpace), SizeUtils.dp2px(verticalSpace)));
    }
}
