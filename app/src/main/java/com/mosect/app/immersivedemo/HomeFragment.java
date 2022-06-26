package com.mosect.app.immersivedemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mosect.lib.immersive.ImmersiveLayout;
import com.mosect.lib.immersive.LayoutAdapter;

public class HomeFragment extends Fragment implements LayoutAdapter {

    private View titleView;

    @Override
    public void onResume() {
        super.onResume();
        ImmersiveLayout.lightStatusBar(getActivity());
        ImmersiveLayout.lightNavigationBar(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleView = view.findViewById(R.id.ly_title);
    }

    @Override
    public void onAdjustLayoutPadding(ImmersiveLayout layout) {
        if (null != titleView) {
            titleView.setPadding(0, layout.getPaddingTop(), 0, 0);
        }
    }
}
