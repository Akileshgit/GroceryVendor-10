package CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Deepak on 11/17/2017.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public DividerDecoration(int itemOffset) {

        mItemOffset = itemOffset;

    }



    public DividerDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {

        this(context.getResources().getDimensionPixelSize(itemOffsetId));

    }

    @Override

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,

                               RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(0, 0, 0, mItemOffset);

    }

}