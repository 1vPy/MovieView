
package com.roy.movieview.bean.user.config;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DConfigResult {

    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("installationId")
    private String mInstallationId;
    @SerializedName("objectId")
    private String mObjectId;
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

    public String getInstallationId() {
        return mInstallationId;
    }

    public void setInstallationId(String installationId) {
        mInstallationId = installationId;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        mObjectId = objectId;
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
