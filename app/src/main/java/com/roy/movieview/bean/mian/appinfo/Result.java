
package com.roy.movieview.bean.mian.appinfo;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("apkFile")
    private ApkFile mApkFile;
    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("fileMD5")
    private String mFileMD5;
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("updatedAt")
    private String mUpdatedAt;
    @SerializedName("versionCode")
    private Long mVersionCode;
    @SerializedName("versionName")
    private String mVersionName;

    public ApkFile getApkFile() {
        return mApkFile;
    }

    public void setApkFile(ApkFile apkFile) {
        mApkFile = apkFile;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getFileMD5() {
        return mFileMD5;
    }

    public void setFileMD5(String fileMD5) {
        mFileMD5 = fileMD5;
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

    public Long getVersionCode() {
        return mVersionCode;
    }

    public void setVersionCode(Long versionCode) {
        mVersionCode = versionCode;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public void setVersionName(String versionName) {
        mVersionName = versionName;
    }

}
