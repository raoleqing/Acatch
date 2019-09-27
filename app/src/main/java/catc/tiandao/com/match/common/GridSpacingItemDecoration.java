package catc.tiandao.com.match.common;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

    private int spanCount;
    private int spacing;
    private int buttomSpacing;
    private boolean includeEdge;

    /*
    spanCount:跟布局里面的spanCount属性是一致的
    spacing 每一个矩形的间距
    includeEdge 如果设置成false那边缘地带就没有间距
    */
    public GridSpacingItemDecoration(int spanCount, int spacing,boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.buttomSpacing = spacing;
        this.includeEdge = includeEdge;
    }


   /*
    spanCount:跟布局里面的spanCount属性是一致的
    spacing 每一个矩形的间距
    includeEdge 如果设置成false那边缘地带就没有间距
    */
    public GridSpacingItemDecoration(int spanCount, int spacing, int buttomSpacing,boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.buttomSpacing = buttomSpacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = buttomSpacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
