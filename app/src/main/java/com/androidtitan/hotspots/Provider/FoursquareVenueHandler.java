package com.androidtitan.hotspots.Provider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.Venue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by amohnacs on 8/25/15.
 */
//TODO: let's call this in our fragment
public class FoursquareVenueHandler {
    private static String TAG = "FoursquareVenueHandler";

    DatabaseHelper databaseHelper;

    private Context context;

    //private long venueDBid;
    private String venue_id;

    Venue focusVenue;

    public FoursquareVenueHandler(Context context, long venueDBid){
        this.context=context;
        databaseHelper = DatabaseHelper.getInstance(context);
        focusVenue = databaseHelper.getVenue(venueDBid);
        //this.venueDBid = venueDBid;
        this.venue_id = focusVenue.getVenueId();

        new fourquareVenue().execute();
    }

    public class fourquareVenue extends AsyncTask<View, Void, String> {

        String tempString;



        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            tempString = makeCall("https://api.foursquare.com/v2/venues/" + venue_id +"?client_id="
                    + FoursquareHandler.CLIENT_ID + "&client_secret=" + FoursquareHandler.CLIENT_SECRET
                    + "&v=20130815");

            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (tempString == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right
                parseFoursquare(tempString);
            }
        }

    }


    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
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
                        //Venue poi = new Venue();
                        if (jsonArray.getJSONObject(i).has("name")) {

                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
                                    }

                                    ////////////////////////////
                                    //TODO:
                                    //VENUE_RATING
                                    //we will use this for another URI query and get more detailed information on the venue!!!
                                    if (jsonArray.getJSONObject(i).has("id")) {
                                        //poi.setVenueId(jsonArray.getJSONObject(i).getString("id"));
                                    }
                                    if(jsonArray.getJSONObject(i).has("rating")) {
                                        focusVenue.setRating(Integer.valueOf(jsonArray.getJSONObject(i).getString("rating")));

                                        Log.e(TAG, jsonArray.getJSONObject(i).getString("rating"));
                                        databaseHelper.updateVenue(focusVenue);
                                    }
                                    else {
                                        Log.e(TAG, "No rating");
                                    }
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

}
