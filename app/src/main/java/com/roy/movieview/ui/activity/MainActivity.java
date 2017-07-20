package com.roy.movieview.ui.activity;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.movieview.MvKit;
import com.roy.movieview.R;
import com.roy.movieview.ui.activity.user.LoginRegisterActivity;
import com.roy.movieview.ui.fragment.main.AboutFragment;
import com.roy.movieview.ui.fragment.movie.MovieFragment;
import com.roy.movieview.ui.listener.OnUserStateChangeListener;
import com.roy.movieview.utils.common.LogUtils;
import com.roy.movieview.utils.image.ImageUtils;
import com.roy.movieview.utils.tip.SnackBarUtils;
import com.roy.movieview.utils.tip.ToastUtils;
import com.roy.movieview.utils.user.UserPreference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import solid.ren.skinlibrary.SkinConfig;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.loader.SkinManager;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener
        , OnUserStateChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static long DOUBLE_CLICK_TIME = 0L;

    @BindView(R.id.rl_root)
    RelativeLayout rl_root;

    @BindView(R.id.dl_main)
    DrawerLayout dl_main;

    @BindView(R.id.nv_main)
    NavigationView nv_main;

    private ImageView user_login;
    private TextView user_name;

    private RelativeLayout viewSplash;

    private Fragment mNowFragment;
    private FragmentManager mFragmentManager;

    private MovieFragment mMovieFragment;
    //private MusicFragment mMusicFragment;
    private AboutFragment mAboutFragment;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Inject
    UserPreference mUserPreference;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("qwe", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        if (mSharedPreferences.getBoolean("isFirst", true)) {
            SplashActivity.start(this);
            mEditor.putBoolean("isFirst", false);
            mEditor.commit();
        }
        isLogin();
    }

    private void isLogin() {
        if (mUserPreference.readUserInfo() != null) {
            ImageUtils.displayImage(this, mUserPreference.readAvatar(), user_login);
            user_name.setText(mUserPreference.readUserInfo().getUsername());
        }
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
        return false;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        View headerView = nv_main.getHeaderView(0);
        user_login = (ImageView) headerView.findViewById(R.id.user_login);
        user_name = (TextView) headerView.findViewById(R.id.user_name);
        user_login.setOnClickListener(this);

        mToolbar.setTitle(R.string.movie);
        mFragmentManager = getSupportFragmentManager();
        mMovieFragment = MovieFragment.newInstance();
        mAboutFragment = AboutFragment.newInstance();
        //mMusicFragment = MusicFragment.newInstance();

        //FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //transaction.add(R.id.fl_main_container, mMovieFragment).commit();
        switchFragment(mMovieFragment);

        mNowFragment = mMovieFragment;

        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, dl_main, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        dl_main.addDrawerListener(mActionBarDrawerToggle);
        nv_main.setNavigationItemSelectedListener(this);

        MvKit.setOnUserStateChangeListener(this);

        //switchFragment(mMovieFragment,mMovieFragment);

        ViewGroup parent = (ViewGroup) dl_main.getParent();
        View.inflate(this, R.layout.view_splash, parent);
        viewSplash = (RelativeLayout) parent.findViewById(R.id.view_splash);

        viewSplash.setVisibility(View.VISIBLE);
        dl_main.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(() -> mHandler.sendEmptyMessage(0), 2000);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    startAnim();
                    break;
            }
        }
    };

    /**
     * splash结束动画
     */
    private void startAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(viewSplash, "alphsa", 1f, 0f);
        animator.setDuration(1000);
        animator.start();
        dl_main.setVisibility(View.VISIBLE);
        animator.addUpdateListener(animation -> {
            float a = (Float) animation.getAnimatedValue();
            viewSplash.setAlpha(a);
            dl_main.setAlpha(1f - a);
        });
    }

    /**
     * fragment 切换
     *
     * @param show
     */
    public void switchFragment(Fragment show) {
        if (mNowFragment == null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!show.isAdded()) {    // 先判断是否被add过
                transaction.add(R.id.fl_main_container, show).commit();
            } else {
                transaction.show(show).commit();
            }
            mNowFragment = show;
        }
        if (mNowFragment != show) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!show.isAdded()) {    // 先判断是否被add过
                transaction.hide(mNowFragment).add(R.id.fl_main_container, show).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mNowFragment).show(show).commit(); // 隐藏当前的fragment，显示下一个
            }
            mNowFragment = show;
        }
    }

    /**
     * 侧滑菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_movie:
                switchFragment(mMovieFragment);
                mToolbar.setTitle(R.string.movie);
                break;
            case R.id.item_music:
                //switchFragment(mNowFragment, mMusicFragment);
                //mToolbar.setTitle(R.string.music);
                ToastUtils.showSingleLongToast("功能还未开放..");
                break;
            case R.id.item_collection:
                //MovieCollectionActivity.start(this);
                break;
            case R.id.item_setting:
                modifySkin();
                break;
            case R.id.item_feedback:
                break;
            case R.id.item_about:
                switchFragment(mAboutFragment);
                mToolbar.setTitle(R.string.about_app);
                break;
        }
        dl_main.closeDrawers();
        return true;
    }


    private void modifySkin() {
        if (!SkinConfig.isDefaultSkin(this)) {
            SkinManager.getInstance().restoreDefaultTheme();
            ImageUtils.displayImage(mContext, mUserPreference.readAvatar(), user_login);
            user_name.setText(mUserPreference.readUserInfo().getUsername());
        } else {
            SkinManager.getInstance().loadSkin("test.skin", new SkinLoaderListener() {
                @Override
                public void onStart() {
                    ToastUtils.showSingleLongToast("开始");
                }

                @Override
                public void onSuccess() {
                    ToastUtils.showSingleLongToast("换肤成功");
                    new Handler().postDelayed(() -> runOnUiThread(() -> {
                        ImageUtils.displayImage(mContext, mUserPreference.readAvatar(), user_login);
                        user_name.setText(mUserPreference.readUserInfo().getUsername());
                    }), 500);
                }

                @Override
                public void onFailed(String errMsg) {
                    ToastUtils.showSingleLongToast("换肤失败");
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        }
    }


    /**
     * 退出监听(连续点击两次退出)
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && dl_main != null) {
            if (dl_main.isDrawerOpen(Gravity.LEFT)) {
                dl_main.closeDrawer(Gravity.LEFT);
            } else {
                dl_main.openDrawer(Gravity.LEFT);
            }
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (dl_main.isDrawerOpen(Gravity.LEFT)) {
                dl_main.closeDrawer(Gravity.LEFT);
            } else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    SnackBarUtils.LongSnackbar(rl_root, "再按一次退出", SnackBarUtils.Info).show();
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 侧滑菜单头部点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_login:
                LoginRegisterActivity.start(this);
                break;
        }
    }

    @Override
    public void stateChanged(UserState state) {
        switch (state) {
            case Login:
                ImageUtils.displayImage(this, mUserPreference.readAvatar(), user_login);
                user_name.setText(mUserPreference.readUserInfo().getUsername());
                break;
            case Logout:
                ImageUtils.displayImage(this, R.drawable.avatar_default, user_login);
                user_name.setText("点击登录");
                break;
            case AvatarChange:
                ImageUtils.displayImage(this, mUserPreference.readAvatar(), user_login);
                break;
        }
    }


}
