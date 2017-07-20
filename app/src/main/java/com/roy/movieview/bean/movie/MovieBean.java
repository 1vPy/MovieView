
package com.roy.movieview.bean.movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieBean {

    @SerializedName("count")
    private Long mCount;
    @SerializedName("start")
    private Long mStart;
    @SerializedName("subjects")
    private List<Subject> mSubjects;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("total")
    private Long mTotal;

    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public Long getStart() {
        return mStart;
    }

    public void setStart(Long start) {
        mStart = start;
    }

    public List<Subject> getSubjects() {
        return mSubjects;
    }

    public void setSubjects(List<Subject> subjects) {
        mSubjects = subjects;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
