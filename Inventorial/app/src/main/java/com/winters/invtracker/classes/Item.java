package com.winters.invtracker.classes;

public class Item {

    private int mId;
    private String mText;
    private String mQuantity;

    public Item() {
        super();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Item(int id, String text, String quantity) {
        mId = id;
        mText = text;
        mQuantity = quantity;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

}