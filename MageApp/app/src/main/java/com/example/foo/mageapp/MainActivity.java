package com.example.foo.mageapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.foo.mageapp.cart.CartInfo;
import com.example.foo.mageapp.helper.SharedPref;
import com.example.foo.mageapp.xmlconnect.CartConnect;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";
    protected static int sVisited;
    protected CartInfo mCartInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Test().checkForLoop();

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            f = MainFragment.getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Starting the app..");
        Log.d(TAG, "Visited: " + sVisited);
        if (sVisited < 1) {
            this.onAppStart();
        }
        sVisited++;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Stopping the app..");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /**
     * gets called once after app started
     */
    protected void onAppStart() {
//        Intent service = InitService.newIntent(this);
//        startService(service);

        // update mini cart
        new CartInfoTask().execute();
    }

    protected void updateMiniCart() {
        String qty = mCartInfo.getSummaryQty();
        SharedPref.putCartitesmQty(this, qty);
        this.invalidateOptionsMenu();
    }

    private class CartInfoTask extends AsyncTask<Void, Void, CartInfo> {
        @Override
        protected CartInfo doInBackground(Void... params) {
            CartInfo info = new CartConnect(MainActivity.this).fetchCartInfo();
            return info;
        }

        @Override
        protected void onPostExecute(CartInfo result) {
            mCartInfo = result;
            updateMiniCart();
        }
    }
}