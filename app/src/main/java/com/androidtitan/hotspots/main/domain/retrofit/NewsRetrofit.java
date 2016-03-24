package com.androidtitan.hotspots.main.domain.retrofit;

import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.domain.ArticleDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amohnacs on 3/21/16.
 */
public class NewsRetrofit {

    public static final String NEWYORKTIMES_BASE_URL = "http://api.nytimes.com/svc/news/v3/content/nyt/";
    private Retrofit nytRetrofit;

    public NewsRetrofit() {
        //logger
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        nytRetrofit = new Retrofit.Builder()
                .baseUrl(NEWYORKTIMES_BASE_URL)
                .client(httpClient)
                .addConverterFactory(buildGsonConverter())
                .build();
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //adding custom deserializer
        //gsonBuilder.registerTypeAdapter(NewsResponse.class, new NewsJsonDeserializer());
        gsonBuilder.registerTypeAdapter(Article.class, new ArticleDeserializer());
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }

    public Retrofit getRetrofit() {
        return nytRetrofit;
    }

}
