package com.inbar.michal.nearbeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Michal on 02/07/2016.
 */
public class BeerActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer);
        if (savedInstanceState == null) {

        }
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String beer = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) findViewById(R.id.beer_textView)).setText(beer);
        }


    }
}

