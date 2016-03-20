package com.androidtitan.hotspots.main.presenter;

import android.content.Context;
import android.util.Log;

import com.androidtitan.hotspots.main.domain.DaggerSpotifyRetroFitComponent;
import com.androidtitan.hotspots.main.domain.SpotifyEndpointInterface;
import com.androidtitan.hotspots.main.model.Item;
import com.androidtitan.hotspots.main.model.SpotifyResponse;
import com.androidtitan.hotspots.main.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by amohnacs on 3/15/16.
 */
public class MainPresenterImpl implements MainPresenter{
    private final String TAG = getClass().getSimpleName();

    @Inject Retrofit retrofit;

    private Context context;

    @Inject
    public MainPresenterImpl(Context context) {
        DaggerSpotifyRetroFitComponent.create()
                .inject(this);

        //App.getAppComponent().inject(this);
        this.context = context;
    }

    public void respond(String string) {
        Log.e("TAG", string);

    }

    @Override
    public List<Item> querySpotifyTracks(String search, int count) {
        //todo: create Dagger2 dependency with these objects

        final ArrayList<Item> itemList = new ArrayList<Item>();

        SpotifyEndpointInterface spotifyService = retrofit.create(SpotifyEndpointInterface.class);
        final Call<SpotifyResponse> call = spotifyService.tracks(search, "track", count);
        //new RetroFitAsyncTask().execute(call);

        call.enqueue(new Callback<SpotifyResponse>() {
            @Override
            public void onResponse(Call<SpotifyResponse> call, Response<SpotifyResponse> response) {
                if(response.isSuccessful()) {

                    SpotifyResponse resp = response.body();

                    for(Item item : resp.getTracks().getItems()) {
                        itemList.add(item);
                        ((MainActivity)context).updateImageAdapter();

                    }
                    //Log.e(TAG, resp.getTracks().getItems().get(0).getName());
                } else {
                    Log.e(TAG, "response fail");
                }
            }

            @Override
            public void onFailure(Call<SpotifyResponse> call, Throwable t) {

            }
        });

        return itemList;
    }

}
