package com.example.foo.mageapp.form;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class FormField {

    public static final String TYPE_CHECKBOX = "checkbox";
    public static final String TYPE_SELECT = "select";
    public static final String TYPE_TEXT = "text";

    protected String mId;
    protected String mName;
    protected String mLabel;
    protected String mType;
    protected String mRequired;
    protected List<FormFieldValidator> mValidators = new ArrayList<>();
    protected List<FormFieldValue> mValues = new ArrayList<>();

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

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getRequired() {
        return mRequired;
    }

    public void setRequired(String required) {
        mRequired = required;
    }

    public boolean isRequred() {
        return ((mRequired != null) && mRequired.equals("true"));
    }

    public List<FormFieldValidator> getValidators() {
        return mValidators;
    }

    public void setValidators(List<FormFieldValidator> validators) {
        mValidators = validators;
    }

    public List<FormFieldValue> getValues() {
        return mValues;
    }

    public void setValues(List<FormFieldValue> values) {
        mValues = values;
    }
}
