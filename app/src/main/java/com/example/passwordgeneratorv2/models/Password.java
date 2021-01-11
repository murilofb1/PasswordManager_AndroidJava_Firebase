package com.example.passwordgeneratorv2.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.UUID;

public class Password implements Serializable {
    private String site;
    private String password;
    private String iconLink;
    private String siteLink;
    private boolean favorite = false;

    public Password() {
    }

    public Password(String site, String password, String iconLink, String siteLink) {
        this.site = site;
        this.password = password;
        this.iconLink = iconLink;
        this.siteLink = siteLink;
    }

    public Password(String site, String iconLink) {
        this.site = site;
        this.iconLink = iconLink;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getSiteLink() {
        return siteLink;
    }

    public String getIconLink() {
        return iconLink;
    }

    public String getPassword() {
        return password;
    }

    public String getSite() {
        return site;
    }


}
