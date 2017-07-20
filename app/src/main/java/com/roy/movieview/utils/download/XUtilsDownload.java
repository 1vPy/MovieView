package com.roy.movieview.utils.download;

import com.roy.movieview.Constants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/7/19.
 */

public class XUtilsDownload {

    public static void startTask(String url, OnDownloadingListener onDownloadingListener) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(Constants.apk_save_path);
        requestParams.setCancelFast(true);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                onDownloadingListener.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onDownloadingListener.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {
                onDownloadingListener.onWaiting();
            }

            @Override
            public void onStarted() {
                onDownloadingListener.onStarted();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                onDownloadingListener.onLoading(total, current, isDownloading);
            }
        });
    }

    public interface OnDownloadingListener {
        void onWaiting();

        void onStarted();

        void onLoading(long total, long current, boolean isDownloading);

        void onSuccess(File apkFile);

        void onError(Throwable ex, boolean isOnCallback);
    }
}
