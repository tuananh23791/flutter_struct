package com.hm.gillcaptital.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hm.gillcaptital.R;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class PaddingDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;

    private boolean isPaddingLeft;
    private boolean isPaddingRight;

    /**
     * Default divider will be used
     */
    public PaddingDividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public PaddingDividerItemDecoration(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }

    public PaddingDividerItemDecoration paddingLeft() {
        isPaddingLeft = true;
        return this;
    }

    public PaddingDividerItemDecoration paddingRight() {
        isPaddingRight = true;
        return this;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int padding = parent.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        int left = parent.getPaddingLeft() + (isPaddingLeft ? padding : 0);
        int right = parent.getWidth() - parent.getPaddingRight() - (isPaddingRight ? padding : 0);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
