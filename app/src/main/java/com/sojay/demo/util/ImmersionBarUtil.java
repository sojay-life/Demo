package com.sojay.demo.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.sojay.demo.R;

/**
 * 沉浸式逻辑处理
 *
 * @author guodong
 * @datetime 2018/11/22 19:04.
 */
public class ImmersionBarUtil {

    /**
     * 全屏style
     *
     * @param context
     */
    public static void fullScreenStyle(Activity context) {
        ImmersionBar.with(context).fullScreen(true).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
    }

    /**
     * 首页沉浸式style
     *
     * @param context
     */
    public static void mainStyle(Activity context, View statusView) {
        ImmersionBar.with(context).statusBarView(statusView).fitsSystemWindows(false).statusBarDarkFont(false).init();
    }

    public static void mainWhiteStyle(Activity context, View statusView) {
        ImmersionBar.with(context).statusBarView(statusView).fitsSystemWindows(false).statusBarDarkFont(true).init();
    }

    public static void mainBlackStyle(Activity context, View statusView) {
        ImmersionBar.with(context).statusBarView(statusView).fitsSystemWindows(false).init();
    }


    /**
     * 状态栏白底黑字
     *
     * @param context
     */
    public static void whiteStyle(Activity context) {
        ImmersionBar.with(context).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
    }
 /**
     * 状态栏白底黑字 显示状态栏
     *
     * @param context
     */
    public static void whiteshowStyle(Activity context) {
        ImmersionBar.with(context).hideBar(BarHide.FLAG_SHOW_BAR).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).keyboardEnable(false).init();
    }

    /**
     * 长视频列表播放 状态栏兼容
     *
     * @param context
     * @param statusId
     */
    public static void whiteStyle(Activity context, int statusId) {
        ImmersionBar.with(context).statusBarColor(R.color.white).statusBarDarkFont(true).statusBarView(statusId).init();
    }

    /**
     * 状态栏黑底白字
     *
     * @param context
     */
    public static void blackStyle(Activity context) {
        ImmersionBar.with(context).statusBarColor(R.color.black).fitsSystemWindows(true).init();
    }

    /**
     * 视频全屏
     *
     * @param context
     */
    public static void videoFullScreenStyle(Activity context) {
        ImmersionBar.with(context).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).statusBarDarkFont(false).fitsSystemWindows(false).keyboardEnable(true).init();
    }

    /**
     * 资源回收
     *
     * @param activity
     */
    public static void destory(Activity activity) {
        ImmersionBar.with(activity).destroy();
    }

    /**
     * 资源回收
     *
     * @param fragment
     */
    public static void destory(Fragment fragment) {
        ImmersionBar.with(fragment).destroy();
    }

    public static void setOnKeyboardListener(Activity activity, OnKeyboardListener onKeyboardListener) {
        ImmersionBar.with(activity)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false
                .setOnKeyboardListener(onKeyboardListener)
                .init();
    }

    /**
     * 白字沉浸式状态栏
     * @param activity
     */
    public static void transparentStyle(Activity activity) {
        ImmersionBar.with(activity)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .reset()
                .init();
    }

//    public static void setStatuseBar(Activity context, int colorIds) {
//        if (context == null)
//            return;
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (context.getWindow().getStatusBarColor() == context.getResources().getColor(colorIds))
//                return;
//            context.getWindow().setStatusBarColor(context.getResources().getColor(colorIds));
//            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            context.getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
//    }

    public void test() {
//        ImmersionBar.with(this)
//                .transparentStatusBar()  //透明状态栏，不写默认透明色
//                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
//                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
//                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
//                .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
//                .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
//                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
//                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
//                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//                .flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
//                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
//                .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
//                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
//                .supportActionBar(true) //支持ActionBar使用
//                .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
//                .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
//                .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
//                .removeSupportView(toolbar)  //移除指定view支持
//                .removeSupportAllView() //移除全部view支持
//                .addTag("tag")  //给以上设置的参数打标记
//                .getTag("tag")  //根据tag获得沉浸式参数
//                .reset()  //重置所以沉浸式参数
//                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false
//                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
//                    @Override
//                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//                        LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
//                    }
//                })
//                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
//                .init();  //必须调用方可沉浸式
    }
}
