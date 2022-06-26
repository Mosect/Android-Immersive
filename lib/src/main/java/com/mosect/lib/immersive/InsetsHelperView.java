package com.mosect.lib.immersive;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Android4.4版本，insets计算辅助视图
 */
public class InsetsHelperView extends View {

    private OnInsetsChangedListener onInsetsChangedListener;

    public InsetsHelperView(Context context) {
        super(context);
    }

    public InsetsHelperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsetsHelperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (null != onInsetsChangedListener) {
            onInsetsChangedListener.onInsetsChanged(insets);
        }
        return true;
    }

    public void setOnInsetsChangedListener(OnInsetsChangedListener onInsetsChangedListener) {
        this.onInsetsChangedListener = onInsetsChangedListener;
    }

    /**
     * Insets发生更改监听器
     */
    public interface OnInsetsChangedListener {

        /**
         * Insets发生更改
         *
         * @param insets insets
         */
        void onInsetsChanged(Rect insets);
    }
}
