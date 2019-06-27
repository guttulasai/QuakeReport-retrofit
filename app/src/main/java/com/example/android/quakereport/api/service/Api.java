package com.example.android.quakereport.api.service;

import com.example.android.quakereport.api.model.Earthquake;
import com.example.android.quakereport.api.model.Example;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    String BaseUrl="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

//    @GET("/users.json")
//    void contacts(Callback<Contacts> cb);

    //@GET("/users.json")
    //void contacts(Callback<List<User>> cb);

    @GET("/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10")
    Call<Example> getEarthquakeData(@Query("format") String format,
                                       @Query("orderby") String orderby,
                                       @Query("minmag") int minmag,
                                       @Query("limit") int limit);

}
