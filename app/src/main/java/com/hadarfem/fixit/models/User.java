package com.hadarfem.fixit.models;

import java.io.Serializable;

/**
 * Created by Tzach & Nadav on 2/2/2018.
 */

public class User implements Serializable {
    private String name;
    private String profilePictureUrl;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public User setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        return this;
    }
}
