package com.example.foo.mageapp.helper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.StructuredName;
import static android.provider.ContactsContract.Data;

/**
 * Created by foo on 11/23/17.
 */

public class Contact {
    protected static final String TAG = Contact.class.getSimpleName();
    protected static final String OWNER_ACCOUNT_TYPE = "com.google";
    protected static final String GOOGLE_GEOCODE_REQUEST_URL =
            "https://maps.googleapis.com/maps/api/geocode/json?address=%s";
    protected static Contact sContact;
    protected Context mContext;
    protected Map<String, String> mData = new HashMap<>();
    protected String mGoogleGeocodeResult;
    protected OnAddressUpdateListener mAddrUpdateListener;

    public interface OnAddressUpdateListener {
        void onAddressUpdated(Map<String, String> data);
    }

    protected Contact(Context context) {
        mContext = context;
    }

    public static Contact getInstance(Context context) {
        if (sContact == null) {
            sContact = new Contact(context);
        }
        return sContact;
    }

    public void setOnAddressUpdateListener(OnAddressUpdateListener listener) {
        mAddrUpdateListener = listener;
    }

    public Map getCursorRow(Cursor c) {
        Map<String, String> data = new HashMap<>();
        int numCols = c.getColumnCount();
        for (int i = 0; i < numCols; i++) {
            String name = c.getColumnName(i);
            String val = null;
            int type = c.getType(i);
            switch (type) {
                case Cursor.FIELD_TYPE_BLOB:
                    val = String.valueOf(c.getBlob(i));
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    val = String.valueOf(c.getFloat(i));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    val = String.valueOf(c.getInt(i));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    val = c.getString(i);
                    break;
            }
            data.put(name, val);
        }
        return data;
    }

    public String getOwnerEmail() {
        AccountManager am = (AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accts = am.getAccountsByType(OWNER_ACCOUNT_TYPE);
        if (accts.length == 0) return null;
        Account acct = accts[0];
        String email = acct.name;
        return email;
    }

    public Map<String, String> getOwnerContactInfo() {
        if (mData.isEmpty()) {
            String email = this.getOwnerEmail();
            if (email != null) {
                String contactId = null;
                Cursor c = mContext.getContentResolver().query(Email.CONTENT_URI,
                        null, Email.ADDRESS + " = ?", new String[]{email},
                        null);
                if ((c != null) && (c.getCount() > 0)) {
                    c.moveToFirst();
                    contactId = c.getString(c.getColumnIndex(Email.CONTACT_ID));
                    mData.put("contact_id", contactId);
                    mData.put("email", c.getString(c.getColumnIndex(Email.ADDRESS)));
                    c.close();
                }
                c = mContext.getContentResolver().query(Phone.CONTENT_URI,
                        null,
                        Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);
                if ((c != null) && (c.getCount() > 0)) {
                    c.moveToFirst();
                    String phone = c.getString(c.getColumnIndex(Phone.NUMBER));
                    mData.put("telephone", phone);
                    c.close();
                }
                c = mContext.getContentResolver().query(Data.CONTENT_URI,
                        null,
                        Data.MIMETYPE + " = ? AND " + StructuredName.CONTACT_ID + " = ?",
                        new String[]{StructuredName.CONTENT_ITEM_TYPE, contactId},
                        null);
                if ((c != null) && (c.getCount() > 0)) {
                    c.moveToFirst();
                    String fname = c.getString(c.getColumnIndex(StructuredName.GIVEN_NAME));
                    String lname = c.getString(c.getColumnIndex(StructuredName.FAMILY_NAME));
                    mData.put("firstname", fname);
                    mData.put("lastname", lname);
                    c.close();
                }
            }
        }
        return mData;
    }

    public String getOwnerContactId() {
        Map<String, String> info = this.getOwnerContactInfo();
        if (info.isEmpty()) return null;
        return info.get("contact_id");
    }

    public Map<String, String> getOwnerAddressInfo() {
        if (mData.get("postcode") == null) {
            String contactId = this.getOwnerContactId();
            if (contactId != null) {
                Cursor c = mContext.getContentResolver().query(
                        StructuredPostal.CONTENT_URI,
                        new String[]{
                                StructuredPostal.CITY,
                                StructuredPostal.COUNTRY,
                                StructuredPostal.FORMATTED_ADDRESS,
                                StructuredPostal.POSTCODE,
                                StructuredPostal.REGION,
                                StructuredPostal.STREET
                        },
                        StructuredPostal.CONTACT_ID + " = ?",
                        new String[]{contactId}, null);
                if (c != null) {
                    int cnt = c.getCount();
                    if (cnt > 0) {
                        c.moveToFirst();
                        String city = c.getString(c.getColumnIndex(StructuredPostal.CITY));
                        String country = c.getString(c.getColumnIndex(StructuredPostal.COUNTRY));
                        String formattedAddr = c.getString(c.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
                        String postcode = c.getString(c.getColumnIndex(StructuredPostal.POSTCODE));
                        String region = c.getString(c.getColumnIndex(StructuredPostal.REGION));
                        String street = c.getString(c.getColumnIndex(StructuredPostal.STREET));
                        mData.put("city", city);
                        mData.put("country", country);
                        mData.put("formatted_address", formattedAddr);
                        mData.put("postcode", postcode);
                        mData.put("region", region);
                        mData.put("street", street);
                    }
                    c.close();
                }
                if ((mData.get("city") == null) && (mData.get("country") == null)
                        && (mData.get("postcode") == null) && (mData.get("region") == null)) {
                    if (mData.get("formatted_address") != null) {
                        this.getAddressesFromLocationName(mData.get("formatted_address"));
                    } else if (mData.get("street") != null) {
                        this.getAddressesFromLocationName(mData.get("street"));
                    }
                }
            }
        }
        return mData;
    }

    protected List getAddressesFromLocationName(String addr) {
        List<Address> addrs = new ArrayList();
        Geocoder geo = new Geocoder(mContext, Locale.getDefault());
        if (geo.isPresent()) {
            try {
                addrs = geo.getFromLocationName(addr, 10);
                if (addrs.isEmpty()) {
                    new LocationTask().execute(addr);
                } else {
                    this.parseAddressList(addrs);
                }
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage(), ioe);
            }
        }
        return addrs;
    }

    protected void updateContactAddress() {
        Map<String, String> data = new HashMap<>();
        String json = mGoogleGeocodeResult;
        try {
            JSONObject jo = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray results = jo.getJSONArray("results");
            if (results.length() > 0) {
                int numResults = results.length();
                for (int i = 0; i < numResults; i++) {
                    JSONObject result = (JSONObject) results.get(i);
                    JSONArray components = result.getJSONArray("address_components");
                    int numComponents = components.length();
                    for (int j = 0; j < numComponents; j++) {
                        JSONObject component = (JSONObject) components.get(j);
                        String longName = component.getString("long_name");
                        String shortName = component.getString("short_name");
                        JSONArray types = component.getJSONArray("types");
                        int numTypes = types.length();
                        for (int k = 0; k < numTypes; k++) {
                            String type = (String) types.get(k);
                            switch (type) {
                                case "subpremise":
                                    mData.put("street_2", longName);
                                    break;
                                case "administrative_area_level_1":
                                    // we don't need region if region_code is set
                                    if (TextUtils.isEmpty(shortName)) {
                                        mData.put("region", longName);
                                    }
                                    mData.put("region_code", shortName);
                                    break;
                            }
                        }
                    }
                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    Geocoder geo = new Geocoder(mContext);
                    List<Address> addrs = geo.getFromLocation(lat, lng, 1);
                    this.parseAddressList(addrs);
                }
                mAddrUpdateListener.onAddressUpdated(mData);
            }
        } catch (JSONException je) {
            Log.e(TAG, je.getMessage(), je);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
    }

    protected void parseAddressList(List<Address> addrs) {
        for (Address addr : addrs) {
            String street = addr.getAddressLine(0);
            String countryCode = addr.getCountryCode();
            String countryName = addr.getCountryName();
            String city = (addr.getLocality() != null) ? addr.getLocality() : addr.getSubLocality();
            String postcode = addr.getPostalCode();
            mData.put("street", street);
            mData.put("country_code", countryCode);
            mData.put("country", countryName);
            mData.put("country_id", countryCode);
            mData.put("city", city);
            mData.put("postcode", postcode);

            // get street_2 from formatted_address field
            String formattedAddr = mData.get("formatted_address");
            // use REGEX to remove anything but alphanumeric
            String street2 = formattedAddr.replaceAll("[^a-zA-Z0-9#\\s]", "");
            String[] patterns = new String[]{
                    city, countryCode, countryName, postcode, mData.get("region"),
                    mData.get("region_code"), street
            };
            String pattern = TextUtils.join("|", patterns);
            street2 = street2.replaceAll(pattern, "");
            street2 = street2.trim();
            mData.put("street_2", street2);
        }
    }

    private class LocationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String addr = params[0];
            String url = String.format(GOOGLE_GEOCODE_REQUEST_URL, addr);
            byte[] bytes = Helper.getUrlBytes(url);
            String result = new String(bytes);
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            mGoogleGeocodeResult = result;
            updateContactAddress();
        }
    }
}
