package com.example.andyk.mageapp;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andyk.mageapp.form.Form;
import com.example.andyk.mageapp.xmlconnect.CheckoutBillingConnect;
import com.example.andyk.mageapp.xmlconnect.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutBillingFragment extends CheckoutAddressFragment {

    public CheckoutBillingFragment() {
        // Required empty public constructor
        mPostData = new HashMap<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        new BillingTask().execute();
        this.fetchContact();
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

    protected void fetchContact() {
        this.checkPermission();
    }

    @Override
    protected void fetchAddress() {
        AccountManager am = (AccountManager) this.getContext().getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = am.getAccountsByType("com.google");
        int numAccounts = accounts.length;
        if (numAccounts < 1)  return;;
        Account account = accounts[0];
        String email = account.name;

        // get CONTACT_ID by email
        Cursor cursor = this.getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Email.ADDRESS
                },
                ContactsContract.CommonDataKinds.Email.ADDRESS + " = ?",
                new String[]{email},
                null
        );
        int cnt = cursor.getCount();
        if (cnt < 1) return;

        cursor.moveToFirst();
        int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
        String contactId = cursor.getString(index);
        cursor.close();

        cursor = this.getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET
                },
                ContactsContract.Data.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        cnt = cursor.getCount();
        if (cnt < 1) return;
        cursor.moveToFirst();

        String[] names = cursor.getColumnNames();
        int numNames = names.length;
        for (int i = 0; i < numNames; i++) {
            String name = cursor.getColumnName(i);
            String val = null;
            int type = cursor.getType(i);
            switch (type) {
                case Cursor.FIELD_TYPE_BLOB:
                    byte[] bytes = cursor.getBlob(i);
                    val = String.valueOf(bytes);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    float f = cursor.getFloat(i);
                    val = String.valueOf(f);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    int n = cursor.getInt(i);
                    val = String.valueOf(n);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    val = cursor.getString(i);
                    break;
            }
            mAddrData.put(name, val);
        }
        cursor.close();
        return;
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
