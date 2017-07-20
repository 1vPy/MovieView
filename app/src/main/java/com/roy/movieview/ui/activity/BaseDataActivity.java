package com.roy.movieview.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.roy.movieview.presenter.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public abstract class BaseDataActivity<V, T extends BasePresenterImpl<V>> extends BaseActivity {

    @Inject
    protected T mPresenterImpl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenterImpl.attachView((V) this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterImpl.detachView();
    }



}
