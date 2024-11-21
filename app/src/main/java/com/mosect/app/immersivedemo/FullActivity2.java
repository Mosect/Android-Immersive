package com.mosect.app.immersivedemo;

import android.os.Build;
import android.view.View;

public class FullActivity2 extends FullActivity {
    @Override
    public void applyDecorStyle() {
        super.applyDecorStyle();
        View decorView = getWindow().getDecorView();
        int sui = 0;
        // 隐藏导航栏
        sui |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        // 全屏
        sui |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        // 固定布局，系统栏不会影响布局
        sui |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        // 让布局延申到状态栏
        sui |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        // 让布局延申到导航栏
        sui |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 沉浸式交互
            sui |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(sui);
    }
}
