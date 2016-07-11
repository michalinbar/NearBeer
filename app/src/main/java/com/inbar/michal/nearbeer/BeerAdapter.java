package com.inbar.michal.nearbeer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Michal on 11/07/2016.
 */
public class BeerAdapter extends ArrayAdapter<Beer> {

        public BeerAdapter(Context context, ArrayList<Beer> beers) {
            super(context, 0, beers);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Beer beer = getItem(position);

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.beer_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.beer_item_name);
                viewHolder.style = (TextView) convertView.findViewById(R.id.beer_item_style);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.beer_item_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(beer.name);
            viewHolder.style.setText(beer.style);
            // TODO: 11/07/2016 download image on separate thread
            //viewHolder.image.setImageURI();

            return convertView;
        }

    private static class ViewHolder {

        TextView name;
        TextView style;
        ImageView image;
    }

}

