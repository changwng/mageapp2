package com.example.foo.mageapp.form;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/30/17.
 */

public class FormFieldValue implements DropDownItem {
    protected String mRelation;
    protected String mLabel;
    protected String mValue;
    protected List<FormFieldValue> mItems = new ArrayList<>();

    public String getRelation() {
        return mRelation;
    }

    public void setRelation(String relation) {
        mRelation = relation;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public List<FormFieldValue> getItems() {
        return mItems;
    }

    public void setItems(List<FormFieldValue> items) {
        mItems = items;
    }

    public String getItemLabel() {
        return mLabel;
    }
}
