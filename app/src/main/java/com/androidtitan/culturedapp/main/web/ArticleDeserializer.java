package com.androidtitan.culturedapp.main.web;

import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.model.newyorktimes.RelatedUrl;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by amohnacs on 3/22/16.
 */

/*
    TODO: reevaluate the null checks inside of the deserializer
        There are a number of JsonObjects that are checked for null values.  The null values are getting
        saved to the ArticleTable and we passing null values.

 */
public class ArticleDeserializer implements JsonDeserializer<Article> {
    final String TAG = getClass().getSimpleName();

    @Override
    public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        Article tempArticle = new Article();

        tempArticle.setSection(!obj.getAsJsonObject().get("section").isJsonNull() ?
                obj.getAsJsonObject().get("section").getAsString() : "");
        tempArticle.setSubsection(!obj.getAsJsonObject().get("subsection").isJsonNull() ?
                obj.getAsJsonObject().get("subsection").getAsString() : "");
        tempArticle.setTitle(!obj.getAsJsonObject().get("title").isJsonNull() ?
                obj.getAsJsonObject().get("title").getAsString() : "");

        tempArticle.setAbstract(!obj.getAsJsonObject().get("abstract").isJsonNull() ?
                obj.getAsJsonObject().get("abstract").getAsString() : "");
        tempArticle.setUrl(!obj.getAsJsonObject().get("url").isJsonNull() ?
                obj.getAsJsonObject().get("url").getAsString() : "");

        tempArticle.setByline(!obj.getAsJsonObject().get("byline").isJsonNull()
                ? obj.getAsJsonObject().get("byline").getAsString() : "");
        if (obj.getAsJsonObject().has("thumbnail_standard")) {
            tempArticle.setThumbnailStandard(obj.getAsJsonObject().get("thumbnail_standard").getAsString());
        }

        tempArticle.setItemType(!obj.getAsJsonObject().get("item_type").isJsonNull() ?
                obj.getAsJsonObject().get("item_type").getAsString() : "");

        if (obj.getAsJsonObject().has("source")) {
            tempArticle.setSource(!obj.getAsJsonObject().get("source").isJsonNull()
                    ? obj.getAsJsonObject().get("source").getAsString() : "");
        }


        tempArticle.setUpdatedDate(context.deserialize(obj.getAsJsonObject().get("updated_date"), Date.class));
        tempArticle.setCreatedDate(context.deserialize(obj.getAsJsonObject().get("created_date"), Date.class));
        tempArticle.setPublishedDate(context.deserialize(obj.getAsJsonObject().get("published_date"), Date.class));

        tempArticle.setMaterialTypeFacet(!obj.getAsJsonObject().get("material_type_facet").isJsonNull() ?
                obj.getAsJsonObject().get("material_type_facet").getAsString() : "");

        if (obj.getAsJsonObject().has("kicker")) {
            tempArticle.setKicker(!obj.getAsJsonObject().get("kicker").isJsonNull()
                    ? obj.getAsJsonObject().get("kicker").getAsString() : "");
        }

        if (obj.getAsJsonObject().has("headline")) {
            tempArticle.setheadline(!obj.getAsJsonObject().get("headline").isJsonNull()
                    ? obj.getAsJsonObject().get("headline").getAsString() : "");
        }

        if (obj.getAsJsonObject().has("blog_name")) {
            tempArticle.setBlogName(!obj.getAsJsonObject().get("blog_name").isJsonNull()
                    ? obj.getAsJsonObject().get("blog_name").getAsString() : "");
        }

        if (obj.getAsJsonObject().has("related_urls")) {
            if (obj.getAsJsonObject().get("related_urls") instanceof JsonObject) {
                RelatedUrl tempRelatedUrl = new RelatedUrl();
                tempRelatedUrl.setSuggestedLinkText((obj.getAsJsonObject().get("related_urls")).getAsJsonObject().get("suggested_link_text").getAsString());
                tempRelatedUrl.setUrl((obj.getAsJsonObject().get("related_urls")).getAsJsonObject().get("url").getAsString());

                List<RelatedUrl> list = new ArrayList<RelatedUrl>();
                list.add(tempRelatedUrl);
                tempArticle.setRelatedUrls(list);
            }
        }

        List<Facet> desList = new ArrayList<>();
        if (obj.get("des_facet") instanceof JsonArray && !obj.get("des_facet").isJsonNull()) {
            for (JsonElement ele : obj.get("des_facet").getAsJsonArray()) {
                Facet facet = new Facet(FacetType.DES, ele.getAsString(), tempArticle.getCreatedDate());
                desList.add(facet);
            }
        } else {
            desList.add(new Facet());
        }
        tempArticle.setDesFacet(desList);

        List<Facet> orgList = new ArrayList<>();
        if (obj.get("org_facet") instanceof JsonArray && !obj.get("org_facet").isJsonNull()) {
            for (JsonElement ele : obj.get("org_facet").getAsJsonArray()) {
                Facet facet = new Facet(FacetType.ORG, ele.getAsString(), tempArticle.getCreatedDate());
                orgList.add(facet);
            }
        } else {
            orgList.add(new Facet());
        }
        tempArticle.setOrgFacet(orgList);

        List<Facet> perList = new ArrayList<>();
        if (obj.get("per_facet") instanceof JsonArray && !obj.get("per_facet").isJsonNull()) {
            for (JsonElement ele : obj.get("per_facet").getAsJsonArray()) {
                Facet facet = new Facet(FacetType.PER, ele.getAsString(), tempArticle.getCreatedDate());
                perList.add(facet);
            }
        } else {
            perList.add(new Facet());
        }
        tempArticle.setPerFacet(perList);

        List<Facet> geoList = new ArrayList<>();
        if (obj.get("geo_facet") instanceof JsonArray && !obj.get("geo_facet").isJsonNull()) {
            for (JsonElement ele : obj.get("geo_facet").getAsJsonArray()) {
                Facet facet = new Facet(FacetType.GEO, ele.getAsString(), tempArticle.getCreatedDate());
                geoList.add(facet);
            }
        } else {
            geoList.add(new Facet());
        }
        tempArticle.setGeoFacet(geoList);

        if (obj.getAsJsonObject().get("multimedia") instanceof JsonArray) {
            Multimedium[] mediaobj = context.deserialize(obj.getAsJsonObject().get("multimedia"), Multimedium[].class);
            tempArticle.setMultimedia(Arrays.asList(mediaobj));

        }

        return tempArticle;
    }


    /*private ArticleTable getOrCreate(final String name) {
        ArticleTable author = cache.get().get(name);
        if (author == null) {
            author = new ArticleTable();
            cache.get().put(name, author);
        }
        return author;
    }*/
}
