package com.hadarfem.fixit.dal.models;

import java.util.Date;

/**
 * Created by Tzach & Nadav on 1/22/2018.
 */

public class Problem {
    private String id;
    private String userName;
    private String title;
    private String description;
    private String city;
    private String category;
    private String pictureBase64;
    private Date date;

    public String getId() {
        return id;
    }

    public Problem setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Problem setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Problem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Problem setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Problem setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Problem setDescription(String description) {
        this.description = description;
        return this;
    }



    public String getPictureBase64() {
        return pictureBase64;
    }

    public Problem setPictureBase64(String pictureBase64) {
        this.pictureBase64 = pictureBase64;
        return this;
    }


    public Date getDate() {
        return date;
    }

    public Problem setDate(Date date) {
        this.date = date;
        return this;
    }
}
