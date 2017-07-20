
package com.roy.movieview.bean.movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MStarDetail {

    @SerializedName("aka")
    private List<String> mAka;
    @SerializedName("aka_en")
    private List<String> mAkaEn;
    @SerializedName("alt")
    private String mAlt;
    @SerializedName("avatars")
    private Avatars mAvatars;
    @SerializedName("born_place")
    private String mBornPlace;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("id")
    private String mId;
    @SerializedName("mobile_url")
    private String mMobileUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    @SerializedName("works")
    private List<Work> mWorks;

    public List<String> getAka() {
        return mAka;
    }

    public void setAka(List<String> aka) {
        mAka = aka;
    }

    public List<String> getAkaEn() {
        return mAkaEn;
    }

    public void setAkaEn(List<String> akaEn) {
        mAkaEn = akaEn;
    }

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

    public String getBornPlace() {
        return mBornPlace;
    }

    public void setBornPlace(String bornPlace) {
        mBornPlace = bornPlace;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMobileUrl() {
        return mMobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        mMobileUrl = mobileUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNameEn() {
        return mNameEn;
    }

    public void setNameEn(String nameEn) {
        mNameEn = nameEn;
    }

    public List<Work> getWorks() {
        return mWorks;
    }

    public void setWorks(List<Work> works) {
        mWorks = works;
    }

}
