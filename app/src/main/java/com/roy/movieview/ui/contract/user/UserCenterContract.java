package com.roy.movieview.ui.contract.user;

import com.roy.movieview.bean.user.UserHeader;
import com.roy.movieview.bean.user.UserInfo;
import com.roy.movieview.ui.contract.BaseContract;

import java.io.File;

/**
 * Created by Administrator on 2017/6/27.
 */

public interface UserCenterContract {

    interface View extends BaseContract.View{
        void modifySuccess();
    }

    interface Presenter{
        String getUserAvatar();

        UserInfo getUserInfo();

        void logout();

        void modifyAvatar(File userAvatar);

    }

}
