package com.android.euithemes;

public class Theme {
    private String mUrl;
    private String mPicture;
    private String mName;
    private String mFileName;
    private boolean mIsDownloaded;

    public Theme() {}

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        this.mPicture = picture;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public boolean isDownloaded() {
        return mIsDownloaded;
    }

    public void setDownloaded(boolean isDownloaded) {
        this.mIsDownloaded = isDownloaded;
    }
}
