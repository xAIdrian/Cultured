package com.androidtitan.culturedapp.common;

/**
 * Created by amohnacs on 5/7/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.LruCache;

import static com.androidtitan.culturedapp.common.Constants.CULTURED_PREFERENCES;

/**
 * Class used to docking, loading, and departing of caches on external or disk storage
 */
public class CacheDockingStation<K, J, L> {
    private static final String TAG = CacheDockingStation.class.getCanonicalName();

    public static final String TOP_ARTICLE_TITLE_CACHE = "cachedockingstation.toparticletitlecache";
    public static final String TOP_ARTICLE_FACET_CACHE = "cachedockingstation.toparticlefacetcache";
    public static final String TOP_ARTICLE_BITMAP_CACHE = "cachedockingstation.toparticlebitmapcache";

    public enum BITMAP_STATION_SIZE {
        PLANETARY,
        GALACTIC,
        FEDERATION
    }

    private Context context;
    private LruCache<String, Bitmap> bitmapLruCache;
    private SharedPreferences preferences;

    public CacheDockingStation(Context context, BITMAP_STATION_SIZE size) {
        this.context = context;

        preferences = context.getSharedPreferences(CULTURED_PREFERENCES, Context.MODE_PRIVATE);

        switch (size) {
            case PLANETARY:
                bitmapLruCache = buildPlanetaryBitmapCache();
                break;
            case GALACTIC:

                break;
            case FEDERATION:

                break;
            default:
                //throw
        }
    }

    private LruCache<String, Bitmap> buildPlanetaryBitmapCache() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        return new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void cacheTopArticleBasics(K title, J widgetFacet, L bitmap) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(TOP_ARTICLE_TITLE_CACHE, (String) title);
        editor.putString(TOP_ARTICLE_FACET_CACHE, (String) widgetFacet);
        editor.apply();

        addBitmapToMemoryCache(
                CacheDockingStation.TOP_ARTICLE_BITMAP_CACHE, (Bitmap) bitmap);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return bitmapLruCache.get(key);
    }

    public Bitmap loadBitmap(int resId) {
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            return bitmap;
        } else {
            /*
            background thread is spawned to process the image

            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
            task.execute(resId);
            */
        }
        return null;
    }
}
