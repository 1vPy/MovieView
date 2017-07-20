package com.roy.movieview.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.roy.movieview.R;
import com.roy.movieview.utils.image.ImageUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SplashAdapter extends PagerAdapter {
    private Context mContext;
    private int mCount;
    private int[] mSplashImg;
    private String[] mSplashText;
    private OnStartBtnClickListener mOnStartBtnClickListener;

    public SplashAdapter(Context context, int count,String[] splashText, int[] splashImg, OnStartBtnClickListener onStartBtnClickListener) {
        mContext = context;
        mCount = count;
        mSplashText = splashText;
        mSplashImg = splashImg;
        mOnStartBtnClickListener = onStartBtnClickListener;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position != mCount - 1) {
            /*ImageView view = new ImageView(mContext);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageUtils.displayImage(mContext, mSplashImg[position], view);*/
            TextView view = new TextView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
            view.setGravity(Gravity.CENTER);
            view.setText(mSplashText[position]);
            container.addView(view);
            return view;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_guide_splash, null);
            Button btn = (Button) view.findViewById(R.id.btn_start);
            btn.setOnClickListener(v -> mOnStartBtnClickListener.onClick());
            container.addView(view);
            return view;
        }
    }

    public interface OnStartBtnClickListener {
        void onClick();
    }
}
