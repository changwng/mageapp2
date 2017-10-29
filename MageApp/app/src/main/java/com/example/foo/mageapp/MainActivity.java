package com.example.foo.mageapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";

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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Stopping the app..");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}