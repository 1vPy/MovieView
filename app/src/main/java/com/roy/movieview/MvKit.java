package com.roy.movieview;

import android.content.Context;

import com.roy.movieview.ui.listener.OnUserStateChangeListener;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public class MvKit {
    private static Context mContext;

    private static OnUserStateChangeListener mOnUserStateChangeListener;


    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }


    public static void setOnUserStateChangeListener(OnUserStateChangeListener onUserStateChangeListener) {
        mOnUserStateChangeListener = onUserStateChangeListener;
    }

    public static OnUserStateChangeListener getOnUserStateChangeListener() {
        return mOnUserStateChangeListener;
    }

}
