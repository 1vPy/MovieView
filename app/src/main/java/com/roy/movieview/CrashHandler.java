package com.roy.movieview;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;


import com.roy.movieview.bean.user.ErrorBean;
import com.roy.movieview.utils.common.JustUtils;
import com.roy.movieview.utils.common.LogUtils;
import com.roy.movieview.utils.json.JsonUtils;
import com.roy.movieview.utils.tip.ToastUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment
            .getExternalStorageDirectory().getPath() + "/" + MvKit.getContext().getString(R.string.app_name) + "/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sInstance = new CrashHandler();
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            // 导出异常信息到SD卡中
            uploadExceptionToServer(dumpExceptionToSDCard(ex).getPath());
            // 这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            Looper.prepare();
            ToastUtils.showLongToast("哎呀，程序发生异常啦...,正在上传异常文件");
            Looper.loop();
        }).start();
        ex.printStackTrace();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {

            Process.killProcess(Process.myPid());
        }

    }

    private File dumpExceptionToSDCard(Throwable ex) throws IOException {
        // 如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return null;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date(current));
        String preciseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(current));
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);


        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
                    file, true)));
            pw.println(preciseTime);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed:");
            e.printStackTrace();
        }
        return file;
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer(String path) {
        // TODO Upload Exception Message To Your Web Server
        LogUtils.log(TAG, path, LogUtils.DEBUG);
        File f = new File(path);
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), JustUtils.txt2String(f));
        MvApplication.getApplicationComponent().provideBmobApi()
                .uploadCrash("Crash.txt", file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    LogUtils.log(TAG, "上传", LogUtils.DEBUG);
                    if (responseBodyResponse.isSuccessful()) {
                        ToastUtils.showSingleToast("上传成功");
                    } else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                ToastUtils.showSingleToast("上传失败：" + JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                ToastUtils.showSingleToast("上传失败：未知错误");
                                break;
                        }
                    }
                }, throwable -> {
                    ToastUtils.showSingleToast("上传失败：" + throwable.getLocalizedMessage());
                });
    }

}
