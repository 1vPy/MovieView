package com.roy.movieview.ui.custom;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.roy.movieview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 1vPy(Roy) on 2017/6/21.
 */

public class CycleView extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private View view;
    private ViewPager vp_img_cycle;
    private LinearLayout llHottestIndicator;
    private CycleAdapter mCycleAdapter;
    private ImageView[] mBottomImages;//底部只是当前页面的小圆点
    private int autoCurrIndex = 0;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private OnCycleItemClickListener mOnCycleItemClickListener;

    private List<String> mUrlList = new ArrayList<>();

    public CycleView(Context context) {
        super(context);
        init(context);
    }

    public CycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.view_cycle, this, true);
        vp_img_cycle = (ViewPager) view.findViewById(R.id.vp_img_cycle);
        llHottestIndicator = (LinearLayout) view.findViewById(R.id.llHottestIndicator);
        vp_img_cycle.addOnPageChangeListener(this);
    }

    public void setImageList(List<String> urlList) {
        this.mUrlList = urlList;
        if (mUrlList == null && mUrlList.size() <= 0) {
            throw new NullPointerException("imgList is null or imgList size <= 0");
        }
        initIndicator(mUrlList.size());
        mCycleAdapter = new CycleAdapter(mContext, mUrlList);
        vp_img_cycle.setAdapter(mCycleAdapter);
    }

    public void setOnCycleItemClickListener(OnCycleItemClickListener onCycleItemClickListener) {
        this.mOnCycleItemClickListener = onCycleItemClickListener;
    }

    private void initIndicator(int size) {
        //创建底部指示位置的导航栏
        mBottomImages = new ImageView[size];

        for (int i = 0; i < mBottomImages.length; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.indicator_select);
            } else {
                imageView.setBackgroundResource(R.drawable.indicator_unselect);
            }

            mBottomImages[i] = imageView;
            //把指示作用的原点图片加入底部的视图中
            llHottestIndicator.addView(mBottomImages[i]);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int total = mBottomImages.length;
        for (int j = 0; j < total; j++) {
            if (j == position) {
                mBottomImages[j].setBackgroundResource(R.drawable.indicator_select);
            } else {
                mBottomImages[j].setBackgroundResource(R.drawable.indicator_unselect);
            }
        }

        //设置全局变量，currentIndex为选中图标的 index
        autoCurrIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                resetTimer();
                break;
        }
    }

    public class CycleAdapter extends PagerAdapter {
        private static final String LOG = "NEWS_LOG";

        private List<String> mUrlList = new ArrayList<>();
        private List<ImageView> mImageViewList = new ArrayList<>();


        public CycleAdapter(Context context, List<String> urlList) {
            if (urlList == null || urlList.size() == 0) {
                this.mUrlList = new ArrayList<>();
            } else {
                this.mUrlList = urlList;
            }

            for (int i = 0; i < mUrlList.size(); i++) {
                ImageView image = new ImageView(context);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(mUrlList.get(i)).into(image);
                mImageViewList.add(image);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(mImageViewList.get(position));
            mImageViewList.get(position).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCycleItemClickListener != null) {
                        mOnCycleItemClickListener.onItemClick(v, position);
                    }
                }
            });
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViewList.get(position));
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            Log.i(LOG, "in isViewFromObject view: " + view + " object: "
                    + object + " equal: " + (view == (View) object));
            return view == (View) object;
        }
    }


    public void start() {
        // 设置自动轮播图片，5s后执行，周期是5s
        mTimerTask = new CycleTimerTask();
        mTimer.schedule(mTimerTask, 5000, 5000);
    }

    //定时轮播图片，需要在主线程里面修改 UI
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.arg1 != 0) {
                        vp_img_cycle.setCurrentItem(msg.arg1);
                    } else {
                        //false 当从末页调到首页是，不显示翻页动画效果，
                        vp_img_cycle.setCurrentItem(msg.arg1, false);
                    }
                    break;
            }
        }
    };

    public void resetTimer() {
        if (mTimer != null) {
            if (mTimerTask != null) {
                mTimerTask.cancel(); // 将原任务从队列中移除
                mTimerTask = null;
            }
        }
        mTimerTask = new CycleTimerTask();
        mTimer.schedule(mTimerTask, 5000, 5000);
    }

    class CycleTimerTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 0;
            if (autoCurrIndex == mUrlList.size() - 1) {
                autoCurrIndex = -1;
            }
            message.arg1 = autoCurrIndex + 1;
            mHandler.sendMessage(message);
        }
    }

    public interface OnCycleItemClickListener {
        void onItemClick(View view, int position);
    }


}
