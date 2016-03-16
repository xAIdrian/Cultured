package com.androidtitan.hotspots.Provider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.Venue;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by amohnacs on 8/25/15.
 */

public class FoursquareVenueHandler {
    private static String TAG = "FoursquareVenueHandler";

    DatabaseHelper databaseHelper;

    //todo: @Inject
    private Context context;

    //private long venueDBid;
    private String venue_id;
    private long venueDBid;

    Venue focusVenue;

    List<Integer> ratingsList;

    //@Inject
    public FoursquareVenueHandler(Context context, long venueDBid) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        this.venueDBid = venueDBid;
        //this.venueDBid = venueDBid;
        ratingsList = new ArrayList<Integer>();

        new foursquareUrlFetch().execute();
    }

    public class foursquareUrlFetch extends AsyncTask<View, Void, String> {

        String tempString;


        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url

            if(confirmConnection(context)) {
                tempString = makeCall("https://api.foursquare.com/v2/venues/" + venue_id + "?client_id="
                        + FoursquareHandler.CLIENT_ID + "&client_secret=" + FoursquareHandler.CLIENT_SECRET
                        + "&v=20130815");

                Log.e(TAG, tempString);
            }

            return "";
        }

        @Override
        protected void onPreExecute() {
            focusVenue = databaseHelper.getVenue(venueDBid);
            venue_id = focusVenue.getVenueId();

        }

        @Override
        protected void onPostExecute(String result) {
            if (tempString == null) {
                // we have an error to the call
                // we can also stop the progress bar
                Log.e(TAG, "ERROR: TEMPSTRING == NULL");
            } else {
                new foursquareVenueFetch().execute(tempString);
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
        }
    }

    /**
     * Constructs the URL that is used for our call to Foursquare
     * @param passedURL
     * @return
     */
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
            JSONObject initialObject = new JSONObject(response);
            if (initialObject.has("response")) {

                JSONObject jsonObject = new JSONObject(response).getJSONObject("response");

                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Log.i("**********", "**********");
                    Log.i("resonse key", key);

                    JSONObject innerJObject = jsonObject.getJSONObject(key);
                    if(innerJObject.has("rating")) {
                        String rating = innerJObject.getString("rating");

                        focusVenue.setRating(Float.parseFloat((rating)));
                        databaseHelper.updateVenue(focusVenue);

                    }
                    else {
                        //Log.e(TAG, "no rating");
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo: we are going to move these away eventually
    public boolean confirmConnection(Context context) {
        ConnectivityManager check = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] networkInfos = new Network[0];
            networkInfos = check.getAllNetworks();

            for(int i = 0; i < networkInfos.length; i++) {
                if(check.getNetworkInfo(networkInfos[i]).getState() == NetworkInfo.State.CONNECTED) {
                    Log.e(TAG, "Connected to the network!");
                    return true;
                }
            }

            return false;

        } else {
           NetworkInfo[] networkInfos  = check.getAllNetworkInfo();
            for(int i = 0; i < networkInfos.length; i++) {
                if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                    Log.e(TAG, "Connected to the network!");
                    return true;
                }
            }
            return false;
        }
    }


}
