package com.example.foo.mageapp.checkout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 10/12/17.
 */

public class ShippingMethod {
    protected String mLabel;
    protected List<ShippingMethodRate> mRages = new ArrayList<>();

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public List<ShippingMethodRate> getRages() {
        return mRages;
    }

    public void setRages(List<ShippingMethodRate> rages) {
        mRages = rages;
    }
}
