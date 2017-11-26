package com.example.foo.mageapp.helper;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by foo on 11/25/17.
 */

public class RequestParamList extends ArrayList<RequestParam> {

    public boolean containsKey(String key) {
        for (RequestParam param : this) {
            if (param.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasValue(String key) {
        for (RequestParam param : this) {
            if (param.getKey().equals(key)) {
                if (!TextUtils.isEmpty(param.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
