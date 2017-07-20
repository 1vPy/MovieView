package com.roy.movieview.ui.contract.main;

import com.roy.movieview.bean.mian.appinfo.AppInfo;
import com.roy.movieview.ui.contract.BaseContract;

import java.io.File;

/**
 * Created by Administrator on 2017/7/19.
 */

public interface AboutContract {
    interface View extends BaseContract.View{
        void needUpdate(AppInfo appInfo);

        void md5(boolean isCheck);

        void nowAppInfo(int versionCode,String versionName);
    }

    interface Presenter{
        void checkUpdate();

        void md5Check(File apkFile,String correctMD5);

        void getNowAppInfo();
    }

}
