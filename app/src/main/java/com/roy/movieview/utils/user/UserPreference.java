package com.roy.movieview.utils.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.roy.movieview.bean.user.UserInfo;
import com.roy.movieview.utils.common.LogUtils;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/6/22.
 */

public class UserPreference {

    private static final String TAG = UserPreference.class.getSimpleName();
    private final static String SP_NAME = "UserInfo";
    private final static int MODE = Context.MODE_PRIVATE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static UserPreference instance = null;

    @Inject
    public UserPreference(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, MODE);
        editor = preferences.edit();
    }


    public void saveUserInfo(UserInfo userInfo) {
        editor.putString("username", userInfo.getUsername());
        editor.putString("password", userInfo.getPassword());
        editor.putString("telephone", userInfo.getTelephone());
        editor.putString("email", userInfo.getEmail());
        editor.putString("objectId", userInfo.getObjectId());
        editor.commit();
    }

    public UserInfo readUserInfo() {
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        String telephone = preferences.getString("telephone", "");
        String email = preferences.getString("email", "");
        String objectId = preferences.getString("objectId", "");
        LogUtils.log(TAG, "username:" + username + ",password:" + password, LogUtils.DEBUG);
        if (!username.isEmpty() && !password.isEmpty() && !objectId.isEmpty()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setPassword(password);
            userInfo.setTelephone(telephone);
            userInfo.setEmail(email);
            userInfo.setObjectId(objectId);
            return userInfo;
        }
        return null;
    }

    public void saveAvatar(String avatar) {
        editor.putString("avatar", avatar);
        editor.commit();
    }

    public String readAvatar() {
        return preferences.getString("avatar", "");
    }

    public void saveSessionToken(String sessionToken) {
        editor.putString("sessionToken", sessionToken);
        editor.commit();
    }

    public String readSessionToken() {
        return preferences.getString("sessionToken", "");
    }

    public void clearUserInfo() {
        editor.clear();
        editor.commit();
    }


}
