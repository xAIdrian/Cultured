package com.androidtitan.hotspots.Provider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Data.Venue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amohnacs on 8/25/15.
 */

/**
 * Gets the list of Venues nearcy and executes
 */
public class FoursquareHandler {
    private static String TAG = "FoursquareHandler";

    DatabaseHelper databaseHelper;

    public static final String CLIENT_ID = "CE1BU4JLX2UL5ESYMNKRO14QBOMDCKXONG55XUCBX1MEAPRW";
    public static final String CLIENT_SECRET = "NQ01XNIA4CM0WID0QWML5LSSCU1UU4QDUBFVGUZHNIESGEVT";

    private Context context;

    private String latitude;
    private String longitude;
    private int location_id;
    private LocationBundle locationHandle;


    public FoursquareHandler(Context context, double latitude, double longitude, int location_id){
        this.context=context;
        databaseHelper = DatabaseHelper.getInstance(context);
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
        this.location_id = location_id;
        locationHandle = databaseHelper.getAllLocations().get(location_id);

        new foursquare().execute();
    }

    public class foursquare extends AsyncTask<View, Void, String> {

        String tempString;


        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            tempString = makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
                    + "&v=20130815&ll=" + latitude + "," + longitude);

            Log.e(TAG, tempString);
            return "";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            if (tempString == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right
                new foursquareVenueFetch().execute(tempString);

                Log.e(TAG, "size: " + databaseHelper.getAllVenuesFromLocation(locationHandle));

            }
        }

    }

    public class foursquareVenueFetch extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            parseFoursquare(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //now we are getting the rating for each...
            for(Venue freshVenue : databaseHelper.getAllVenuesFromLocation(locationHandle)) {
                new FoursquareVenueHandler(context, freshVenue.getId());
            }
        }
    }

    //todo: we have two of these methods
    //todo:     can we create a "parent" class that our Venues extend
    public static String makeCall(String passedURL) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(passedURL);
        String replyString = "";

        try {
            URL url = new URL(buffer_string.toString());
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

            InputStream is = httpConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[50]; //create an array of bytes
            int current = 0;

            while ((current = bis.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }

            replyString = new String(buffer.toByteArray());// the result as a string is ready for parsing

        } catch (Exception e) {
            e.printStackTrace();
        }
        // trim the whitespaces
        return replyString.trim();
    }

    public void parseFoursquare(final String response) {

        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Venue poi = new Venue();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).getString("name"));

                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
                                        poi.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
                                    }

                                    ////////////////////////////
                                    //VENUE_ID
                                    //we will use this for another URI query and get more detailed information on the venue!!!
                                    if (jsonArray.getJSONObject(i).has("id")) {
                                        //Log.e(TAG, "realVenueId: " + jsonArray.getJSONObject(i).getString("id"));
                                        poi.setVenueId(jsonArray.getJSONObject(i).getString("id"));
                                    }

                                    //////////////////////////
                                    if (jsonArray.getJSONObject(i).has("categories")) {
                                        if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
                                                poi.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                                            }
                                        }
                                    }

                                    databaseHelper.createVenue(poi, location_id);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setJSONvenueRating(Venue poi) {


    }
}

