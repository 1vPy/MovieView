
package com.roy.movieview.bean.user.movie.praise;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PraiseResult {

    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("movieId")
    private String mMovieId;
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
