package com.inbar.michal.nearbeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.ShareActionProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

/**
 * Created by Michal on 02/07/2016.
 */
public class BeerActivity extends AppCompatActivity {

    private final String LOG_TAG = BeerActivity.class.getSimpleName();


    private String beerInfo = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            beerInfo = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) findViewById(R.id.beer_textView)).setText(beerInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beer_activity_menu, menu);
        MenuItem menuShare = menu.findItem(R.id.menu_share);
        ShareActionProvider provider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuShare);

        if (provider != null) {
            provider.setShareIntent(createShareIntent());
        } else {
            Log.e(LOG_TAG, "Share Action Provider is null");
        }
        return true;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");;
        shareIntent.putExtra(Intent.EXTRA_TEXT, beerInfo);
        return shareIntent;
    }



}

