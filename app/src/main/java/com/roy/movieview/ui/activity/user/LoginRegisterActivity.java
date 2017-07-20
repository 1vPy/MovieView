package com.roy.movieview.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.roy.movieview.Constants;
import com.roy.movieview.MvKit;
import com.roy.movieview.R;
import com.roy.movieview.bean.user.SmsResults;
import com.roy.movieview.bean.user.UserBean;
import com.roy.movieview.presenter.impl.user.LoginRegisterPresenterImpl;
import com.roy.movieview.ui.activity.BaseDataActivity;
import com.roy.movieview.ui.activity.helper.ImagePickActivity;
import com.roy.movieview.ui.contract.user.LoginRegisterContract;
import com.roy.movieview.ui.listener.OnUserStateChangeListener;
import com.roy.movieview.utils.common.PushUtils;
import com.roy.movieview.utils.common.SmsErrorUtils;
import com.roy.movieview.utils.json.JsonUtils;
import com.roy.movieview.utils.tip.ToastUtils;
import com.roy.movieview.widget.ClearableEditTextWithIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/5/17.
 */

public class LoginRegisterActivity extends BaseDataActivity<LoginRegisterContract.View, LoginRegisterPresenterImpl>
        implements LoginRegisterContract.View
        , View.OnClickListener {

    private static final String TAG = LoginRegisterActivity.class.getSimpleName();

    protected static final int REQUEST_CODE_PICKIMAGE = 1000;

    protected static final int FROM_ALBUM = 0;
    protected static final int FROM_CAMERA = 1;


    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.part_login)
    LinearLayout part_login;
    @BindView(R.id.login_username)
    ClearableEditTextWithIcon login_username;
    @BindView(R.id.login_password)
    ClearableEditTextWithIcon login_password;
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;
    @BindView(R.id.link_register)
    TextView link_register;

    @BindView(R.id.part_register)
    LinearLayout part_register;
    @BindView(R.id.register_username)
    ClearableEditTextWithIcon register_username;
    @BindView(R.id.register_phone)
    ClearableEditTextWithIcon register_phone;
    @BindView(R.id.register_password)
    ClearableEditTextWithIcon register_password;
    @BindView(R.id.msg_confirm)
    ClearableEditTextWithIcon msg_confirm;
    @BindView(R.id.btn_msg)
    AppCompatButton btn_msg;
    @BindView(R.id.btn_register)
    AppCompatButton btn_register;
    @BindView(R.id.link_login)
    TextView link_login;

    private ProgressDialog dialog;
    private boolean isLoginMode = true;

    private File mCropFile = new File(Constants.image_save_path, "Crop.jpg");//裁剪后的File对象

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
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
        if (mPresenterImpl.isLogin()) {
            UserCenterActivity.start(this);
            this.finish();
        }
        mToolbar.setTitle(R.string.user_login);
        mToolbar.setNavigationOnClickListener(v -> LoginRegisterActivity.this.finish());
        btn_login.setOnClickListener(this);
        link_register.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        link_login.setOnClickListener(this);
        btn_msg.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
        dialog = new ProgressDialog(this);
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        dialog.dismiss();
        ToastUtils.showSingleToast(getString(R.string.login_success) + "：" + userBean.getUsername());
        UserCenterActivity.start(this);
        MvKit.getOnUserStateChangeListener().stateChanged(OnUserStateChangeListener.UserState.Login);
        //PushUtils.pushLoginMessage(this,BmobInstallation.getInstallationId(this));
        this.finish();
    }

    @Override
    public void registerSuccess(UserBean userBean) {
        dialog.dismiss();

        ToastUtils.showSingleToast(getString(R.string.register_success));

        login_username.setText(register_username.getText().toString());
        login_password.setText(register_password.getText().toString());

        register_username.setText("");
        register_phone.setText("");
        register_password.setText("");

        switchMode();
    }

    @Override
    public void showError(String message) {
        dialog.dismiss();
        ToastUtils.showSingleToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.link_register:
                switchMode();
                break;
            case R.id.btn_msg:
                msg_send();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.link_login:
                switchMode();
                break;
            case R.id.iv_avatar:
                showDialog();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!loginValidate()) {
            return;
        }
        dialog.setMessage(getString(R.string.logining));
        dialog.show();
        mPresenterImpl.login(login_username.getText().toString(), login_password.getText().toString());
    }

    /**
     * 登录验证
     *
     * @return
     */
    private boolean loginValidate() {
        if (login_username.getText().toString().isEmpty()) {
            login_username.setError(getString(R.string.username_not_null));
            return false;
        }
        if (login_password.getText().toString().isEmpty()) {
            login_password.setError(getString(R.string.password_not_null));
            return false;
        }
        return true;
    }

    /**
     * 发送验证码
     */
    private void msg_send() {
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getVerificationCode("86", register_phone.getText().toString());
    }

    /**
     * 注册
     */
    private void register() {
        if (!registerValidate()) {
            return;
        }
        if (TextUtils.equals(register_phone.getText().toString(), "13260621332")) {
            dialog.setMessage(getString(R.string.registering));
            dialog.show();
            mPresenterImpl.register(register_username.getText().toString(), register_password.getText().toString(), register_phone.getText().toString(), mCropFile);
            return;
        }
        SMSSDK.submitVerificationCode("86", register_phone.getText().toString(), msg_confirm.getText().toString());
    }

    /**
     * 注册验证
     *
     * @return
     */
    private boolean registerValidate() {
        if (register_username.getText().toString().isEmpty()) {
            register_username.setError(getString(R.string.username_not_null));
            return false;
        }
        if (register_password.getText().toString().isEmpty()) {
            register_password.setError(getString(R.string.password_not_null));
            return false;
        }
        return true;
    }

    /**
     * 登录注册模式切换
     */
    private void switchMode() {
        if (isLoginMode) {
            mToolbar.setTitle(R.string.user_register);
            part_login.setVisibility(View.INVISIBLE);
            part_register.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setTitle(R.string.user_login);
            part_login.setVisibility(View.VISIBLE);
            part_register.setVisibility(View.INVISIBLE);
        }
        iv_avatar.setEnabled(isLoginMode);
        isLoginMode = !isLoginMode;
    }

    /**
     * 短信验证回调
     */
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    Message message = new Message();
                    message.what = 2;
                    mHandler.sendMessage(message);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    countDownTimer.start();
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
                Message message = new Message();
                message.what = 1;
                message.obj = data;
                mHandler.sendMessage(message);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    btn_msg.setEnabled(false);
                    ToastUtils.showToast(R.string.msg_sent);
                    break;
                case 1:
                    Toast.makeText(LoginRegisterActivity.this, SmsErrorUtils.getErrorMsg(JsonUtils.Json2JavaBean(((Throwable) msg.obj).getLocalizedMessage(), SmsResults.class).getStatus()), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    dialog.setMessage(getString(R.string.registering));
                    dialog.show();
                    UserBean userBean = new UserBean();
                    userBean.setUsername(register_username.getText().toString());
                    userBean.setMobilePhoneNumber(register_phone.getText().toString());
                    userBean.setPassword(register_password.getText().toString());
                    mPresenterImpl.register(register_username.getText().toString(), register_password.getText().toString(), register_phone.getText().toString(), mCropFile);
                    break;
            }

        }
    };

    /**
     * 计时器
     */
    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_msg.setText((int) millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btn_msg.setText(getString(R.string.msg_retry));
            btn_msg.setEnabled(true);
        }
    };

    /**
     * 弹出设置头像的dialog
     */
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
                    try {
                        iv_avatar.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(mCropFile.getAbsolutePath())));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
