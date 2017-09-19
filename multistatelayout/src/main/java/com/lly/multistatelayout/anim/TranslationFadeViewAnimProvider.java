package com.lly.multistatelayout.anim;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * TranslationFadeViewAnimProvider[v 1.0.0]
 * classes:com.lly.test.multistatelayout.anim.TranslationFadeViewAnimProvider
 *
 * @author lileiyi
 * @date 2017/3/27
 * @time 16:33
 * @description
 */


public class TranslationFadeViewAnimProvider implements ViewAnimProvider {
    @Override
    public Animation showViewAnimation() {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 300, 0);
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        set.setDuration(200);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(alphaAnimation);
        set.addAnimation(translateAnimation);
        return set;
    }

    @Override
    public Animation hideViewAnimation() {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -300);
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        set.setDuration(200);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(alphaAnimation);
        set.addAnimation(translateAnimation);
        return set;
    }
}
