package com.example.foo.mageapp.checkout;

/**
 * Created by foo on 10/13/17.
 */

public class ShippingMethodRate {
    protected String mLabel;
    protected String mCode;
    protected String mPrice;
    protected String mFormatedPrice;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getFormatedPrice() {
        return mFormatedPrice;
    }

    public void setFormatedPrice(String formatedPrice) {
        mFormatedPrice = formatedPrice;
    }

    public String getRadioButtonLabel() {
        return String.format("%s: %s", mLabel, mFormatedPrice);
    }
}
