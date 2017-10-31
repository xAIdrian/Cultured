package com.androidtitan.culturedapp.main.web;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by amohnacs on 3/29/16.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    private final String TAG = getClass().getSimpleName();

    private static final String[] formats = {
            "yyyy-MM-dd'T'HH:mm:ss'Z'",   "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",        "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ",     "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss",        "yyyyMMdd",
            "MMM dd, yyyy HH:mm:ss a"};

    @Override
    public Date deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String date = element.getAsString();

        Date transferDate = parse(date);
        //Log.e(TAG, transferDate != null ? "Successfully parsed the value of " + transferDate.toString() : "date is null");

        return transferDate;
    }

    /*
     * @param args
     */
    public static void main(String[] args) {
        String yyyyMMdd = "20110917";
        parse(yyyyMMdd);
    }

    public static Date parse(String d) {
        if (d != null) {
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    return sdf.parse(d);
                } catch (ParseException e) {
                    // Log.e(TAG, "Failed Parsing ... " + parse);
                }
            }
        }
        return null;
    }
}
