package com.lly.multistatelayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lly.multistatelayout.anim.FadeScaleViewAnimProvider;
import com.lly.multistatelayout.anim.ViewAnimHelper;
import com.lly.multistatelayout.anim.ViewAnimProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * MultiStateLayout[v 1.0.0]
 * classes:xunfeng.xinchang.customview.MultiStateLayout
 *
 * @author lileiyi
 * @date 2017/3/24
 * @time 16:27
 * @description
 */

public class MultiStateLayout extends FrameLayout implements VOperationInterface {
    /**
     * 创建各种状态的View
     */
    private MultiViewFactory mMultiViewFactory;
    /**
     * View切换是否需要动画效果
     */
    private boolean isAnimation = true;
    /**
     * 当前显示的View
     */
    private View mCurrentView;

    /**
     * 各种状态的View
     */
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mNoNetWWork;
    private View mLoginView;

    private TextView tv_error_info;

    private TextView tv_empty;


    private String mUnLoginInfo;

    private long lastClickTime;

    private boolean isComplete;

    private String mEmptyHint;

    /**
     * 内容View
     */
    private View[] mContentViews;

    /**
     * 重试接口
     */
    private onRetryListener mOnRetryListener;

    /**
     * 动画提供者
     */
    private ViewAnimProvider mViewAnimProvider;


    private List<Animator> mAnimations;

    public void setRetryListener(onRetryListener mOnRetryListener) {
        this.mOnRetryListener = mOnRetryListener;
    }

    public void setEmptyHint(String mEmptyHint) {
        this.mEmptyHint = mEmptyHint;
    }

    /**
     * 是否请求完成
     *
     * @return true 加载完成
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * 设置ContentView
     */
    public void setContentView(View... views) {
        mContentViews = views;
        //隐藏内容
        if (mContentViews != null && mContentViews.length > 0) {
            for (View v : mContentViews) {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(GONE);
                }
            }
        }
    }

    public void setContentParentView(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            View[] views = new View[viewGroup.getChildCount()];
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    views[i] = viewGroup.getChildAt(i);
                    views[i].setVisibility(GONE);
                }
            }
            mContentViews = views;
        }
    }


    /**
     * 自定义动画
     *
     * @param viewAnimProvider
     */
    public void setViewAnimOperation(ViewAnimProvider viewAnimProvider) {
        mViewAnimProvider = viewAnimProvider;
    }

    public MultiStateLayout(Context context) {
        this(context, null);
    }

    public MultiStateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMultiViewFactory = new MultiViewFactory(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadingView = mMultiViewFactory.createView(MultiViewFactory.LOADING);
        addView(mLoadingView);
        mCurrentView = mLoadingView;
    }

    /**
     * 是否启用动画
     *
     * @param animation 默认true
     */
    public void isAnimation(boolean animation) {
        isAnimation = animation;
    }

    @Override
    public void showLoadingLayout() {
        if (mLoadingView == null) {
            mLoadingView = mMultiViewFactory.createView(MultiViewFactory.LOADING);
            addView(mLoadingView);
        }
        if (mCurrentView == mLoadingView) {
            return;
        }
        switchView(mCurrentView, mLoadingView);
        mCurrentView = mLoadingView;
    }


    /**
     * 更新错误信息
     *
     * @param tv_error_info
     * @param info
     */
    private void updateErrorInfo(TextView tv_error_info, String info) {
        if (mOnRetryListener != null && info != null) {//如果实现了点击重试接口，在错误信息后面追加点击重试文字信息
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(info).append("点击重试!");
            info = stringBuilder.toString();
        }
        if (info != null) {
            tv_error_info.setText(info);
        }
    }

    @Override
    public void showErrorLayout(String info) {
        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
        if (mErrorView == null) {
            mErrorView = mMultiViewFactory.createView(MultiViewFactory.ERROR);
            tv_error_info = (TextView) mErrorView.findViewById(R.id.tv_error_info);
            updateErrorInfo(tv_error_info, info);
            mErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (mOnRetryListener != null) {
                        if (isFastDoubleClick()) {
                            return;
                        }
                        showLoadingLayout();
                        postDelayed(new Runnable() {//动画需要执行200ms
                            @Override
                            public void run() {
                                mOnRetryListener.onClick(v);
                            }
                        }, 300);
                    }
                }
            });
            addView(mErrorView);
        }
        updateErrorInfo(tv_error_info, info);
        if (mCurrentView == mErrorView) {
            return;
        }
        switchView(mCurrentView, mErrorView);
        mCurrentView = mErrorView;
    }

    @Override
    public void showEmptyLayout() {
        isComplete = true;
        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
        if (mEmptyView == null) {
            mEmptyView = mMultiViewFactory.createView(MultiViewFactory.EMPTY);
            tv_empty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
            addView(mEmptyView);
        }
        if (mEmptyHint != null && mEmptyHint.length() > 0) {
            tv_empty.setText(mEmptyHint);
        }
        if (mCurrentView == mEmptyView) {
            return;
        }
        switchView(mCurrentView, mEmptyView);
        mCurrentView = mEmptyView;
    }

    @Override
    public void showNoNetWorkLayout() {
        if (mNoNetWWork == null) {
            mNoNetWWork = mMultiViewFactory.createView(MultiViewFactory.NETWORK);
            addView(mNoNetWWork);
        }
        if (mCurrentView == mNoNetWWork) {
            return;
        }
        switchView(mCurrentView, mNoNetWWork);
        mCurrentView = mNoNetWWork;
    }


    @Override
    public void showLoginLayout() {
        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
        if (mLoginView == null) {
            mLoginView = mMultiViewFactory.createView(MultiViewFactory.LOGIN);
            Button btn_login = (Button) mLoginView.findViewById(R.id.btn_login);
            addView(mLoginView);
            btn_login.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "跳转到登录页面", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
//                    v.getContext().startActivity(intent);
                }
            });
        }
        if (mCurrentView == mLoginView) {
            return;
        }
        switchView(mCurrentView, mLoginView);
        mCurrentView = mLoginView;
    }


    /**
     * 设置未登录文字信息
     */
    public void setUnLoginInfo(String loginInfo) {
        this.mUnLoginInfo = loginInfo;
    }

    /**
     * 加载完成调用该方法
     */
    @Override
    public void onComplete() {
        isComplete = true;
        setVisibility(GONE);
        if (mContentViews != null && mContentViews.length > 0) {
//            for (int i = 0; i < mContentViews.length; i++) {
            for (View v : mContentViews) {
                if (v != null) {
                    v.setVisibility(View.VISIBLE);
                }
//                }
            }
        }
    }


    public void onAnimComplete() {
        setVisibility(GONE);
        isComplete = true;
//        AnimationSet animationSet = new AnimationSet(true);
        AnimatorSet animationSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        if (mContentViews != null && mContentViews.length > 0) {
            for (int i = 0; i < mContentViews.length; i++) {
                View v = mContentViews[i];
                if (v != null) {
                    v.setVisibility(VISIBLE);
                    animators.add(getTranslationAnim(v, i));
                }
            }
            animationSet.playTogether(animators);
            animationSet.start();
        }
    }

    public Animator getTranslationAnim(View v, int position) {
        ObjectAnimator objectAnimator = null;
        if (position == 0) {
            objectAnimator = ObjectAnimator.ofFloat(v, "translationY", 10, 0);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(v, "translationY", (position + 1) * 15, 0);
        }
        int duration = (position + 1) * 500;
        if (duration > 2500) {
            duration = 2500;
        }
        if (position == 0) {
            objectAnimator.setDuration(800);
        } else {
            objectAnimator.setDuration(duration);
        }
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        return objectAnimator;

    }

    private void switchView(View hideView, View showView) {
        if (mViewAnimProvider != null) {
            ViewAnimHelper.switchViewEffect(hideView, showView, isAnimation, mViewAnimProvider);
        } else {
            ViewAnimHelper.switchViewEffect(hideView, showView, isAnimation, new FadeScaleViewAnimProvider());
        }
    }

    public interface onRetryListener {
        void onClick(View v);
    }


    /**
     * 防止频繁点击的一个方法
     *
     * @return
     */
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
