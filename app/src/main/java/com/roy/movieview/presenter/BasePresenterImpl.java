package com.roy.movieview.presenter;

import com.roy.movieview.ui.contract.BaseContract;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public class BasePresenterImpl<T> implements BaseContract.Presenter<T> {

    protected Reference<T> mViewRef;

    protected CompositeDisposable mCompositeDisposable;

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    @Override
    public boolean isAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public T getView() {
        return mViewRef.get();
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        unSubscribe();
    }
}
