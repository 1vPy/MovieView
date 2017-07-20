package com.roy.movieview;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.roy.movieview.di.component.ApplicationComponent;
import com.roy.movieview.di.component.DaggerApplicationComponent;
import com.roy.movieview.di.module.ApplicationModule;
import com.roy.movieview.di.module.HttpModule;
import com.roy.movieview.ui.custom.CollapsingToolbarLayoutAttr;
import com.roy.movieview.utils.common.LogUtils;
import com.squareup.leakcanary.LeakCanary;

import org.xutils.x;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.smssdk.SMSSDK;
import solid.ren.skinlibrary.SkinConfig;
import solid.ren.skinlibrary.base.SkinBaseApplication;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public class MvApplication extends SkinBaseApplication {
    private static final String TAG = MvApplication.class.getSimpleName();
    private static ApplicationComponent appComponent;
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MvKit.setContext(getApplicationContext());
        initSmsSDK();
        initPush();
        initCrashHandler();
        initXUtils();
        //initLeakCanary();
        SkinConfig.addSupportAttr("collapsingToolbarLayout", new CollapsingToolbarLayoutAttr());
        SkinConfig.enableGlobalSkinApply();


    }

    private void initXUtils(){
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initSmsSDK() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String key = appInfo.metaData.getString("sms_app_key");
        String secret = appInfo.metaData.getString("sms_app_secret");
        LogUtils.log(TAG, "key = " + key + ",secret = " + secret, LogUtils.DEBUG);
        SMSSDK.initSDK(getApplicationContext(), key, secret);
    }

    private void initPush() {
        Bmob.initialize(this, Constants.application_id);
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this);
    }

    private void initCrashHandler() {
        CrashHandler.getInstance().init(getApplicationContext());
    }

    public static ApplicationComponent getApplicationComponent() {
        if (appComponent == null) {
            appComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }


}
