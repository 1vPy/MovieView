package com.roy.movieview.ui.listener;

/**
 * Created by Administrator on 2017/6/28.
 */

public interface OnUserStateChangeListener {
    enum UserState{
        Login,
        Logout,
        AvatarChange;
    }
    void stateChanged(UserState state);
}
