package com.roy.movieview.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.roy.movieview.MvApplication;
import com.roy.movieview.R;
import com.roy.movieview.di.component.ActivityComponent;
import com.roy.movieview.di.component.DaggerActivityComponent;
import com.roy.movieview.di.module.ActivityModule;
import com.roy.movieview.ui.activity.movie.MovieDetailActivity;
import com.roy.movieview.utils.user.UserPreference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import solid.ren.skinlibrary.base.SkinBaseActivity;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public abstract class BaseActivity extends SkinBaseActivity {

    @BindView(R.id.common_toolbar)
    protected Toolbar mToolbar;

    @Inject
    protected Activity mActivity;

    protected Context mContext;

    @Inject
    protected Context mApplicationContext;

    private TextView retry;

    private RelativeLayout viewError;
    private LinearLayout viewLoading;
    private View viewRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        mContext = mActivity.getBaseContext();
        if (canSwipeBack()) {
            SwipeBackHelper.onCreate(this);
            SwipeBackHelper.getCurrentPage(this)//get current instance
                    .setSwipeBackEnable(true)
                    .setSwipeEdgePercent(0.2f)//0.2 mean left 20% of screen can touch to begin swipe.
                    .setSwipeSensitivity(1)//sensitiveness of the gesture。0:slow  1:sensitive
                    .setScrimColor(Color.TRANSPARENT)//color of Scrim below the activity
                    .setClosePercent(0.8f)//close activity when swipe over this
                    .setSwipeRelateEnable(true)//if should move together with the following Activity
                    .setSwipeRelateOffset(500)//the Offset of following Activity when setSwipeRelateEnable(true)
                    .setDisallowInterceptTouchEvent(false);
        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        viewRoot = rootView();
        if (viewRoot != null) {
            ViewGroup parent = (ViewGroup) viewRoot.getParent();
            View.inflate(this, R.layout.view_error, parent);
            View.inflate(this, R.layout.view_loading, parent);
            viewError = (RelativeLayout) parent.findViewById(R.id.view_error);
            viewLoading = (LinearLayout) parent.findViewById(R.id.view_loading);
            retry = (TextView) viewError.findViewById(R.id.retry);
            retry.setOnClickListener(v -> retryClicked());
        }

        initToolBar();
        initViewAndEvent();

    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(MvApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (canSwipeBack()) {
            SwipeBackHelper.onPostCreate(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (canSwipeBack()) {
            SwipeBackHelper.onDestroy(this);
        }
    }


    protected void showLoading() {
        viewError.setVisibility(View.GONE);
        viewRoot.setVisibility(View.GONE);
        viewLoading.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        viewError.setVisibility(View.GONE);
        //startAnim();
        viewRoot.setVisibility(View.VISIBLE);
        viewLoading.setVisibility(View.GONE);
    }

    protected void showError() {
        viewError.setVisibility(View.VISIBLE);
        viewRoot.setVisibility(View.GONE);
        viewLoading.setVisibility(View.GONE);
    }


   /* private void startAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(viewLoading, "qwe", 1f, 0f);
        animator.setDuration(1000);
        animator.start();
        viewRoot.setVisibility(View.VISIBLE);
        animator.addUpdateListener(animation -> {
            float a = (Float) animation.getAnimatedValue();
            viewLoading.setAlpha(a);
            viewRoot.setAlpha(1f - a);
        });
        viewLoading.setVisibility(View.GONE);
    }*/

    protected abstract View rootView();

    protected abstract void retryClicked();

    protected abstract boolean canSwipeBack();

    protected abstract void initInject();

    protected abstract void initViewAndEvent();

}
