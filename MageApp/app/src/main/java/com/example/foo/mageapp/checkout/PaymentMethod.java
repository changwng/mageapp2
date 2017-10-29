package com.example.foo.mageapp.checkout;

import com.example.foo.mageapp.form.Form;

/**
 * Created by foo on 10/14/17.
 */

public class PaymentMethod {

    private static final PaymentMethod ourInstance = new PaymentMethod();

    protected String mId;
    protected String mCode;
    protected String mPostName;
    protected String mLabel;
    protected Form mForm;

    public static PaymentMethod getInstance() {
        return ourInstance;
    }

    /*private PaymentMethod() {
    }*/

    public static PaymentMethod getOurInstance() {
        return ourInstance;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getPostName() {
        return mPostName;
    }

    public void setPostName(String postName) {
        mPostName = postName;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public Form getForm() {
        return mForm;
    }

    public void setForm(Form form) {
        mForm = form;
    }
}
