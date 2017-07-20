
package com.roy.movieview.bean.user.movie;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SuccessBean {

    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("objectId")
    private String mObjectId;

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

}
