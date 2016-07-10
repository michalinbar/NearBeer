package com.inbar.michal.nearbeer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.zip.Inflater;

/**
 * Created by Michal on 05/07/2016.
 */
public class BeerListFragment extends Fragment {

    ArrayAdapter<String> beerListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
      //  beerListAdapter = new ArrayAdapter<String>(getContext(), R.layout.fragment_beer_list,
        //        R.id.item_textview, ) {
        beerListAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_textview,
                        R.id.item_textview);


        View rootView = inflater.inflate(R.layout.fragment_beer_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_nearby);
        listView.setAdapter(beerListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), BeerActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, item);
                startActivity(intent);
            }
        });
        return rootView;
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

    @Override
    public void onStart() {
        super.onStart();
        getBeerList();
    }

    private void getBeerList() {
        new AsyncDataFetcher().execute();
    }


    public class AsyncDataFetcher extends AsyncTask {

        private final String LOG_TAG = AsyncDataFetcher.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(Object[] params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

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
                jsonStr = buffer.toString();
                Log.v("returned json: ", jsonStr);
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

            try {
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(getDataFromJson(jsonStr)));
                return list;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getDataFromJson(String jsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String BEER_DATA = "data";
            final String BEER_NAME = "nameDisplay";
            final String BEER_ABV = "abv";
            final String BEER_IBU = "ibu";
            final String BEER_STYLE = "style";
            final String BEER_STYLE_SHORT_NAME = "shortName";
            final String BEER_LABELS = "labels";
            final String BEER_ICON = "icon";
            final String STRING_FALLBACK = "N/A";

            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray beerArray = jsonObj.getJSONArray(BEER_DATA);

            String[] resultStrs = new String[beerArray.length()];
            for(int i = 0; i < beerArray.length(); i++) {
                // Get the JSON object representing a beer
                JSONObject beerInfo = beerArray.getJSONObject(i);
                String name = beerInfo.optString(BEER_NAME, STRING_FALLBACK);
                String abv = beerInfo.optString(BEER_ABV, STRING_FALLBACK);
                String ibu = beerInfo.optString(BEER_IBU, STRING_FALLBACK);
                String style = beerInfo.getJSONObject(BEER_STYLE).optString(BEER_STYLE_SHORT_NAME, STRING_FALLBACK);
                String icon = "";
                if (beerInfo.has(BEER_LABELS)) {
                    JSONObject labelsObject = beerInfo.getJSONObject(BEER_LABELS);
                    icon = labelsObject.optString(BEER_ICON, STRING_FALLBACK);
                }
                resultStrs[i] = name+"\nStyle: "+ style+ " ABV:"+abv+"% ibu:"+ibu+" icon: "+icon;
                Log.v(LOG_TAG, resultStrs[i]);
            }

            return resultStrs;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result != null) {
                ArrayList array = (ArrayList)result;
                beerListAdapter.clear();
                for (int i = 0; i < array.size(); i++) {
                    beerListAdapter.add((String) array.get(i));
                }
            }
        }
    }


}
