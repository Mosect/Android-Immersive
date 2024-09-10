package com.mosect.app.immersivedemo;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mosect.lib.immersive.ImmersiveLayout;
import com.mosect.lib.immersive.LayoutAdapter;

public class FullActivity extends AppCompatActivity implements LayoutAdapter {

    private View lyTitle;
    private boolean fullScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersiveLayout.lightStatusBar(this);

        ImmersiveLayout immersiveLayout = new ImmersiveLayout(this);
        immersiveLayout.addAdapter(this);
        setContentView(R.layout.activity_full);
        lyTitle = findViewById(R.id.ly_title);
        findViewById(R.id.btn_toggleFull).setOnClickListener(v -> {
            setFullScreen(!fullScreen);
        });
        setFullScreen(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersiveLayout.lightStatusBar(this);
        setFullScreen(fullScreen);
    }

    private void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;

        View decorView = getWindow().getDecorView();
        int sui = decorView.getSystemUiVisibility();
        if (fullScreen) {
            sui |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            sui &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        decorView.setSystemUiVisibility(sui);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(attrs);
        }
    }

    @Override
    public void onAdjustLayoutPadding(ImmersiveLayout layout) {
        lyTitle.setPadding(0, layout.getPaddingTop(), 0, 0);
    }
}
