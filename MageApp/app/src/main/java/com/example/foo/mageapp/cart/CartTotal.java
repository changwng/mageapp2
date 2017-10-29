package com.example.foo.mageapp.cart;

/**
 * Created by foo on 9/26/17.
 */

public class CartTotal {
    protected String mType;
    protected String mTitle;
    protected String mValue;
    protected String mFormatedValue;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public String getFormatedValue() {
        return mFormatedValue;
    }

    public void setFormatedValue(String formatedValue) {
        mFormatedValue = formatedValue;
    }
}
