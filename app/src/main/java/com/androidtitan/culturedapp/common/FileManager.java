package com.androidtitan.culturedapp.common;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import static com.androidtitan.culturedapp.common.Constants.CULTURED_PREFERENCES;

/**
 * Created by Triforce on 3/6/17.
 */

public class FileManager {
    private static final String TAG = FileManager.class.getSimpleName();

    private static final String BOOKMARKED_COUNT_PREFERENCE = "bookmarked.count.preference";

    private static FileManager instance;

    private Context context;
    private WeakReference<FileCallback> weakFileCallback;
    private SharedPreferences sharedPreferences;

    private int bookmarkedCount;

    public static FileManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null) {
                    instance = new FileManager(context);
                }
            }
        }
        return instance;
    }

    public FileManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(CULTURED_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Local storage
     * @param callback
     * @param focusedArticle
     */
    public void writeArticleToFile(FileCallback callback, Article focusedArticle) {

        weakFileCallback = new WeakReference<>(callback);
        FileCallback fileCallback = weakFileCallback.get();

        Gson gson = new Gson();
        FileOutputStream outputStream;

        String json = gson.toJson(focusedArticle) + "\n";

            try {
                //internal
                outputStream = context.openFileOutput(Constants.BOOKMARK_ARTICLES_FILE, Context.MODE_APPEND);

                if(context.getFilesDir().getFreeSpace() > json.getBytes().length) {
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    fileCallback.onFileWriteComplete(context.getFilesDir().toString() + "/" + Constants.BOOKMARK_ARTICLES_FILE, false);
                    // to temporarily store files using caching use `getCacheDir()`

                    Log.d(TAG, "*** write success ***");

                    bookmarkedCount ++;
                    sharedPreferences.edit().putInt(BOOKMARKED_COUNT_PREFERENCE, bookmarkedCount).apply();
                } else {
                    String error = "Not enough free space available in memory to write file";
                    fileCallback.onFileWriteComplete(error, true);
                    Log.e(TAG, error);
                }
            } catch (Exception e) {
                fileCallback.onFileWriteComplete(e.toString(), true);
                e.printStackTrace();
            }

    }

    /**
     * External storage
     * @param callback
     * @param focusedArticle
     */
    public void writeArticleToExternalFile(FileCallback callback, Article focusedArticle) {

        weakFileCallback = new WeakReference<>(callback);
        FileCallback fileCallback = weakFileCallback.get();

        Gson gson = new Gson();

        String json = gson.toJson(focusedArticle) + "\n";

        File file  = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Constants.BOOKMARK_ARTICLES_FILE);
        if(!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }

        FileOutputStream outputStream;
        try {
            //external
            outputStream = new FileOutputStream(file);

            outputStream.write(json.getBytes());
            outputStream.flush();
            outputStream.close();

            fileCallback.onFileWriteComplete(context.getFilesDir().toString() + "/" + Constants.BOOKMARK_ARTICLES_FILE, false);

            Log.d(TAG, "*** write success ***");

        } catch (FileNotFoundException e) {
            fileCallback.onFileWriteComplete(e.toString(), true);
            e.printStackTrace();
        } catch (IOException e) {
            fileCallback.onFileWriteComplete(e.toString(), true);
            e.printStackTrace();
        }

    }

    public HashMap<String, Boolean> getInternalArticlesHashMap() {

        HashMap<String, Boolean> articleHashMap = new HashMap<>();

        for(Article article : getInternalArticles()) {
            articleHashMap.put(article.getTitle(), true);
        }

        return articleHashMap;
    }

    /**
     * Reads articles from internal storage
     * @return
     */
    public ArrayList<Article> getInternalArticles() {
        /* retrieveing your app's files folder and read the content from there:
            String filePath = context.getFilesDir() + Constants.BOOKMARK_ARTICLES_FILE;
            File bookmarkFile = new File(filePath);

            to delete a file:
            bookmarkFile.delete()
            context.deleteFile(bookmarkFile)
        */

        Gson gson = new Gson();

        ArrayList<Article> bookmarkedArticles = new ArrayList<>();
        FileInputStream fileInputStream;
        try {
            //internal
            fileInputStream = context.openFileInput(Constants.BOOKMARK_ARTICLES_FILE);

            InputStreamReader streamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            StringBuilder sb = new StringBuilder();
            String readLine;

            while((readLine = bufferedReader.readLine()) != null) {
                //sb.append(readLine);

                Article article = gson.fromJson(readLine, Article.class);
                bookmarkedArticles.add(article);
            }

            return bookmarkedArticles;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read articles from external storage
     * @return
     */
    public ArrayList<Article> getExternalArticles() {
        Gson gson = new Gson();

        ArrayList<Article> bookmarkedArticles = new ArrayList<>();
        FileInputStream fileInputStream;

        final File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Constants.BOOKMARK_ARTICLES_FILE);

        try {
            //external
            fileInputStream = new FileInputStream(file);

            InputStreamReader streamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            StringBuilder sb = new StringBuilder();
            String readLine;

            while((readLine = bufferedReader.readLine()) != null) {
                //sb.append(readLine);

                Article article = gson.fromJson(readLine, Article.class);
                bookmarkedArticles.add(article);
            }

            return bookmarkedArticles;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public interface FileCallback {

        void onFileWriteComplete(String response, boolean hasError);
    }
}
