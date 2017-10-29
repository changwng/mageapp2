package com.example.foo.mageapp.xmlconnect;

import android.content.Context;

import com.example.foo.mageapp.form.Form;

import java.util.Map;

/**
 * Created by foo on 10/7/17.
 */

public class CheckoutShippingConnect extends CheckoutAddressConnect {

    public CheckoutShippingConnect(Context c) {
        super(c);
    }

    public Form fetchShippingForm() {
        mPath = "xmlconnect/checkout/newShippingAddressForm";
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseFormByXml(resp);
    }

    public ResponseMessage saveShipping(Map<String, String> postData) {
        mPath = "xmlconnect/checkout/saveShippingAddress";
        mPostData = postData;
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseResponseMessage(resp);
    }
}
