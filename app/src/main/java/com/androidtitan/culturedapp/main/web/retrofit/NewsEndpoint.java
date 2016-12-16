package com.androidtitan.culturedapp.main.web.retrofit;


import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by amohnacs on 3/21/16.
 */
public interface NewsEndpoint {
    ///{section}[/time-period][.response-format]?api-key={your-API-key}
    //hworld/.json?limit=20&api-key=sample-key

    @GET("/svc/news/v3/content/all/{section}.json")
    Observable<NewsResponse> newsWireArticles(
            @Path("section") String string,
            @Query("limit") int count,
            @Query("offset") int offset,
            @Query("api-key") String yourKey
    );

    @GET("svc/topstories/v2/{section}.json")
    Observable<NewsResponse> topStories(
            @Path("section") String string,
            @Query("api-key") String yourKey
            /*,
            @Query("limit") int count,
            @Query("offset") int offset,
            */
    );

}
