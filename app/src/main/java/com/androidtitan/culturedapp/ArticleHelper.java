package com.androidtitan.culturedapp;

import com.google.gson.Gson;

import com.androidtitan.culturedapp.common.CollectionUtils;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by amohnacs on 10/17/17.
 */

public class ArticleHelper {

    public static String multimediaToJsonString(List<Multimedium> mulit) {
        Gson gson = new Gson();

        if (!CollectionUtils.isEmpty(mulit)) {
            Multimedium ourGuy = mulit.get(mulit.size() - 1);
            if (ourGuy != null) {
                return gson.toJson(ourGuy);
            }
        }
        return "";
    }

    public static boolean isArticleBookmarked(
            @NonNull Map<String, Boolean> bookMarkedArticles, @NonNull String articleTitle) {
        if (bookMarkedArticles.get(articleTitle) != null) {
            return bookMarkedArticles.get(articleTitle);
        }
        return false;
    }

    public static ArrayList<String> getGeoFacetArrayList(@NonNull Article article) {
        ArrayList<String> facets = new ArrayList<>();
        for (Facet facet : article.getGeoFacet()) {
            facets.add(facet.getFacetText());
        }
        return facets;
    }
}
