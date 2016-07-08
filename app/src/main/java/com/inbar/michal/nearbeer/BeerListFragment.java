package com.inbar.michal.nearbeer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michal on 05/07/2016.
 */
public class BeerListFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.beer_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getBeerList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBeerList() {

        new AsyncDataFetcher().execute();
    }


    public class AsyncDataFetcher extends AsyncTask {

        private final String LOG_TAG = AsyncDataFetcher.class.getSimpleName();

        @Override
        protected Object doInBackground(Object[] params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                final String BASE_URL =
                        "http://api.brewerydb.com/v2/beers?";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("key", "ac7a853f38efe2216477f97908b55e93")
                        .appendQueryParameter("availableId", "1")
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + url.toString());

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                if (inputStream == null){
                    //// TODO: 06/07/2016
                    Log.e(LOG_TAG, "null input stream");
                    return null;
                }
                InputStreamReader ir = new InputStreamReader(inputStream);
                reader = new BufferedReader(ir);
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0){
                    Log.e(LOG_TAG, "empty result");
                    return null;
                    // TODO: 06/07/2016  
                }
                Log.v("returned json: ", buffer.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG,"" );
                Log.getStackTraceString(e);
            } finally {
                if (reader != null) {
                    connection.disconnect();
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    /*
    //dummy data
    String[] data = {
            "Guinness",
            "Corona",
            "Budwiser",
            "zombie",
            "Duvel",
            "Guinness",
            "Corona",
            "Budwiser",
            "zombie",
            "Duvel",
            "Duvel",
            "Guinness",
            "Corona",
            "Budwiser",
            "zombie",
            "Duvel"
    };

    List<String> nearList = new ArrayList<String>(Arrays.asList(data));

    final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
            R.layout.list_item_textview, nearList);

    ListView listView = (ListView) findViewById(R.id.listview_nearby);
    listView.setAdapter(listAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Toast.makeText(MainActivity.this, "Position=" + position, Toast.LENGTH_SHORT).show();
            String item = listAdapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, BeerActivity.class);
            //  Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, item);
            startActivity(intent);
        }
    });
    */
}
