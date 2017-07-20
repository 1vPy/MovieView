package com.roy.movieview.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roy.movieview.R;
import com.roy.movieview.ui.adapter.SplashAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SplashActivity extends BaseActivity implements SplashAdapter.OnStartBtnClickListener {
    @BindView(R.id.vp_splash)
    ViewPager vpSplash;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;

    private int[] mSplashImg = {R.drawable.splash_1, R.drawable.splash_2};
    private String[] mSplashText = {"这是一款影讯查看App", "同时还可以评论和点赞"};

    private SplashAdapter mSplashAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected View rootView() {
        return null;
    }

    @Override
    protected void retryClicked() {

    }

    @Override
    protected boolean canSwipeBack() {
        return false;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setVisibility(View.INVISIBLE);
        mSplashAdapter = new SplashAdapter(this, 3, mSplashText, mSplashImg, this);
        vpSplash.setAdapter(mSplashAdapter);
        initPageListener(llIndicator, mSplashAdapter.getCount(), vpSplash);
    }

    // 初始化更多布局PageListener
    private static void initPageListener(final ViewGroup indicator, final int count, final ViewPager viewPager) {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setIndicator(indicator, count, position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setIndicator(indicator, count, 0);
    }


    /**
     * 设置页码
     */
    private static void setIndicator(ViewGroup indicator, int total, int current) {
        if (total <= 1) {
            indicator.removeAllViews();
        } else {
            indicator.removeAllViews();
            for (int i = 0; i < total; i++) {
                ImageView imgCur = new ImageView(indicator.getContext());
                imgCur.setId(i);
                // 判断当前页码来更新
                if (i == current) {
                    imgCur.setBackgroundResource(R.drawable.indicator_select);
                } else {
                    imgCur.setBackgroundResource(R.drawable.indicator_unselect);
                }

                indicator.addView(imgCur);
            }
        }
    }

    @Override
    public void onClick() {
        this.finish();
    }
}
