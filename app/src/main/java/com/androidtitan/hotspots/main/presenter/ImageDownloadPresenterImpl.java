package com.androidtitan.hotspots.main.presenter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.androidtitan.hotspots.common.util.ImageHelper;

import java.lang.ref.WeakReference;

/**
 * Created by amohnacs on 3/19/16.
 */
public class ImageDownloadPresenterImpl implements ImageDownloadPresenter {
    private final String TAG = getClass().getSimpleName();


    @Override
    public void imageDownload(String url, ImageView imageView) {

        if(cancelPotentialDownload(url, imageView)) {
            BitmapDownloaderAsyncTask asyncTask = new BitmapDownloaderAsyncTask(imageView);
            DownloadDrawable downloadDrawable = new DownloadDrawable(asyncTask);
            imageView.setImageDrawable(downloadDrawable);
            asyncTask.execute(url);
        }
    }

    private static BitmapDownloaderAsyncTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadDrawable) {
                DownloadDrawable downloadedDrawable = (DownloadDrawable)drawable;
                return downloadedDrawable.getBitmapDownloadTask();
            }
        }
        return null;
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderAsyncTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    class BitmapDownloaderAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private String url;
        private final WeakReference<ImageView> weakImageView;

        public BitmapDownloaderAsyncTask(ImageView imageView) {
            weakImageView = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            return ImageHelper.getBitmapFromURL(params[0]); //parameters are given in an array
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (weakImageView != null) {
                ImageView imageView = weakImageView.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);

                    /*int bgColor = Palette.from(bitmap).generate().getDarkVibrantColor(
                            ContextCompat.getColor(App.getAppComponent().getApplicationContext(), android.R.color.black));

                    bgLayout.setBackgroundColor(bgColor);*/
                }
            }
        }
    }

    static class DownloadDrawable extends ColorDrawable { //can we change this to a Bitmap Drawable
        private final WeakReference<BitmapDownloaderAsyncTask> weakBitmapAsyncTask;

        public DownloadDrawable(BitmapDownloaderAsyncTask task) {
            super(Color.LTGRAY);
            weakBitmapAsyncTask = new WeakReference<BitmapDownloaderAsyncTask>(task);
        }

        public BitmapDownloaderAsyncTask getBitmapDownloadTask() {
            return weakBitmapAsyncTask.get();
        }
    }
}
