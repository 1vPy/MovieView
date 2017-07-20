
package com.roy.movieview.bean.user.movie.praise;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PraiseMovie {

    @SerializedName("results")
    private List<PraiseResult> mResults;

    public List<PraiseResult> getResults() {
        return mResults;
    }

    public void setResults(List<PraiseResult> results) {
        mResults = results;
    }

}
