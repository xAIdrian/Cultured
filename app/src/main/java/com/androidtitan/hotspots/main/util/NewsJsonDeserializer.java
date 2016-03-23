package com.androidtitan.hotspots.main.util;

import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.model.newyorktimes.NewsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by amohnacs on 3/22/16.
 */
public class NewsJsonDeserializer implements JsonDeserializer<NewsResponse> {

    private final String TAG = getClass().getSimpleName();

    public NewsJsonDeserializer() {

    }

    @Override
    public NewsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();

        Gson gson = new Gson();
        NewsResponse response = gson.fromJson(json, NewsResponse.class);

        if (jsonObject.get("status").getAsString().equals("OK")){

            response.setStatus(jsonObject.get("status").getAsString());
            response.setCopyright(jsonObject.get("copyright").getAsString());
            response.setNumResults(jsonObject.get("num_results").getAsInt());

            List<Article> list = context.deserialize(json.getAsJsonObject().get("results"), Article.class);
            response.setArticles(list);

        }

        return response;
    }


}
