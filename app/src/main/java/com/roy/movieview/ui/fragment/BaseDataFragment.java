package com.roy.movieview.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.roy.movieview.MvApplication;
import com.roy.movieview.di.component.DaggerFragmentComponent;
import com.roy.movieview.di.component.FragmentComponent;
import com.roy.movieview.di.module.FragmentModule;
import com.roy.movieview.presenter.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public abstract class BaseDataFragment<V, T extends BasePresenterImpl<V>>  extends BaseFragment{
    @Inject
    protected T mPresenterImpl;

    @Inject
    protected Activity mActivity;

    protected Context mContext;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        mPresenterImpl.attachView((V) this);//加入到弱引用中
        mContext = mActivity.getBaseContext();
        super.onViewCreated(view, savedInstanceState);

    }

    //Dagger
    protected FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .applicationComponent(MvApplication.getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterImpl.detachView();//从弱引用中移除
    }

    //Fragment注入
    protected abstract void initInject();
}
