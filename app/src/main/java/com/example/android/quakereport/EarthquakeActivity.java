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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private EarthquakeAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    TextView emptyView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);
;
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Earthquake currentEarthquake = adapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent);
            }
        });
       /* AsyncQuakeReport task=new AsyncQuakeReport();
        task.execute(USGS_REQUEST_URL);*/
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&  activeNetwork.isConnectedOrConnecting();

       if(isConnected==true){
        LoaderManager loader = getLoaderManager();
        loader.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        emptyView=(TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);}
        else
        {
            progressBar=(ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            emptyView=(TextView) findViewById(R.id.empty_view);
            earthquakeListView.setEmptyView(emptyView);
            emptyView.setText("No Internet Connection");
        }
    }


    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {
        adapter.clear();
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
        else
        {
            emptyView.setText("No Eartquake Data Found");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        adapter.clear();
    }
}
   /* private class AsyncQuakeReport extends AsyncTask<String,Void,ArrayList<Earthquake>> {

       @Override
       protected ArrayList<Earthquake> doInBackground(String... urls)
       {
           if(urls.length<1 || urls[0]==null)
               return null;
           URL url = QueryUtils.createUrl(urls[0]);
           String jsonResponse = "";
           try {
               jsonResponse = QueryUtils.makeHTTPRequest(url);
               ArrayList<Earthquake> list = QueryUtils.extractFeatureFromJson(jsonResponse);
               return list;
           } catch (IOException e) {
               Log.e(LOG_TAG, "Networking Failed", e);
               return null;
           }
       }

       @Override
       protected void onPostExecute(ArrayList<Earthquake> list) {
           adapter.clear();
           if (list != null && !list.isEmpty()) {
               adapter.addAll(list);
           }
       }
   }

}*/
