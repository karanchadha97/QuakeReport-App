package com.example.android.quakereport;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public final class QueryUtils {

    /** Sample JSON response for a USGS query */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }

        public static URL createUrl(String stringUrl)
        {
            URL url=null;
            try {
                url = new URL(stringUrl);
            }
            catch(MalformedURLException e)
            {
                Log.e(LOG_TAG,"Error With Creating URL",e);
                return null;
            }
            return url;
        }
        public static String makeHTTPRequest(URL url) throws IOException
        {
            String jsonResponse="";
            HttpURLConnection urlConnection=null;
            InputStream inputStream=null;
            try {
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if(urlConnection.getResponseCode()==200)
                {
                    inputStream=urlConnection.getInputStream();
                    jsonResponse=readFromInputStream(inputStream);
                }
                else
                {
                    Log.e(LOG_TAG,"Incorrect Response Code"+urlConnection.getResponseCode());
                }
              }
            catch(IOException e)
            {
                Log.e(LOG_TAG,"Problem Retrieving Earthquake Data",e);
            }
            finally
            {
                if(urlConnection!=null)
                    urlConnection.disconnect();
                if(inputStream!=null)
                    inputStream.close();
            }
            return jsonResponse;
        }
        public static String readFromInputStream(InputStream inputStream)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader reader=new BufferedReader(inputStreamReader);
            StringBuilder output=new StringBuilder();
            String line= null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(line!=null)
            {
                output.append(line);
                try {
                    line=reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return output.toString();
        }
        public static ArrayList<Earthquake> extractFeatureFromJson(String jsonResponse)
        {
            if(TextUtils.isEmpty(jsonResponse))
            {
                return null;
            }
            final ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray featureArray = jsonObject.optJSONArray("features");
                for (int i = 0; i < featureArray.length(); i++) {
                    JSONObject obj1 = featureArray.getJSONObject(i);
                    JSONObject obj2 = obj1.getJSONObject("properties");
                    Double mag = obj2.getDouble("mag");
                    String place = obj2.getString("place");
                    Long time = obj2.getLong("time");
                    String url = obj2.getString("url");
                    earthquakes.add(new Earthquake(mag,place,time,url));
                }
                return earthquakes;
            }
            catch(JSONException e)
            {
                Log.e(LOG_TAG,"Problem Parsing the Earthquake JSON Results",e);
                return null;
            }
        }

    }

