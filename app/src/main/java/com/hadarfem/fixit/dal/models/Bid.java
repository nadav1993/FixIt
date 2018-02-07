package com.hadarfem.fixit.dal.models;

import java.util.Date;

/**
 * Created by Tzach & Nadav on 1/22/2018.
 */

public class Bid {
    private String id;
    private String bidderUserName;
    private String costumerUserName;
    private String title;
    private int price;
    private Date date;
    private String pictureUrl;

    public String getId() {
        return id;
    }

    public Bid setId(String id) {
        this.id = id;
        return this;
    }

    public String getBidderUserName() {
        return bidderUserName;
    }

    public Bid setBidderUserName(String bidderUserName) {
        this.bidderUserName = bidderUserName;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Bid setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getCostumerUserName() {
        return costumerUserName;
    }

    public Bid setCostumerUserName(String costumerUserName) {
        this.costumerUserName = costumerUserName;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Bid setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Bid setPrice(int price) {
        this.price = price;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Bid setDate(Date date) {
        this.date = date;
        return this;
    }
}
