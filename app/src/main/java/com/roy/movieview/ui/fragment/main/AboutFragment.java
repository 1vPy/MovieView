package com.roy.movieview.ui.fragment.main;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roy.movieview.BuildConfig;
import com.roy.movieview.Constants;
import com.roy.movieview.R;
import com.roy.movieview.bean.mian.appinfo.AppInfo;
import com.roy.movieview.presenter.impl.main.AboutPresenterImpl;
import com.roy.movieview.ui.activity.SplashActivity;
import com.roy.movieview.ui.contract.main.AboutContract;
import com.roy.movieview.ui.fragment.BaseDataFragment;
import com.roy.movieview.utils.common.NotificationHelper;
import com.roy.movieview.utils.download.XUtilsDownload;
import com.roy.movieview.utils.tip.ToastUtils;

import java.io.File;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/19.
 */

public class AboutFragment extends BaseDataFragment<AboutContract.View, AboutPresenterImpl> implements AboutContract.View
        , View.OnClickListener
        , XUtilsDownload.OnDownloadingListener {
    @BindView(R.id.tv_version_current)
    TextView tv_version_current;

    @BindView(R.id.ll_version_layout)
    LinearLayout ll_version_layout;

    @BindView(R.id.iv_version_tip)
    ImageView iv_version_tip;

    @BindView(R.id.tv_version_check)
    TextView tv_version_check;

    @BindView(R.id.pb_checking)
    ProgressBar pb_checking;

    @BindView(R.id.ll_introduction)
    LinearLayout ll_introduction;

    @BindView(R.id.ll_author_layout)
    LinearLayout ll_author_layout;

    private Notification mNotification;

    private int mCurrentVersionCode;
    private String mCurrentVersionName;

    private boolean mCanUpdate = false;
    private AppInfo mAppInfo;
    private File mFile;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }

    @Override
    protected void initViewAndEvent() {
        mPresenterImpl.getNowAppInfo();
        ll_version_layout.setOnClickListener(this);
        ll_introduction.setOnClickListener(this);
        ll_author_layout.setOnClickListener(this);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void needUpdate(AppInfo appInfo) {
        mAppInfo = appInfo;
        if (mCurrentVersionCode < appInfo.getResults().get(0).getVersionCode()) {
            iv_version_tip.setVisibility(View.VISIBLE);
            tv_version_check.setText("检测到新版本：" + appInfo.getResults().get(0).getVersionName());
            mCanUpdate = true;
        } else {
            iv_version_tip.setVisibility(View.INVISIBLE);
            tv_version_check.setText("已经是最新版本");
            mCanUpdate = false;
        }
        tv_version_check.setVisibility(View.VISIBLE);
        pb_checking.setVisibility(View.INVISIBLE);
    }

    @Override
    public void md5(boolean isCheck) {
        if (isCheck) {
            ToastUtils.showSingleLongToast("MD5验证通过");
            Intent intent = new Intent();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", mFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriForFile, mContext.getContentResolver().getType(uriForFile));
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(mFile),
                        "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);


        } else {
            ToastUtils.showSingleLongToast("MD5验证错误");
            if (mFile.exists())
                mFile.delete();
            XUtilsDownload.startTask(mAppInfo.getResults().get(0).getApkFile().getUrl(), AboutFragment.this);
        }
    }

    @Override
    public void nowAppInfo(int versionCode, String versionName) {
        mCurrentVersionCode = versionCode;
        mCurrentVersionName = versionName;
        tv_version_check.setVisibility(View.INVISIBLE);
        pb_checking.setVisibility(View.VISIBLE);
        mPresenterImpl.checkUpdate();
        tv_version_current.setText("V " + versionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_version_layout:
                if (mCanUpdate) {
                    showUpdateDialog();
                } else {
                    tv_version_check.setVisibility(View.INVISIBLE);
                    pb_checking.setVisibility(View.VISIBLE);
                    mPresenterImpl.checkUpdate();
                }
                break;
            case R.id.ll_introduction:
                SplashActivity.start(mContext);
                break;
            case R.id.ll_author_layout:
                showAuthorInfoDialog();
                break;
        }
    }

    private void showUpdateDialog() {
        new AlertDialog.Builder(getActivity())
                .setPositiveButton("立即升级", (dialog, which) -> {
                    ToastUtils.showSingleLongToast("开始下载升级包");
                    startDownload();
                    dialog.dismiss();
                })
                .setNegativeButton("取消升级", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setTitle("提示")
                .setMessage("检测到新版本，是否立即升级？")
                .show();
    }

    private void showAuthorInfoDialog(){
        new AlertDialog.Builder(getActivity())
                .setNegativeButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setTitle("作者信息")
                .setMessage("1.作者：Roy_1vPy\n\n" +
                        "2.擅长方向：\n" +
                        "  (1). Android应用层开发\n" +
                        "  (2).掌握RxJava、Retrofit、OkHttp等开源框架使用\n" +
                        "  (3).Java后台简单开发：SpringMVC、MyBatis、Struts2、Hibernate等\n\n" +
                        "3.联系方式：846913426@qq.com\n\n" +
                        "4.项目地址:https://github.com/1vPy/MovieView")
                .show();
    }

    private void startDownload() {
        mFile = new File(Constants.apk_save_path);
        if (mFile.exists()) {
            mPresenterImpl.md5Check(mFile, mAppInfo.getResults().get(0).getFileMD5());
        } else {
            XUtilsDownload.startTask(mAppInfo.getResults().get(0).getApkFile().getUrl(), AboutFragment.this);
        }
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {
        mNotification = NotificationHelper.createProgressNotification(mContext);
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        ToastUtils.showSingleLongToast("当前进度：" + (int) (current * 100 / total));
    }

    @Override
    public void onSuccess(File apkFile) {
        mPresenterImpl.md5Check(mFile, mAppInfo.getResults().get(0).getFileMD5());
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
}
