package com.example.android.quakereport;


/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.api.model.Earthquake;
import com.example.android.quakereport.api.model.Example;
import com.example.android.quakereport.api.model.Feature;
import com.example.android.quakereport.api.model.Properties;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

/*
 * {@link AndroidFlavorAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link AndroidFlavor} objects.
 * */
public class EarthquakeAdapter extends ArrayAdapter<Feature>  {


    String url;


    public EarthquakeAdapter(Context context,List<Feature> earthquakes) {
        super(context,0,earthquakes);
    }




//    private static final String LOG_TAG = EarthquakeAdapter.class.getSimpleName();
//
//    List<Feature> features;
//
   private static final String LOCATION_SEPARATOR = " of ";
//
//    /**
//     * This is our own custom constructor (it doesn't mirror a superclass constructor).
//     * The context is used to inflate the layout file, and the list is the data we want
//     * to populate into the lists.
//     *
//     * @param context        The current context. Used to inflate the layout file.
//     * @param earthquakes A List of AndroidFlavor objects to display in a list
//     */
//    public EarthquakeAdapter(Activity context, List<Feature> earthquakes) {
//        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
//        // the second argument is used when the ArrayAdapter is populating a single TextView.
//        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
//        // going to use this second argument, so it can be any value. Here, we used 0.
//        super(context,0,earthquakes);
//    }
//
//    /**
//     * Provides a view for an AdapterView (ListView, GridView, etc.)
//     *
//     * @param position The position in the list of data that should be displayed in the
//     *                 list item view.
//     * @param convertView The recycled view to populate.
//     * @param parent The parent ViewGroup that is used for inflation.
//     * @return The View for the position in the AdapterView.
//     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;


        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        Feature currentEarthquake = getItem(position);




        // Get the original location string from the Earthquake object,
        // which can be in the format of "5km N of Cairo, Egypt" or "Pacific-Antarctic Ridge".
        String originalLocation = currentEarthquake.getProperties().getPlace();
 //       System.out.println("Location="+originalLocation);
        Log.d("Location","fhgdfghdfghdfghfdghdfgjfghjfghj");

       url = currentEarthquake.getProperties().getUrl();

        System.out.print(url);


        String primaryLocation;

        String locationOffset;

        // Find the TextView with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getProperties().getMag());

        // Display the magnitude of the current earthquake in that TextView
        magnitudeView.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getProperties().getMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }


        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getProperties().getTime());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        String url =  currentEarthquake.getProperties().getUrl();

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }


    private int getMagnitudeColor(double magnitude){


        int magnitudeColorResourceId;
        int magnitudeFloor=(int)Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return  ContextCompat.getColor(getContext(), magnitudeColorResourceId);

        }

//    @Override
//    public void onClick(View v) {
//        Uri uri = Uri.parse(url);
//        Intent i = new Intent(Intent.ACTION_VIEW, uri );
//        startActivity(EarthquakeActivity.class,i,null);
//    }
}