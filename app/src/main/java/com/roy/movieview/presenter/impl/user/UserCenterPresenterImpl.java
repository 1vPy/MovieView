package com.roy.movieview.presenter.impl.user;

import com.roy.movieview.bean.user.UploadImg;
import com.roy.movieview.bean.user.UserBean;
import com.roy.movieview.bean.user.UserHeader;
import com.roy.movieview.bean.user.UserInfo;
import com.roy.movieview.http.bmob.BmobApi;
import com.roy.movieview.presenter.BasePresenterImpl;
import com.roy.movieview.ui.contract.user.UserCenterContract;
import com.roy.movieview.utils.common.JustUtils;
import com.roy.movieview.utils.json.JsonUtils;
import com.roy.movieview.utils.tip.ToastUtils;
import com.roy.movieview.utils.user.UserPreference;

import java.io.File;

import javax.inject.Inject;

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
 * Created by Administrator on 2017/6/28.
 */

public class UserCenterPresenterImpl extends BasePresenterImpl<UserCenterContract.View> implements UserCenterContract.Presenter {

    private UserPreference mUserPreference;

    private BmobApi mBmobApi;

    @Inject
    public UserCenterPresenterImpl(BmobApi bmobApi, UserPreference userPreference) {
        mBmobApi = bmobApi;
        mUserPreference = userPreference;
    }

    @Override
    public String getUserAvatar() {
        return mUserPreference.readAvatar();
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserPreference.readUserInfo();
    }

    @Override
    public void logout() {
        mUserPreference.clearUserInfo();
    }

    @Override
    public void modifyAvatar(File userAvatar) {
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), JustUtils.file2byte(userAvatar.getAbsolutePath()));
        String filename = userAvatar.getName();
        addSubscribe(mBmobApi.uploadPic(filename, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful())
                        ToastUtils.showToast("上传成功");

                })
                .doOnError(throwable -> {
                    if (isAttached())
                        getView().showError("avatar set error");
                })
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        UserBean userBean = null;
                        if (responseBodyResponse.isSuccessful()) {
                            userBean = new UserBean();
                            UploadImg uploadImg = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UploadImg.class);
                            UserHeader userHeader = new UserHeader();
                            userHeader.set_Type("File");
                            userHeader.setCdn(uploadImg.getCdn());
                            userHeader.setFilename(uploadImg.getFilename());
                            userHeader.setUrl(uploadImg.getUrl());
                            userBean.setUserHeader(userHeader);
                            mUserPreference.saveAvatar(userBean.getUserHeader().getUrl());
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                        return mBmobApi.updateUser(mUserPreference.readSessionToken(), mUserPreference.readUserInfo().getObjectId(), requestBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                            if (responseBodyResponse.isSuccessful())
                                if (isAttached())
                                    getView().modifySuccess();
                        },
                        throwable -> {
                            if (isAttached()) {
                                getView().showError(throwable.getLocalizedMessage());
                            }
                        })
        );

    }
}
