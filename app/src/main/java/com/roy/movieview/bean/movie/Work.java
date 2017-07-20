
package com.roy.movieview.bean.movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Work {

    @SerializedName("roles")
    private List<String> mRoles;
    @SerializedName("subject")
    private Subject mSubject;

    public List<String> getRoles() {
        return mRoles;
    }

    public void setRoles(List<String> roles) {
        mRoles = roles;
    }

    public Subject getSubject() {
        return mSubject;
    }

    public void setSubject(Subject subject) {
        mSubject = subject;
    }

}
