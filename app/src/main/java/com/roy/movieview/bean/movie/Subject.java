
package com.roy.movieview.bean.movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("alt")
    private String mAlt;
    @SerializedName("casts")
    private List<Cast> mCasts;
    @SerializedName("collect_count")
    private Long mCollectCount;
    @SerializedName("directors")
    private List<Director> mDirectors;
    @SerializedName("genres")
    private List<String> mGenres;
    @SerializedName("id")
    private String mId;
    @SerializedName("images")
    private Images mImages;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("rating")
    private Rating mRating;
    @SerializedName("subtype")
    private String mSubtype;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("year")
    private String mYear;

    public String getAlt() {
        return mAlt;
    }

    public void setAlt(String alt) {
        mAlt = alt;
    }

    public List<Cast> getCasts() {
        return mCasts;
    }

    public void setCasts(List<Cast> casts) {
        mCasts = casts;
    }

    public Long getCollectCount() {
        return mCollectCount;
    }

    public void setCollectCount(Long collectCount) {
        mCollectCount = collectCount;
    }

    public List<Director> getDirectors() {
        return mDirectors;
    }

    public void setDirectors(List<Director> directors) {
        mDirectors = directors;
    }

    public List<String> getGenres() {
        return mGenres;
    }

    public void setGenres(List<String> genres) {
        mGenres = genres;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Images getImages() {
        return mImages;
    }

    public void setImages(Images images) {
        mImages = images;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public Rating getRating() {
        return mRating;
    }

    public void setRating(Rating rating) {
        mRating = rating;
    }

    public String getSubtype() {
        return mSubtype;
    }

    public void setSubtype(String subtype) {
        mSubtype = subtype;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

}
