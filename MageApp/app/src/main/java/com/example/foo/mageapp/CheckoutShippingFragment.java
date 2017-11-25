package com.example.foo.mageapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foo.mageapp.form.Form;
import com.example.foo.mageapp.helper.Contact;
import com.example.foo.mageapp.xmlconnect.CheckoutShippingConnect;
import com.example.foo.mageapp.xmlconnect.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutShippingFragment extends CheckoutAddressFragment {

    protected static final int PICK_CONTACT_REQUEST_CODE = 0;

    public CheckoutShippingFragment() {
        // Required empty public constructor
        mPostData = new HashMap<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        new ShippingTask().execute();
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_shipping, container, false);
        mAddressForm = v.findViewById(R.id.form_shipping);
        mBtSave = v.findViewById(R.id.bt_save_shipping);
        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFormValid = true;
                prepareFormPostData();
                if (mIsFormValid) {
                    new ShippingSaveTask().execute();
                }
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.checkout_shipping, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_sync:
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                this.startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode != PICK_CONTACT_REQUEST_CODE) return;
        if (resultCode != Activity.RESULT_OK) return;
        Uri uri = intent.getData();
        Contact contact = Contact.getInstance(this.getContext());
        contact.requestDataByContactUri(uri);
        contact.setOnAddressUpdateListener(new Contact.OnAddressUpdateListener() {
            @Override
            public void onAddressUpdated(Map<String, String> data) {
                populateForm(data);
            }
        });
    }

    public static Fragment newInstance() {
        return new CheckoutShippingFragment();
    }

    protected void saveFormAfter() {
        if (mFormSaveRespMsg.isSuccess()) {
            Intent activity = new Intent(getContext(), CheckoutShippingMethodActivity.class);
            startActivity(activity);
        } else {
            Toast.makeText(getContext(), mFormSaveRespMsg.getText(), Toast.LENGTH_LONG).show();
        }
    }

    private class ShippingTask extends AsyncTask<Void, Void, Form> {
        @Override
        protected Form doInBackground(Void... params) {
            Form form = new CheckoutShippingConnect(getContext()).fetchShippingForm();
            return form;
        }

        @Override
        protected void onPostExecute(Form result) {
            mForm = result;
            updateUI();
        }
    }

    private class ShippingSaveTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... var1) {
            ResponseMessage msg = new CheckoutShippingConnect(getContext()).saveShipping(mPostData);
            return msg;
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mFormSaveRespMsg = result;
            saveFormAfter();
        }
    }
}
