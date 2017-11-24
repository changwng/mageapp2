package com.example.foo.mageapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.foo.mageapp.form.Form;
import com.example.foo.mageapp.form.FormField;
import com.example.foo.mageapp.form.FormFieldValue;
import com.example.foo.mageapp.helper.Contact;
import com.example.foo.mageapp.xmlconnect.CheckoutBillingConnect;
import com.example.foo.mageapp.xmlconnect.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutBillingFragment extends CheckoutAddressFragment {

    protected static final int REQUEST_PERMISSION_READ_CONTACTS = 0;

    public CheckoutBillingFragment() {
        // Required empty public constructor
        mPostData = new HashMap<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        new BillingTask().execute();
        this.getPrimaryContact();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_billing, container, false);
        mAddressForm = v.findViewById(R.id.form_billing);
        mBtSave = v.findViewById(R.id.bt_save_billing);
        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFormValid = true;
                prepareFormPostData();
                if (mIsFormValid) {
                    new BillingSaveTask().execute();
                }
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION_READ_CONTACTS) return;
        if (grantResults.length == 0) return;
        int result = grantResults[0];
        switch (result) {
            case PackageManager.PERMISSION_GRANTED:
                this.fetchPrimaryContact();
                break;
        }
    }

    public static Fragment newInstance() {
        return new CheckoutBillingFragment();
    }

    protected void saveFormAfter() {
        if (mFormSaveRespMsg.isSuccess()) {
            Intent activity = new Intent(getContext(), CheckoutShippingActivity.class);
            startActivity(activity);
        } else {
            Toast.makeText(getContext(), mFormSaveRespMsg.getText(), Toast.LENGTH_LONG).show();
        }
    }

    protected void handlePermission() {
        int permission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.READ_CONTACTS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                    Manifest.permission.READ_CONTACTS
            }, REQUEST_PERMISSION_READ_CONTACTS);
        } else {
            this.fetchPrimaryContact();
        }
    }

    protected void getPrimaryContact() {
        this.handlePermission();
    }

    protected void fetchPrimaryContact() {
        Contact contact = Contact.getInstance(getContext());
        Map<String, String> info = contact.getOwnerAddressInfo();
        if (info != null) {
            updateForm(info);
        }
        contact.setOnAddressUpdateListener(new Contact.OnAddressUpdateListener() {
            @Override
            public void onAddressUpdated(Map<String, String> data) {
                updateForm(data);
            }
        });
    }

    protected void updateForm(Map<String, String> data) {
        if (data == null) return;
        if (mAddressForm == null) return;
        int childCnt = mAddressForm.getChildCount();
        for (int i = 0; i < childCnt; i++) {
            View child = mAddressForm.getChildAt(i);
            FormField field = (FormField) child.getTag();
            if (field != null) {
                String key = field.getId();
                Log.d(TAG, "key: " + key);
                if (data.containsKey(key) && !TextUtils.isEmpty(data.get(key))) {
                    String val = data.get(key);
                    if (child instanceof EditText) {
                        ((EditText) child).setText(val);
                    } else if (child instanceof Spinner) {
                        Spinner view = ((Spinner) child);
                        SpinnerAdapter adapter = view.getAdapter();
                        int cnt = adapter.getCount();
                        for (int j = 0; j < cnt; j++) {
                            FormFieldValue item = (FormFieldValue) adapter.getItem(j);
                            if (item.getValue().equals(val)) {
                               view.setSelection(j);
                            }
                        }
                    }
                }
            }
        }
    }

    private class BillingTask extends AsyncTask<Void, Void, Form> {
        @Override
        protected Form doInBackground(Void... params) {
            Form form = new CheckoutBillingConnect(getContext()).fetchBillingForm();
            return form;
        }

        @Override
        protected void onPostExecute(Form result) {
            mForm = result;
            updateUI();
        }
    }

    private class BillingSaveTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... var1) {
            ResponseMessage msg = new CheckoutBillingConnect(getContext()).saveBilling(mPostData);
            return msg;
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mFormSaveRespMsg = result;
            saveFormAfter();
        }
    }
}