package com.manarat.manaratlibrary;

import java.io.Serializable;

public class ContentItem implements Serializable {
    private String text;
    private int imageResourceId;

    public ContentItem(String text) {
        this.text = text;
        this.imageResourceId = 0; // لا يوجد صورة لهذا العنصر
    }

    public ContentItem(int imageResourceId) {
        this.imageResourceId = imageResourceId;
        this.text = null; // لا يوجد نص لهذا العنصر
    }

    public String getText() {
        return text;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}