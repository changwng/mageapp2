package com.example.foo.mageapp.cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/6/17.
 */

public class Cart {
    protected String mIsVirtual;
    protected String mSummaryQty;
    protected List<CartItem> mItems = new ArrayList<>();

    public String getIsVirtual() {
        return mIsVirtual;
    }

    public void setIsVirtual(String isVirtual) {
        mIsVirtual = isVirtual;
    }

    public String getSummaryQty() {
        return mSummaryQty;
    }

    public void setSummaryQty(String summaryQty) {
        mSummaryQty = summaryQty;
    }

    public List<CartItem> getItems() {
        return mItems;
    }

    public void setItems(List<CartItem> items) {
        mItems = items;
    }

    public boolean hasItem() {
        return (!mItems.isEmpty());
    }
}
