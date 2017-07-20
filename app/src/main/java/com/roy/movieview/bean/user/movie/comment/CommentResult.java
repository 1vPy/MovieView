
package com.roy.movieview.bean.user.movie.comment;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CommentResult {

    @SerializedName("comment")
    private String mComment;
    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("movieId")
    private String mMovieId;
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("rating")
    private Float mRating;
    @SerializedName("updatedAt")
    private String mUpdatedAt;
    @SerializedName("username")
    private String mUsername;

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public void setMovieId(String movieId) {
        mMovieId = movieId;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        mObjectId = objectId;
    }

    public Float getRating() {
        return mRating;
    }

    public void setRating(Float rating) {
        mRating = rating;
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
