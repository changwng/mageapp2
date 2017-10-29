package com.example.foo.mageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.foo.mageapp.checkout.ShippingMethod;
import com.example.foo.mageapp.checkout.ShippingMethodRate;
import com.example.foo.mageapp.xmlconnect.CheckoutShippingMethodConnect;
import com.example.foo.mageapp.xmlconnect.ResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutShippingMethodFragment extends Fragment {

    protected static final String TAG = CheckoutShippingMethodFragment.class.getSimpleName();
    protected List<ShippingMethod> mShippingMethods = new ArrayList<>();
    protected LinearLayout mRadioGroup;
    protected Context mContext;
    protected Map<String, String> mPostData = new HashMap<>();
    protected ResponseMessage mRespMsg;
    protected Button mBtNext;

    public CheckoutShippingMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        mContext = getContext();
        new FetchMethodsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout_shipping_method, container, false);
        mRadioGroup = v.findViewById(R.id.checkout_shipping_method_radio_group);
        mBtNext = v.findViewById(R.id.bt_checkout_shipping_method_continue);
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveMethodTask().execute();
            }
        });
        return v;
    }

    public static Fragment newInstance() {
        Fragment f = new CheckoutShippingMethodFragment();
        return f;
    }

    protected void updateUI() {
        if (!mShippingMethods.isEmpty()) {
            for (ShippingMethod method : mShippingMethods) {
                if (!method.getRages().isEmpty()) {
                    for (ShippingMethodRate rate : method.getRages()) {
                        RadioButton btn = new RadioButton(mContext);
                        btn.setTag(rate);
                        btn.setText(rate.getRadioButtonLabel());
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String rateCode = ((ShippingMethodRate) v.getTag()).getCode();
                                mPostData.put("shipping_method", rateCode);
                                mBtNext.setEnabled(true);
                            }
                        });
                        mRadioGroup.addView(btn);
                    }
                }
            }
        }
    }

    protected void handleSaveMethodAfter() {
        String msg = mRespMsg.getText();
        Resources res = mContext.getResources();
        String title = res.getString(R.string.alert);
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mRespMsg.isSuccess()) {
                            Intent activity = CheckoutPaymentMethodActivity.newIntent(mContext);
                            startActivity(activity);
                        }
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private class FetchMethodsTask extends AsyncTask<Void, Void, List> {
        @Override
        protected List doInBackground(Void... params) {
            List<ShippingMethod> methods = new CheckoutShippingMethodConnect(mContext)
                    .fetchShippingMethods();
            return methods;
        }

        @Override
        protected void onPostExecute(List result) {
            mShippingMethods = result;
            updateUI();
        }
    }

    private class SaveMethodTask extends AsyncTask<Void, Void, ResponseMessage> {
        @Override
        protected ResponseMessage doInBackground(Void... params) {
            ResponseMessage msg = new CheckoutShippingMethodConnect(mContext)
                    .saveShippingMethod(mPostData);
            return msg;
        }

        @Override
        protected void onPostExecute(ResponseMessage result) {
            mRespMsg = result;
            handleSaveMethodAfter();
        }
    }
}