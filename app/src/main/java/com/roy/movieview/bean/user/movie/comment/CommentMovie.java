
package com.roy.movieview.bean.user.movie.comment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class CommentMovie {

    @SerializedName("results")
    private List<CommentResult> mResults;

    public List<CommentResult> getResults() {
        return mResults;
    }

    public void setResults(List<CommentResult> results) {
        mResults = results;
    }

}
