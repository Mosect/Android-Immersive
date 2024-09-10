package com.mosect.app.immersivedemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mosect.lib.immersive.ImmersiveLayout;
import com.mosect.lib.immersive.LayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View lyBottom = findViewById(R.id.ly_bottom);

        WindowInsetsControllerCompat wic = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        ImmersiveLayout immersiveLayout = new ImmersiveLayout(this);
        immersiveLayout.addAdapter(layout -> {
            // 适配底部
            lyBottom.setPadding(0, 0, 0, layout.getPaddingBottom());
            // 触发fragment.onAdjustLayoutPadding
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof LayoutAdapter) {
                    ((LayoutAdapter) fragment).onAdjustLayoutPadding(layout);
                }
            }
        });

        TabLayout lyTabs = findViewById(R.id.ly_tabs);
        ViewPager2 vpContent = findViewById(R.id.vp_content);
        vpContent.setUserInputEnabled(false);

        List<PageInfo> pages = new ArrayList<>();
        pages.add(createPage(new HomeFragment(), R.drawable.tab_home, R.string.tab_home));
        pages.add(createPage(new FindFragment(), R.drawable.tab_find, R.string.tab_find));
        pages.add(createPage(new MyFragment(), R.drawable.tab_my, R.string.tab_my));

        vpContent.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return pages.get(position).fragment;
            }

            @Override
            public int getItemCount() {
                return pages.size();
            }
        });

        TabLayoutMediator mediator = new TabLayoutMediator(lyTabs, vpContent, false, false, (tab, position) -> {
            tab.setCustomView(R.layout.tab_main);
            View view = tab.getCustomView();
            TextView tvLabel = view.findViewById(R.id.tv_label);
            ImageView ivIcon = view.findViewById(R.id.iv_icon);
            PageInfo page = pages.get(position);
            tvLabel.setText(page.label);
            ivIcon.setImageDrawable(page.icon);
        });
        mediator.attach();

        // 请求沉浸式布局
        immersiveLayout.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersiveLayout.lightStatusBar(this);
    }

    private PageInfo createPage(Fragment fragment, int iconResId, int labelResId) {
        Drawable icon = ContextCompat.getDrawable(this, iconResId);
        String label = getString(labelResId);
        return new PageInfo(fragment, icon, label);
    }

    static class PageInfo {
        final Fragment fragment;
        final Drawable icon;
        final String label;

        public PageInfo(Fragment fragment, Drawable icon, String label) {
            this.fragment = fragment;
            this.icon = icon;
            this.label = label;
        }
    }
}
