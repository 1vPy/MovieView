package com.roy.movieview.presenter.impl.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.roy.movieview.bean.mian.appinfo.AppInfo;
import com.roy.movieview.bean.user.ErrorBean;
import com.roy.movieview.http.bmob.BmobApi;
import com.roy.movieview.presenter.BasePresenterImpl;
import com.roy.movieview.ui.contract.main.AboutContract;
import com.roy.movieview.utils.common.VerificationMD5;
import com.roy.movieview.utils.json.JsonUtils;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/19.
 */

public class AboutPresenterImpl extends BasePresenterImpl<AboutContract.View> implements AboutContract.Presenter {

    private BmobApi mBmobApi;
    private Context mContext;

    @Inject
    public AboutPresenterImpl(Context context, BmobApi bmobApi) {
        mBmobApi = bmobApi;
        mContext = context;
    }

    @Override
    public void checkUpdate() {
        mBmobApi.checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        if (isAttached()) {
                            getView().needUpdate(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), AppInfo.class));
                        }
                    } else {
                        String error = "";
                        switch (responseBodyResponse.code()) {
                            case 404:
                                error = JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError();
                                break;
                            default:
                                error = "未知错误";
                                break;
                        }
                        if (isAttached())
                            getView().showError(error);
                    }
                }, throwable -> {
                    if (isAttached()) {
                        getView().showError(throwable.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void md5Check(File apkFile,String correctMD5) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            if(TextUtils.equals(VerificationMD5.getFileMD5(apkFile),correctMD5)){
                e.onNext(true);
                e.onComplete();
            }
            e.onNext(false);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if(isAttached())
                        getView().md5(aBoolean);
                },throwable -> {
                    if(isAttached())
                        getView().showError(throwable.getLocalizedMessage());
                });
    }

    @Override
    public void getNowAppInfo() {
        if (isAttached()) {
            getView().nowAppInfo(getVersionCode(), getVersionName());
        }
    }

    private int getVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionCode;
    }

    private String getVersionName() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }
}
