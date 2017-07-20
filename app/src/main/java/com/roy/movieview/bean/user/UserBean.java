
package com.roy.movieview.bean.user;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserBean {

    @SerializedName("userHeader")
    private UserHeader mUserHeader;
    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("emailVerified")
    private Boolean mEmailVerified;
    @SerializedName("mobilePhoneNumber")
    private String mMobilePhoneNumber;
    @SerializedName("mobilePhoneNumberVerified")
    private Boolean mMobilePhoneNumberVerified;
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("sessionToken")
    private String mSessionToken;
    @SerializedName("updatedAt")
    private String mUpdatedAt;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("password")
    private String mPassword;

    private String installationId;

    public UserHeader getUserHeader() {
        return mUserHeader;
    }

    public void setUserHeader(UserHeader userHeader) {
        mUserHeader = userHeader;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Boolean getEmailVerified() {
        return mEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        mEmailVerified = emailVerified;
    }

    public String getMobilePhoneNumber() {
        return mMobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        mMobilePhoneNumber = mobilePhoneNumber;
    }

    public Boolean getMobilePhoneNumberVerified() {
        return mMobilePhoneNumberVerified;
    }

    public void setMobilePhoneNumberVerified(Boolean mobilePhoneNumberVerified) {
        mMobilePhoneNumberVerified = mobilePhoneNumberVerified;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        mObjectId = objectId;
    }

    public String getSessionToken() {
        return mSessionToken;
    }

    public void setSessionToken(String sessionToken) {
        mSessionToken = sessionToken;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }


    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }
}
