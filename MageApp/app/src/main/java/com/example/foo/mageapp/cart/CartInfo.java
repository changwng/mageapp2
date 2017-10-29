package com.example.foo.mageapp.cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/26/17.
 */

public class CartInfo {
    protected String mIsVirtual;
    protected String mSummaryQty;
    protected String mVirtualQty;
    protected List<CartTotal> mTotals = new ArrayList<>();

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

    public String getVirtualQty() {
        return mVirtualQty;
    }

    public void setVirtualQty(String virtualQty) {
        mVirtualQty = virtualQty;
    }

    public List<CartTotal> getTotals() {
        return mTotals;
    }

    public void setTotals(List<CartTotal> totals) {
        mTotals = totals;
    }
}
