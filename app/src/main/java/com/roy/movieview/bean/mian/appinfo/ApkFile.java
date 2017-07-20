
package com.roy.movieview.bean.mian.appinfo;

import com.google.gson.annotations.SerializedName;

public class ApkFile {

    @SerializedName("cdn")
    private String mCdn;
    @SerializedName("filename")
    private String mFilename;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("__type")
    private String m_Type;

    public String getCdn() {
        return mCdn;
    }

    public void setCdn(String cdn) {
        mCdn = cdn;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String filename) {
        mFilename = filename;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String get_Type() {
        return m_Type;
    }

    public void set_Type(String _Type) {
        m_Type = _Type;
    }

}
