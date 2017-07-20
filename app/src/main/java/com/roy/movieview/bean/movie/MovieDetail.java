
package com.roy.movieview.bean.movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieDetail {

    @SerializedName("aka")
    private List<String> mAka;
    @SerializedName("alt")
    private String mAlt;
    @SerializedName("casts")
    private List<Cast> mCasts;
    @SerializedName("collect_count")
    private Long mCollectCount;
    @SerializedName("comments_count")
    private Long mCommentsCount;
    @SerializedName("countries")
    private List<String> mCountries;
    @SerializedName("current_season")
    private Object mCurrentSeason;
    @SerializedName("directors")
    private List<Director> mDirectors;
    @SerializedName("do_count")
    private Object mDoCount;
    @SerializedName("douban_site")
    private String mDoubanSite;
    @SerializedName("episodes_count")
    private Object mEpisodesCount;
    @SerializedName("genres")
    private List<String> mGenres;
    @SerializedName("id")
    private String mId;
    @SerializedName("images")
    private Images mImages;
    @SerializedName("mobile_url")
    private String mMobileUrl;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("rating")
    private Rating mRating;
    @SerializedName("ratings_count")
    private Long mRatingsCount;
    @SerializedName("reviews_count")
    private Long mReviewsCount;
    @SerializedName("schedule_url")
    private String mScheduleUrl;
    @SerializedName("seasons_count")
    private Object mSeasonsCount;
    @SerializedName("share_url")
    private String mShareUrl;
    @SerializedName("subtype")
    private String mSubtype;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("wish_count")
    private Long mWishCount;
    @SerializedName("year")
    private String mYear;

    private int praiseNum;

    private boolean isPraise;

    private int commentNum;

    public List<String> getAka() {
        return mAka;
    }

    public void setAka(List<String> aka) {
        mAka = aka;
    }

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

    public Long getCommentsCount() {
        return mCommentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        mCommentsCount = commentsCount;
    }

    public List<String> getCountries() {
        return mCountries;
    }

    public void setCountries(List<String> countries) {
        mCountries = countries;
    }

    public Object getCurrentSeason() {
        return mCurrentSeason;
    }

    public void setCurrentSeason(Object currentSeason) {
        mCurrentSeason = currentSeason;
    }

    public List<Director> getDirectors() {
        return mDirectors;
    }

    public void setDirectors(List<Director> directors) {
        mDirectors = directors;
    }

    public Object getDoCount() {
        return mDoCount;
    }

    public void setDoCount(Object doCount) {
        mDoCount = doCount;
    }

    public String getDoubanSite() {
        return mDoubanSite;
    }

    public void setDoubanSite(String doubanSite) {
        mDoubanSite = doubanSite;
    }

    public Object getEpisodesCount() {
        return mEpisodesCount;
    }

    public void setEpisodesCount(Object episodesCount) {
        mEpisodesCount = episodesCount;
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

    public String getMobileUrl() {
        return mMobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        mMobileUrl = mobileUrl;
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

    public Long getRatingsCount() {
        return mRatingsCount;
    }

    public void setRatingsCount(Long ratingsCount) {
        mRatingsCount = ratingsCount;
    }

    public Long getReviewsCount() {
        return mReviewsCount;
    }

    public void setReviewsCount(Long reviewsCount) {
        mReviewsCount = reviewsCount;
    }

    public String getScheduleUrl() {
        return mScheduleUrl;
    }

    public void setScheduleUrl(String scheduleUrl) {
        mScheduleUrl = scheduleUrl;
    }

    public Object getSeasonsCount() {
        return mSeasonsCount;
    }

    public void setSeasonsCount(Object seasonsCount) {
        mSeasonsCount = seasonsCount;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }

    public String getSubtype() {
        return mSubtype;
    }

    public void setSubtype(String subtype) {
        mSubtype = subtype;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getWishCount() {
        return mWishCount;
    }

    public void setWishCount(Long wishCount) {
        mWishCount = wishCount;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }


    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}


