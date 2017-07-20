
package com.roy.movieview.bean.movie;

import com.google.gson.annotations.SerializedName;

public class Cast {

    @SerializedName("alt")
    private String mAlt;
    @SerializedName("avatars")
    private Avatars mAvatars;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public String getAlt() {
        return mAlt;
    }

    public void setAlt(String alt) {
        mAlt = alt;
    }

    public Avatars getAvatars() {
        return mAvatars;
    }

    public void setAvatars(Avatars avatars) {
        mAvatars = avatars;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
