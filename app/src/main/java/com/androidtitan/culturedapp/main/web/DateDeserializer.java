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

    String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Override
    public Date deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String date = element.getAsString();

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return format.parse(date);
        } catch (ParseException exp) {
            Log.e(TAG, "Failed to parse Date :: " + exp);
            return null;
        }
    }
}
