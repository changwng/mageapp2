package com.example.foo.mageapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foo.mageapp.form.Form;
import com.example.foo.mageapp.xmlconnect.CheckoutBillingConnect;
import com.example.foo.mageapp.xmlconnect.ResponseMessage;

import java.util.HashMap;

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
