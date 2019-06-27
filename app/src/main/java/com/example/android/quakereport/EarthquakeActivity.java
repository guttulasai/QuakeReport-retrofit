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
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.quakereport.api.model.Example;
import com.example.android.quakereport.api.model.Feature;
import com.example.android.quakereport.api.service.Api;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** Adapter for the list of earthquakes */
    private EarthquakeAdapter mAdapter;

    // Find a reference to the {@link ListView} in the layou
    private ListView earthquakeListView;


    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&" +
                    "minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        final ArrayList<Feature> repos = new ArrayList<>();

        earthquakeListView = findViewById(R.id.list);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new  OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        Api api=retrofit.create(Api.class);

        api.getEarthquakeData("geojson","time",6,10).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Example example = response.body();
              //  showEarthquakes(example.getFeatures());
                mAdapter = new EarthquakeAdapter(getApplicationContext(), example.getFeatures());
                earthquakeListView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

      // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Feature currentEarthquake = mAdapter.getItem(position);

                Log.d("hi!",currentEarthquake.getProperties().getPlace()+currentEarthquake.getProperties().getUrl());



                // Convert the String URL into a URI object (to pass into the Intent constructor)
               Uri earthquakeUri = Uri.parse(currentEarthquake.getProperties().getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

}
