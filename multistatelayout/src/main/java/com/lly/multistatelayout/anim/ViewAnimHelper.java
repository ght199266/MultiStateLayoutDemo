package com.lly.multistatelayout.anim;

import android.view.View;
import android.view.animation.Animation;

/**
 * ViewAnimHelper[v 1.0.0]
 * classes:com.lly.test.multistatelayout.anim.ViewAnimHelper
 *
 * @author lileiyi
 * @date 2017/3/27
 * @time 11:21
 * @description
 */

public class ViewAnimHelper {

    /**
     * 布局切换效果
     *
     * @param hideView         隐藏的View
     * @param showView         显示的View
     * @param isAnimation      是否需要动画
     * @param viewAnimProvider 动画管理
     */
    public static void switchViewEffect(final View hideView, final View showView, boolean isAnimation, ViewAnimProvider viewAnimProvider) {
        if (isAnimation) {
            final Animation hideViewAnimation = viewAnimProvider.hideViewAnimation();
            final Animation showViewAnimation = viewAnimProvider.showViewAnimation();
            hideViewAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    hideView.setVisibility(View.GONE);
                    showView.setVisibility(View.VISIBLE);
                    showView.startAnimation(showViewAnimation);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            hideView.startAnimation(hideViewAnimation);

        } else {
            hideView.setVisibility(View.GONE);
            showView.setVisibility(View.VISIBLE);
        }
    }

}
