package com.androidtitan.hotspots.main.domain.retrofit;

import com.androidtitan.hotspots.main.model.newyorktimes.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by amohnacs on 3/21/16.
 */
public interface NewsEndpointInterface {
    ///{section}[/time-period][.response-format]?api-key={your-API-key}
    //hworld/.json?limit=20&api-key=sample-key

    @GET("{section}/.json")
    Call<NewsResponse> articles (
        @Path("section") String string,
        @Query("limit") int count,
        @Query("api-key") String yourKey
    );

}
