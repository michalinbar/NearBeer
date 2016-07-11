package com.inbar.michal.nearbeer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Michal on 11/07/2016.
 */
public class Beer {

    private final String LOG_TAG = getClass().getSimpleName();

    public String name;
    public String abv;
    public String ibu;
    public String style;
    public String icon;

    private static final String BEER_DATA = "data";
    private static final String BEER_NAME = "nameDisplay";
    private static final String BEER_ABV = "abv";
    private static final String BEER_IBU = "ibu";
    private static final String BEER_STYLE = "style";
    private static final String BEER_STYLE_SHORT_NAME = "shortName";
    private static final String BEER_LABELS = "labels";
    private static final String BEER_ICON = "icon";
    private static final String STRING_FALLBACK = "N/A";


    //todo handle exception
    public static Beer fromJSON(JSONObject obj) throws JSONException {

        Beer beer = new Beer();

        beer.name = obj.optString(BEER_NAME, STRING_FALLBACK);
        beer.abv = obj.optString(BEER_ABV, STRING_FALLBACK);
        beer.ibu = obj.optString(BEER_IBU, STRING_FALLBACK);
        beer.style = obj.getJSONObject(BEER_STYLE).optString(BEER_STYLE_SHORT_NAME, STRING_FALLBACK);
        beer.icon = "";

        if (obj.has(BEER_LABELS)) {
            JSONObject labelsObject = obj.getJSONObject(BEER_LABELS);
            beer.icon = labelsObject.optString(BEER_ICON, STRING_FALLBACK);
        }
        return beer;
    }

    //todo handle exception
    public static ArrayList<Beer> fromJSON(JSONArray jsonArray) throws JSONException {

        ArrayList<Beer> beerArray = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            // Get the JSON object representing a beer
            beerArray.add(Beer.fromJSON(jsonArray.getJSONObject(i)));
        }
        return beerArray;
    }

    public String toString(){
        return name +"\n"+style+"\n"+"abv: "+abv+"%"+ "  ibu: "+ibu;
    }


}
