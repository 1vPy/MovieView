
package com.roy.movieview.bean.user.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginConfig {

    @SerializedName("results")
    private List<ConfigResult> mResults;

    public List<ConfigResult> getResults() {
        return mResults;
    }

    public void setResults(List<ConfigResult> results) {
        mResults = results;
    }

}
