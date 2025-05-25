package com.manarat.manaratlibrary;

import java.io.Serializable;
import java.util.List;

public class CulturalActivityItem implements Serializable {
    private String title;
    private String date;
    private String description;
    private int imageResourceId;
    private List<ContentItem> contentList;

    public CulturalActivityItem(String title, String date, String description, int imageResourceId) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.contentList = null;
    }

    public CulturalActivityItem(String title, String date, String description, int imageResourceId, List<ContentItem> contentList) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.contentList = contentList;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public List<ContentItem> getContentList() {
        return contentList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setContentList(List<ContentItem> contentList) {
        this.contentList = contentList;
    }
}