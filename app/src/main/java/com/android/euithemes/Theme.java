package com.android.euithemes;

public class Theme {
    private String mUrl;
    private String mPicture;
    private String mName;
    private boolean mIsDownloaded;

    public Theme() {}

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String mPicture) {
        this.mPicture = mPicture;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }


    public boolean isDownloaded() {
        return mIsDownloaded;
    }

    public void setDownloaded(boolean mIsDownloaded) {
        this.mIsDownloaded = mIsDownloaded;
    }
}
