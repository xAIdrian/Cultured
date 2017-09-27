package com.androidtitan.culturedapp.main.no_internet;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by amohnacs on 9/6/17.
 */

public class DataBinder {

    private DataBinder() {
        //NO-OP
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }

}
