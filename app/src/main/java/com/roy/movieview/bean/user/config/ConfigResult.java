
package com.roy.movieview.bean.user.config;

import com.google.gson.annotations.SerializedName;

public class ConfigResult {

    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("sessionToken")
    private String mSessionToken;
    @SerializedName("updatedAt")
    private String mUpdatedAt;
    @SerializedName("username")
    private String mUsername;

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
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

}
