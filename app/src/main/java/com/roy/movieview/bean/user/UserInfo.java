package com.roy.movieview.bean.user;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserInfo {
    private String objectId;
    private String username;
    private String password;
    private String telephone;
    private String email;

    public UserInfo() {

    }

    public UserInfo(String objectId, String username, String password,String telephone,String email) {
        this.objectId = objectId;
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
