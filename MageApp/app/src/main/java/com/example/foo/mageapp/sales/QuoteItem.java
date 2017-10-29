package com.example.foo.mageapp.sales;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/22/17.
 */

public class QuoteItem {
    protected String mEntityId;
    protected String mEntityType;
    protected String mItemId;
    protected String mName;
    protected String mQty;
    protected String mIcon;
    protected String mPrice;
    protected String mFormatedPrice;
    protected String mSubtotal;
    protected String mFormatedSubtotal;
    protected List<QuoteItemOption> mOptions = new ArrayList<>();

    public String getEntityId() {
        return mEntityId;
    }

    public void setEntityId(String entityId) {
        mEntityId = entityId;
    }

    public String getEntityType() {
        return mEntityType;
    }

    public void setEntityType(String entityType) {
        mEntityType = entityType;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String itemId) {
        mItemId = itemId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getQty() {
        return mQty;
    }

    public void setQty(String qty) {
        mQty = qty;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
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

    public String getSubtotal() {
        return mSubtotal;
    }

    public void setSubtotal(String subtotal) {
        mSubtotal = subtotal;
    }

    public String getFormatedSubtotal() {
        return mFormatedSubtotal;
    }

    public void setFormatedSubtotal(String formatedSubtotal) {
        mFormatedSubtotal = formatedSubtotal;
    }

    public List<QuoteItemOption> getOptions() {
        return mOptions;
    }

    public void setOptions(List<QuoteItemOption> options) {
        mOptions = options;
    }
}
