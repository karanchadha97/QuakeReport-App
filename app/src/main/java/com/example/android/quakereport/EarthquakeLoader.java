package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.quakereport.Earthquake;
import com.example.android.quakereport.QueryUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public ArrayList<Earthquake> loadInBackground() {
        URL url=null;
        if (mUrl == null) {
            return null;
        }
        url = QueryUtils.createUrl(mUrl);
        try {
            String jsonResponse = QueryUtils.makeHTTPRequest(url);
            ArrayList<Earthquake> list = QueryUtils.extractFeatureFromJson(jsonResponse);
            return list;
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"Error in Networking",e);
            return null;
        }
    }

}
