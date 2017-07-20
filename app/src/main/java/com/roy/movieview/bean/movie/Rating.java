
package com.roy.movieview.bean.movie;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("average")
    private Double mAverage;
    @SerializedName("max")
    private Long mMax;
    @SerializedName("min")
    private Long mMin;
    @SerializedName("stars")
    private String mStars;

    public Double getAverage() {
        return mAverage;
    }

    public void setAverage(Double average) {
        mAverage = average;
    }

    public Long getMax() {
        return mMax;
    }

    public void setMax(Long max) {
        mMax = max;
    }

    public Long getMin() {
        return mMin;
    }

    public void setMin(Long min) {
        mMin = min;
    }

    public String getStars() {
        return mStars;
    }

    public void setStars(String stars) {
        mStars = stars;
    }

}
