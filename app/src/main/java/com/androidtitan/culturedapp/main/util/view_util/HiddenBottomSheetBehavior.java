package com.androidtitan.culturedapp.main.util.view_util;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by amohnacs on 4/12/16.
 */
public class HiddenBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V>{

    public HiddenBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {

        SavedState dummySavedState = new SavedState(super.onSaveInstanceState(parent, child), STATE_COLLAPSED);
        super.onRestoreInstanceState(parent, child, dummySavedState);

        return super.onLayoutChild(parent, child, layoutDirection);
    }
    /*
            Unfortunately its not good enough to just call setState(STATE_EXPANDED); after super.onLayoutChild
            The reason is that an animation plays after calling setState. This can cause some graphical issues with other layouts
            Instead we need to use setInternalState, however this is a private method.
            The trick is to utilise onRestoreInstance to call setInternalState immediately and indirectly
         */
}
