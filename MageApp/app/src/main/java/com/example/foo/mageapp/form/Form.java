package com.example.foo.mageapp.form;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class Form {
    protected String mId;
    protected String mName;
    protected String mAction;
    protected String mMethod;
    protected List<FormField> mFields = new ArrayList<>();

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public List<FormField> getFields() {
        return mFields;
    }

    public void setFields(List<FormField> fields) {
        mFields = fields;
    }

    public void addField(FormField field) {
        mFields.add(field);
    }
}
