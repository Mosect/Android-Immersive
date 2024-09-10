# Android-Immersive
Android沉浸式布局适配

[![](https://jitpack.io/v/Mosect/Android-Immersive.svg)](https://jitpack.io/#Mosect/Android-Immersive)

# 使用指引

## 引入jitpack仓库
```
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

## 在app模块加入依赖
```
dependencies {
  implementation 'com.github.Mosect:Android-Immersive:1.0.5'
}
```

## 在需要适配的Activity.onCreate中加入以下代码
```

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(xxx);
        
        // 初始化页面布局，使用findViewById查找好页面视图
        
        ImmersiveLayout immersiveLayout = new ImmersiveLayout(this);
        immersiveLayout.addAdapter(new LayoutAdapter() {
            @Override
            public void onAdjustLayoutPadding(ImmersiveLayout layout) {
                // 在此处调整页面布局
                // 左边距：layout.getPaddingLeft()
                // 右边距：layout.getPaddingRight()
                // 上边距：layout.getPaddingTop()
                // 下边距：layout.getPaddingBottom()
                // 上边距用来适配标题沉浸式（状态栏），下边距用来适配底部导航栏沉浸式（虚拟按键）
            }
        });

        // 请求沉浸式布局
        immersiveLayout.requestLayout();
    }
```

# 适配标题栏、导航栏模式
因为是全透明沉浸式，如果不调整状态栏和导航栏颜色模式，会导致状态栏或导航栏看不到问题，在onResume方法中加入适配代码：
```
    @Override
    protected void onResume() {
        super.onResume();
        // 顶部整体比较亮，则启用明亮状态栏模式
        // ImmersiveLayout.lightStatusBar(this);
        // 顶部整体比较暗，则启用暗黑状态栏模式
        // ImmersiveLayout.darkStatusBar(this);
        // 底部整体比较亮，则启用明亮导航栏模式
        // ImmersiveLayout.lightNavigationBar(this);
        // 底部整体比较暗，则启用暗黑导航栏模式
        // ImmersiveLayout.darkNavigationBar(this);
    }
```

# 其他
[个人主页:mosect.com](http://www.mosect.com)

[Bilibili主页：Mosect](https://space.bilibili.com/60944161)

[AcFun主页：Mosect](https://www.acfun.cn/u/67792172)

[Gitee主页：Mosect](https://gitee.com/mosect)

