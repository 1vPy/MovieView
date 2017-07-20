
package com.roy.movieview.bean.user.config;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DeviceConfig {

    @SerializedName("results")
    private List<DConfigResult> mResults;

    public List<DConfigResult> getResults() {
        return mResults;
    }

    public void setResults(List<DConfigResult> results) {
        mResults = results;
    }

}
