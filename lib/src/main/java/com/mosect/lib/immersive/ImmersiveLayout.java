package com.mosect.lib.immersive;

import android.app.Activity;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 沉浸式布局
 */
public class ImmersiveLayout {

    /**
     * 明亮状态栏
     *
     * @param activity 页面
     */
    public static void lightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int sui = activity.getWindow().getDecorView().getSystemUiVisibility();
            sui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(sui);
        }
    }

    /**
     * 暗黑状态栏
     *
     * @param activity 页面
     */
    public static void darkStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int sui = activity.getWindow().getDecorView().getSystemUiVisibility();
            sui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(sui);
        }
    }

    /**
     * 明亮导航栏
     *
     * @param activity 页面
     */
    public static void lightNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int sui = activity.getWindow().getDecorView().getSystemUiVisibility();
            sui |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(sui);
        }
    }

    /**
     * 暗黑导航栏
     *
     * @param activity 页面
     */
    public static void darkNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int sui = activity.getWindow().getDecorView().getSystemUiVisibility();
            sui &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(sui);
        }
    }

    private static FullScreenInsets fullScreenInsets;

    private static FullScreenInsets getFullScreenInsets() {
        if (null == fullScreenInsets) fullScreenInsets = new FullScreenInsets();
        return fullScreenInsets;
    }

    private final Rect insetsRect = new Rect();
    private final List<LayoutAdapter> adapters = new ArrayList<>();

    public ImmersiveLayout(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0+
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            // 需要移除FLAG_TRANSLUCENT_NAVIGATION和FLAG_TRANSLUCENT_STATUS
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            // 不限制布局
            attrs.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            // 允许在状态栏区域绘制
            attrs.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            View decorView = activity.getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                // 监听insets
                insetsRect.setEmpty();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // Android12+
                    // 以下代码无法适配全屏情况，需要对全屏情况做处理
                    /*DisplayCutout displayCutout = insets.getDisplayCutout();
                    if (null == displayCutout) {
                        Insets systemBarInsets = insets.getInsets(WindowInsets.Type.systemBars());
                        insetsRect.left = systemBarInsets.left;
                        insetsRect.top = systemBarInsets.top;
                        insetsRect.right = systemBarInsets.right;
                        insetsRect.bottom = systemBarInsets.bottom;
                    } else {
                        insetsRect.left = displayCutout.getSafeInsetLeft();
                        insetsRect.top = displayCutout.getSafeInsetTop();
                        insetsRect.right = displayCutout.getSafeInsetRight();
                        insetsRect.bottom = displayCutout.getSafeInsetBottom();
                    }*/

                    // 获取屏幕安全显示区域，由系统栏和屏幕镂空部分确定
                    Insets systemBarInsets = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                    int sui = v.getSystemUiVisibility();
                    boolean fullScreen = (sui & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0;
                    if (fullScreen) {
                        // 全屏情况，系统只会汇报一次insets，其他情况，insets都会被提前消费，因此需要缓存insets
                        FullScreenInsets fullScreenInsets = getFullScreenInsets();
                        fullScreenInsets.put(systemBarInsets.left, systemBarInsets.top, systemBarInsets.right, systemBarInsets.bottom);
                        insetsRect.set(fullScreenInsets.rect);
                    } else {
                        // 非全屏情况就无需处理，直接获取系统即可
                        insetsRect.left = systemBarInsets.left;
                        insetsRect.top = systemBarInsets.top;
                        insetsRect.right = systemBarInsets.right;
                        insetsRect.bottom = systemBarInsets.bottom;
                    }
                    dispatchInsetsChanged();
                    return WindowInsets.CONSUMED;
                } else {
                    // Android5.0 ~ Android11
                    insetsRect.left = Math.max(insets.getSystemWindowInsetLeft(), insets.getStableInsetLeft());
                    insetsRect.right = Math.max(insets.getSystemWindowInsetRight(), insets.getStableInsetRight());
                    insetsRect.top = Math.max(insets.getSystemWindowInsetTop(), insets.getStableInsetTop());
                    insetsRect.bottom = Math.max(insets.getSystemWindowInsetBottom(), insets.getStableInsetBottom());
                    dispatchInsetsChanged();
                    return insets.consumeSystemWindowInsets().consumeStableInsets();
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android4.4、Android4.4w
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            attrs.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.setFitsSystemWindows(false);
            InsetsHelperView helperView = new InsetsHelperView(activity);
            decorView.addView(helperView, new ViewGroup.LayoutParams(0, 0));
            helperView.setOnInsetsChangedListener(insets -> {
                insetsRect.set(insets);
                dispatchInsetsChanged();
            });
            helperView.setFitsSystemWindows(true);
        }

    }

    /**
     * 获取左边距
     *
     * @return 左边距
     */
    public int getPaddingLeft() {
        return insetsRect.left;
    }

    /**
     * 获取上边距
     *
     * @return 上边距
     */
    public int getPaddingTop() {
        return insetsRect.top;
    }

    /**
     * 获取右边距
     *
     * @return 右边距
     */
    public int getPaddingRight() {
        return insetsRect.right;
    }

    /**
     * 获取下边距
     *
     * @return 下边距
     */
    public int getPaddingBottom() {
        return insetsRect.bottom;
    }

    /**
     * 请求布局
     */
    public void requestLayout() {
        dispatchInsetsChanged();
    }

    /**
     * 添加布局适配器
     *
     * @param adapter 布局适配器
     */
    public void addAdapter(LayoutAdapter adapter) {
        adapters.add(adapter);
    }

    /**
     * 移除布局适配器
     *
     * @param adapter 布局适配器
     */
    public void removeAdapter(LayoutAdapter adapter) {
        adapters.remove(adapter);
    }

    protected void dispatchInsetsChanged() {
        for (LayoutAdapter adapter : adapters) {
            adapter.onAdjustLayoutPadding(this);
        }
    }

    private static class FullScreenInsets {
        final Rect rect = new Rect();

        void put(int left, int top, int right, int bottom) {
            if (left > rect.left) rect.left = left;
            if (top > rect.top) rect.top = top;
            if (right > rect.right) rect.right = right;
            if (bottom > rect.bottom) rect.bottom = bottom;
        }
    }
}
