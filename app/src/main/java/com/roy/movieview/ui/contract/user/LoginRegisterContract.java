package com.roy.movieview.ui.contract.user;

import com.roy.movieview.bean.user.UserBean;
import com.roy.movieview.ui.contract.BaseContract;

import java.io.File;

/**
 * Created by 1vPy(Roy) on 2017/6/22.
 */

public interface LoginRegisterContract {
    interface View extends BaseContract.View {
        void loginSuccess(UserBean userBean);

        void registerSuccess(UserBean userBean);

        //void isLocalLogin(boolean is);
    }

    interface Presenter {
        boolean isLogin();

        void login(String username,String password);

        void register(String username,String password,String phoneNum,File userAvatar);

        //void LoginCheck();
    }
}
