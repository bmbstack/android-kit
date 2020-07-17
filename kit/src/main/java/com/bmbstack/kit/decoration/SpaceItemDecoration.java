package com.bmbstack.kit.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.HashMap;
import java.util.Map;

/**
 * SpaceItemDecoration (horizontalSpace, verticalSpace)
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int horizontalSpace;
    private int verticalSpace;
    private Map<Integer, Integer> gridPositionSpanSizeMapper = new HashMap<>();

    public SpaceItemDecoration(int horizontalSpace, int verticalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 得到当前Item在RecyclerView中的位置,从0开始
        int position = parent.getChildAdapterPosition(view);
        // 得到RecyclerView中Item的总个数
        int count = parent.getAdapter().getItemCount();

        if (position < 0) {
            gridPositionSpanSizeMapper.clear();
            return;
        }

        if (parent.getLayoutManager() instanceof GridLayoutManager) { // 网格布局
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            // 得到网格布局的列数
            int spanCount = gridLayoutManager.getSpanCount();
            int spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(position);
            int gridPreSpanSizeSum = computeGridPreSpanSizeSum(position);
            // 判断该网格布局是水平还是垂直
            if (LinearLayoutManager.VERTICAL == gridLayoutManager.getOrientation()) { // 垂直
                if (spanCount == 1 || (spanSize == spanCount)) { // 列数为1
                    verticalColumnOne(outRect, position);
                } else { // 列数大于1
                    verticalColumnMulti(outRect, spanCount, gridPreSpanSizeSum);
                }
            } else if (LinearLayoutManager.HORIZONTAL == gridLayoutManager.getOrientation()) { // 水平
                if (spanCount == 1 || (spanSize == spanCount)) { // 行数为1
                    horizontalRowOne(outRect, position);
                } else { // 行数大于1
                    horizontalRowMulti(outRect, spanCount, gridPreSpanSizeSum);
                }
            }

            if (position + 1 < count) {
                int need = spanCount - (gridPreSpanSizeSum % spanCount);
                if (spanSize > need) {
                    spanSize += need;
                }
            }

            gridPositionSpanSizeMapper.put(position, spanSize);
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) { // 线性布局
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            if (LinearLayoutManager.VERTICAL == layoutManager.getOrientation()) { // 垂直
                verticalColumnOne(outRect, position);
            } else if (LinearLayoutManager.HORIZONTAL == layoutManager.getOrientation()) { // 水平
                horizontalRowOne(outRect, position);
            }
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) { // 流布局
            //TODO 瀑布流布局相关
        }
    }

    /**
     * 计算position之前spanSize之和
     *
     * @param position
     * @return
     */
    private int computeGridPreSpanSizeSum(int position) {
        int gridPreSpanSizeSum = 0;
        for (Map.Entry<Integer, Integer> entry : gridPositionSpanSizeMapper.entrySet()) {
            if (entry.getKey() < position) {
                gridPreSpanSizeSum += entry.getValue();
            }
        }
        return gridPreSpanSizeSum;
    }

    /**
     * 列表垂直且列数为1
     *
     * @param outRect  包括左上右下四个参数，分别控制view左上右下的margin
     * @param position 当前view所处位置
     */
    private void verticalColumnOne(Rect outRect, int position) {
        if (position == 0) { // 位置为0时
            outRect.set(0, 0, 0, verticalSpace / 2);
        } else { // 中间的Item，不设置底部间距
            outRect.set(0, verticalSpace / 2, 0, verticalSpace / 2);
        }
    }

    /**
     * 列表垂直且列数大于1
     *
     * @param outRect            包括左上右下四个参数，分别控制view左上右下的margin
     * @param spanCount          布局的列数
     * @param gridPreSpanSizeSum 之前spanSize之和
     */
    private void verticalColumnMulti(Rect outRect, int spanCount, int gridPreSpanSizeSum) {
        // 计算得出当前view所在的行
        int row = gridPreSpanSizeSum / spanCount;
        // 列index
        int column = gridPreSpanSizeSum % spanCount;

        int left = (column * horizontalSpace) / spanCount;
        int right = horizontalSpace - (column + 1) * horizontalSpace / spanCount;
        if (column == 0) { // 列头
            outRect.set(left,
                    row == 0 ? 0 : verticalSpace / 2,
                    right,
                    verticalSpace / 2);
        } else if (column == (spanCount - 1)) { // 列尾
            outRect.set(left, row == 0 ? 0 : verticalSpace / 2, right, verticalSpace / 2);
        } else { // 列中
            outRect.set(left, row == 0 ? 0 : verticalSpace / 2, right, verticalSpace / 2);
        }
    }

    /**
     * 列表水平且行数为1
     *
     * @param outRect  包括左上右下四个参数，分别控制view左上右下的margin
     * @param position 当前view所处位置
     */
    private void horizontalRowOne(Rect outRect, int position) {
        if (position == 0) { // 位置为0时
            outRect.set(0, 0, horizontalSpace / 2, 0);
        } else {
            outRect.set(horizontalSpace / 2, 0, horizontalSpace / 2, 0);
        }
    }

    /**
     * 列表水平且行数大于1
     *
     * @param outRect            包括左上右下四个参数，分别控制view左上右下的margin
     * @param spanCount          布局的行数
     * @param gridPreSpanSizeSum 之前spanSize之和
     */
    private void horizontalRowMulti(Rect outRect, int spanCount, int gridPreSpanSizeSum) {
        // 计算得出当前view所在的列
        int column = gridPreSpanSizeSum / spanCount;
        // 通过对position加1对spanCount取余得到row
        // 保证row等于1为第一行，等于0为最后一个，其它值为中间行
        int row = gridPreSpanSizeSum % spanCount;

        int top = (row * verticalSpace) / spanCount;
        int bottom = verticalSpace - (row + 1) * verticalSpace / spanCount;
        if (row == (spanCount - 1) && spanCount != 1) { // 最后一行
            outRect.set(column == 0 ? 0 : horizontalSpace / 2, verticalSpace / 2, horizontalSpace / 2, bottom);
        } else if (row == 0 && spanCount != 1) { // 第一行
            outRect.set(column == 0 ? 0 : horizontalSpace / 2, top, horizontalSpace / 2, bottom);
        } else {
            outRect.set(column == 0 ? 0 : horizontalSpace / 2, top, horizontalSpace / 2, bottom);
        }
    }

}