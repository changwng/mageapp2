package com.example.foo.mageapp.form;

/**
 * Created by foo on 9/30/17.
 */

public class FormFieldValidator {
    protected String mId;
    protected String mType;
    protected String mMessage;
    protected String mRelation;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getRelation() {
        return mRelation;
    }

    public void setRelation(String relation) {
        mRelation = relation;
    }
}
