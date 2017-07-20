package com.roy.movieview.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.roy.movieview.Constants;
import com.roy.movieview.MvKit;
import com.roy.movieview.R;
import com.roy.movieview.presenter.impl.user.UserCenterPresenterImpl;
import com.roy.movieview.ui.activity.BaseDataActivity;
import com.roy.movieview.ui.activity.helper.ImagePickActivity;
import com.roy.movieview.ui.contract.user.UserCenterContract;
import com.roy.movieview.ui.listener.OnUserStateChangeListener;
import com.roy.movieview.utils.image.ImageUtils;
import com.roy.movieview.utils.tip.ToastUtils;

import java.io.File;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserCenterActivity extends BaseDataActivity<UserCenterContract.View, UserCenterPresenterImpl>
        implements UserCenterContract.View
        , View.OnClickListener
        , CompoundButton.OnCheckedChangeListener {
    protected static final int REQUEST_CODE_PICKIMAGE = 1000;

    protected static final int FROM_ALBUM = 0;
    protected static final int FROM_CAMERA = 1;


    @BindView(R.id.btn_logout)
    AppCompatButton btn_logout;
    @BindView(R.id.avatar_modify)
    LinearLayout avatar_modify;
    @BindView(R.id.user_avatar)
    ImageView user_avatar;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_telephone)
    TextView user_telephone;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.sbtn_msg)
    SwitchButton sbtn_msg;
    @BindView(R.id.sbtn_sync)
    SwitchButton sbtn_sync;
    @BindView(R.id.disk_cache)
    TextView disk_cache;
    @BindView(R.id.cache_clear)
    LinearLayout cache_clear;
    @BindView(R.id.cloud_collection_num)
    TextView cloud_collection_num;

    private File mCropFile = new File(Constants.image_save_path, "Crop.jpg");//裁剪后的File对象


    private ProgressDialog dialog;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
    }

    @Override
    protected View rootView() {
        return null;
    }

    @Override
    protected void retryClicked() {

    }

    @Override
    protected boolean canSwipeBack() {
        return true;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setTitle(R.string.user_center);
        mToolbar.setNavigationOnClickListener(v -> UserCenterActivity.this.finish());

        ImageUtils.displayImage(this, mPresenterImpl.getUserAvatar(), user_avatar);

        user_name.setText(mPresenterImpl.getUserInfo().getUsername());
        user_telephone.setText(mPresenterImpl.getUserInfo().getTelephone());

        if (mPresenterImpl.getUserInfo().getEmail().isEmpty()) {
            user_email.setText(getString(R.string.not_set));
        } else {
            user_email.setText(mPresenterImpl.getUserInfo().getEmail());
        }

        disk_cache.setText(ImageUtils.getCacheSize());

        dialog = new ProgressDialog(this);

        btn_logout.setOnClickListener(this);
        avatar_modify.setOnClickListener(this);
        sbtn_msg.setOnCheckedChangeListener(this);
        sbtn_sync.setOnCheckedChangeListener(this);
        cache_clear.setOnClickListener(this);

    }

    @Override
    public void showError(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                mPresenterImpl.logout();
                MvKit.getOnUserStateChangeListener().stateChanged(OnUserStateChangeListener.UserState.Logout);
                this.finish();
                break;
            case R.id.avatar_modify:
                showDialog();
                break;
            case R.id.cache_clear:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.cache_clear)
                        .setNegativeButton(R.string.confirm, (dialog1, which) -> ImageUtils.clearDiskCache(() -> disk_cache.setText(ImageUtils.getCacheSize())))
                        .setPositiveButton(R.string.cancel, (dialog12, which) -> dialog12.dismiss())
                        .show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sbtn_msg:
                if (isChecked) {
                    ToastUtils.showSingleToast(R.string.msg_push_open);
                } else {
                    ToastUtils.showSingleToast(R.string.msg_push_close);
                }
                break;
            case R.id.sbtn_sync:
                if (isChecked) {
                    ToastUtils.showSingleToast(R.string.collection_sync_open);
                } else {
                    ToastUtils.showSingleToast(R.string.collection_sync_close);
                }
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, (dialog1, which) -> {
            switch (which) {
                case FROM_ALBUM: // 选择本地照片
                    ImagePickActivity.start(this, REQUEST_CODE_PICKIMAGE, FROM_ALBUM);
                    break;
                case FROM_CAMERA: // 拍照
                    ImagePickActivity.start(this, REQUEST_CODE_PICKIMAGE, FROM_CAMERA);
                    break;
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case REQUEST_CODE_PICKIMAGE:
                    dialog.setMessage(getString(R.string.modifying));
                    dialog.show();
                    mPresenterImpl.modifyAvatar(mCropFile);
                    break;
            }
        }
    }

    @Override
    public void modifySuccess() {
        ToastUtils.showToast(R.string.modify_success);
        ImageUtils.displayImage(this, mPresenterImpl.getUserAvatar(), user_avatar);
        MvKit.getOnUserStateChangeListener().stateChanged(OnUserStateChangeListener.UserState.AvatarChange);
        dialog.dismiss();
    }

}
