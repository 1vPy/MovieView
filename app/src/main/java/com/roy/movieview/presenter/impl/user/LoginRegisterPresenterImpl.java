package com.roy.movieview.presenter.impl.user;

import android.content.Context;
import android.text.TextUtils;

import com.roy.movieview.BuildConfig;
import com.roy.movieview.Constants;
import com.roy.movieview.bean.user.ErrorBean;
import com.roy.movieview.bean.user.UploadImg;
import com.roy.movieview.bean.user.UserBean;
import com.roy.movieview.bean.user.UserHeader;
import com.roy.movieview.bean.user.UserInfo;
import com.roy.movieview.bean.user.config.DeviceConfig;
import com.roy.movieview.bean.user.config.LoginConfig;
import com.roy.movieview.http.bmob.BmobApi;
import com.roy.movieview.presenter.BasePresenterImpl;
import com.roy.movieview.ui.contract.user.LoginRegisterContract;
import com.roy.movieview.utils.common.JustUtils;
import com.roy.movieview.utils.common.PushUtils;
import com.roy.movieview.utils.json.JsonUtils;
import com.roy.movieview.utils.tip.ToastUtils;
import com.roy.movieview.utils.user.UserPreference;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import cn.bmob.v3.BmobInstallation;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by 1vPy(Roy) on 2017/6/22.
 */

public class LoginRegisterPresenterImpl extends BasePresenterImpl<LoginRegisterContract.View> implements LoginRegisterContract.Presenter {

    private Context mContext;

    private BmobApi mBmobApi;

    private UserPreference mUserPreference;

    private UserBean mUserBean;


    @Inject
    public LoginRegisterPresenterImpl(Context context, BmobApi bmobApi, UserPreference userPreference) {
        mContext = context;
        mBmobApi = bmobApi;
        mUserPreference = userPreference;
    }


    @Override
    public boolean isLogin() {
        return mUserPreference.readUserInfo() != null;
    }

    @Override
    public void login(String username, String password) {
        addSubscribe(mBmobApi.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        mUserBean = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UserBean.class);
                        mUserPreference.saveUserInfo(new UserInfo(mUserBean.getObjectId(), username, password, mUserBean.getMobilePhoneNumber(), mUserBean.getEmail()));
                        mUserPreference.saveSessionToken(mUserBean.getSessionToken());
                        mUserPreference.saveAvatar(mUserBean.getUserHeader().getUrl());
                        if (isAttached())
                            getView().loginSuccess(mUserBean);
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
                })
                .doOnError(throwable -> {
                    if (isAttached())
                        getView().showError(throwable.getLocalizedMessage());
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        UserBean userBean = new UserBean();
                        userBean.setUsername(username);
                        String json = JsonUtils.JavaBean2Json(userBean);
                        return mBmobApi.Config(json);
                    }
                })
                .flatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        if (responseBodyResponse.isSuccessful()) {
                            DeviceConfig dc = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), DeviceConfig.class);
                            UserBean userBean = new UserBean();
                            userBean.setInstallationId(BmobInstallation.getInstallationId(mContext));
                            if (dc.getResults().size() > 0) {
                                if (!TextUtils.equals(dc.getResults().get(0).getInstallationId(), BmobInstallation.getInstallationId(mContext))) {
                                    PushUtils.pushLoginMessage(mContext, dc.getResults().get(0).getInstallationId());
                                }

                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                                UserBean userBean1 = new UserBean();
                                userBean1.setUsername(username);
                                return mBmobApi.updateInstallationId(JsonUtils.JavaBean2Json(userBean1), requestBody);
                            } else {
                                userBean.setUsername(username);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                                return mBmobApi.uploadInstallationId(requestBody);
                            }
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                }, throwable -> {
                })
        );
    }

    @Override
    public void register(String username, String password, String phoneNum, File userAvatar) {
        byte[] bytes = new byte[1024];
        if (!userAvatar.exists()) {
            try {
                bytes = JustUtils.inputStream2byte(mContext.getAssets().open("avatar_default.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bytes = JustUtils.file2byte(userAvatar.getAbsolutePath());
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), bytes);
        addSubscribe(mBmobApi.uploadPic(userAvatar.getName(), requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful())
                        userAvatar.delete();
                })
                .doOnError(throwable -> {
                    if (isAttached())
                        getView().showError("avatar set error");
                })
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        UserBean userBean = new UserBean();
                        if (responseBodyResponse.isSuccessful()) {
                            UploadImg uploadImg = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UploadImg.class);
                            UserHeader userHeader = new UserHeader();
                            userHeader.set_Type("File");
                            userHeader.setCdn(uploadImg.getCdn());
                            userHeader.setFilename(uploadImg.getFilename());
                            userHeader.setUrl(uploadImg.getUrl());
                            userBean.setUserHeader(userHeader);
                        }
                        userBean.setUsername(username);
                        userBean.setPassword(password);
                        userBean.setMobilePhoneNumber(phoneNum);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                        return mBmobApi.register(requestBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        if (isAttached()) {
                            UserBean userBean = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UserBean.class);
                            getView().registerSuccess(userBean);
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
                    if (isAttached())
                        getView().showError(throwable.getLocalizedMessage());
                })
        );
    }
}
