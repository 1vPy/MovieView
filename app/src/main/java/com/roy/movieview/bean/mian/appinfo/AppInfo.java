
package com.roy.movieview.bean.mian.appinfo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AppInfo {

    @SerializedName("results")
    private List<Result> mResults;

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

}
