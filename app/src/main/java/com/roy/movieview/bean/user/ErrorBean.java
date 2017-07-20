
package com.roy.movieview.bean.user;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ErrorBean {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("error")
    private String mError;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

}
