package com.example.foo.mageapp.checkout;

import com.example.foo.mageapp.cart.CartTotal;
import com.example.foo.mageapp.sales.QuoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/19/17.
 */

public class OrderReview {
    protected List<QuoteItem> mItems = new ArrayList<>();
    protected List<CartTotal> mTotals = new ArrayList<>();

    public List<QuoteItem> getProducts() {
        return mItems;
    }

    public void setProducts(List<QuoteItem> items) {
        mItems = items;
    }

    public List<CartTotal> getTotals() {
        return mTotals;
    }

    public void setTotals(List<CartTotal> totals) {
        mTotals = totals;
    }
}