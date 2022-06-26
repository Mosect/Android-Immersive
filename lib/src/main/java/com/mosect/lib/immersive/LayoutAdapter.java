package com.mosect.lib.immersive;

/**
 * 沉浸式布局适配器
 */
public interface LayoutAdapter {

    /**
     * 调整布局边距
     *
     * @param layout 沉浸式布局对象
     */
    void onAdjustLayoutPadding(ImmersiveLayout layout);
}
