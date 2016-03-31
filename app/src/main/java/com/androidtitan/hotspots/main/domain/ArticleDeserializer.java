package com.androidtitan.hotspots.main.domain;

import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.model.newyorktimes.Multimedium;
import com.androidtitan.hotspots.main.model.newyorktimes.RelatedUrl;
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
public class ArticleDeserializer implements JsonDeserializer<Article> {
    final String TAG = getClass().getSimpleName();

    @Override
    public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        Article tempArticle = new Article();

        tempArticle.setSection(obj.getAsJsonObject().get("section").getAsString());
        tempArticle.setSubsection(obj.getAsJsonObject().get("subsection").getAsString());
        tempArticle.setTitle(obj.getAsJsonObject().get("title").getAsString());

        tempArticle.setAbstract(obj.getAsJsonObject().get("abstract").getAsString());
        tempArticle.setUrl(obj.getAsJsonObject().get("url").getAsString());
        tempArticle.setByline(obj.getAsJsonObject().get("byline").getAsString());

        tempArticle.setThumbnailStandard(obj.getAsJsonObject().get("thumbnail_standard").getAsString());
        tempArticle.setItemType(obj.getAsJsonObject().get("item_type").getAsString());
        tempArticle.setSource(obj.getAsJsonObject().get("source").getAsString());

        tempArticle.setUpdatedDate((Date) context.deserialize(obj.getAsJsonObject().get("updated_date"), Date.class));
        tempArticle.setCreatedDate((Date) context.deserialize(obj.getAsJsonObject().get("created_date"), Date.class));
        tempArticle.setPublishedDate((Date) context.deserialize(obj.getAsJsonObject().get("published_date"), Date.class));

        tempArticle.setMaterialTypeFacet(obj.getAsJsonObject().get("material_type_facet").getAsString());
        tempArticle.setKicker(obj.getAsJsonObject().get("kicker").getAsString());
        tempArticle.setSubheadline(obj.getAsJsonObject().get("subheadline").getAsString());
        tempArticle.setBlogName(obj.getAsJsonObject().get("section").getAsString());

        //todo
        if(obj.getAsJsonObject().get("related_urls") instanceof JsonObject) {
            RelatedUrl tempRelatedUrl = new RelatedUrl();
            tempRelatedUrl.setSuggestedLinkText((obj.getAsJsonObject().get("related_urls")).getAsJsonObject().get("suggested_link_text").getAsString());
            tempRelatedUrl.setUrl((obj.getAsJsonObject().get("related_urls")).getAsJsonObject().get("url").getAsString());

            List<RelatedUrl> list = new ArrayList<RelatedUrl>();
            list.add(tempRelatedUrl);
            tempArticle.setRelatedUrls(list);
        }

        List<String> desList = new ArrayList<String>();
        if (obj.get("des_facet") instanceof JsonArray) {
            for(JsonElement ele : obj.get("des_facet").getAsJsonArray()) {
                desList.add(ele.getAsString());
            }
        } else {
            desList.add("");
        }
        tempArticle.setDesFacet(desList);

        List<String> orgList = new ArrayList<String>();
        if (obj.get("org_facet") instanceof JsonArray) {
            for(JsonElement ele : obj.get("org_facet").getAsJsonArray()) {
                orgList.add(ele.getAsString());
            }
        } else {
            orgList.add("");
        }
        tempArticle.setOrgFacet(orgList);

        List<String> perList = new ArrayList<String>();
        if (obj.get("per_facet") instanceof JsonArray) {
            for(JsonElement ele : obj.get("per_facet").getAsJsonArray()) {
                perList.add(ele.getAsString());
            }
        } else {
            perList.add("");
        }
        tempArticle.setPerFacet(perList);

        List<String> geoList = new ArrayList<String>();
        if (obj.get("geo_facet") instanceof JsonArray) {
            for(JsonElement ele : obj.get("geo_facet").getAsJsonArray()) {
                geoList.add(ele.getAsString());
            }
        } else {
            geoList.add("");
        }
        tempArticle.setGeoFacet(geoList);

        //todo
        if (obj.getAsJsonObject().get("multimedia") instanceof JsonArray) {
            Multimedium[] mediaobj = context.deserialize(obj.getAsJsonObject().get("multimedia"), Multimedium[].class);
            tempArticle.setMultimedia(Arrays.asList(mediaobj));
        }


        return tempArticle;
    }


    /*private Article getOrCreate(final String name) {
        Article author = cache.get().get(name);
        if (author == null) {
            author = new Article();
            cache.get().put(name, author);
        }
        return author;
    }*/
}