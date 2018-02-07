package com.hadarfem.fixit.toolbars;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

// Comment for commit
public class SplitToolbar extends Toolbar {
    public SplitToolbar(Context context) {
        super(context);
    }

    public SplitToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SplitToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (child instanceof ActionMenuView) {
            params.width = LayoutParams.MATCH_PARENT;
        }
        super.addView(child, params);
    }
}
