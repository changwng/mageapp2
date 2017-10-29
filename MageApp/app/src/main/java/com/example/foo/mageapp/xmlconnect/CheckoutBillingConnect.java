package com.example.foo.mageapp.xmlconnect;

import android.content.Context;

import com.example.foo.mageapp.form.Form;

import java.util.Map;

/**
 * Created by foo on 10/7/17.
 */

public class CheckoutBillingConnect extends CheckoutAddressConnect {

    public CheckoutBillingConnect(Context c) {
        super(c);
    }

    public Form fetchBillingForm() {
        mPath = "xmlconnect/checkout/newBillingAddressForm";
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseFormByXml(resp);
    }

    public ResponseMessage saveBilling(Map<String, String> postData) {
        mPath = "xmlconnect/checkout/saveBillingAddress";
        mPostData = postData;
        String url = this.getRequestUrl();
        String resp = getContentByUrl(url);
        return this.parseResponseMessage(resp);
    }
}