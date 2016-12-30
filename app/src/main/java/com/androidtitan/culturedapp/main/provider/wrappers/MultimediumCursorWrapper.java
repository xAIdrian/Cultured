package com.androidtitan.culturedapp.main.provider.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.main.provider.DatabaseContract;

/**
 * Created by Adrian Mohnacs on 11/30/16.
 */

public class MultimediumCursorWrapper extends CursorWrapper{

    public MultimediumCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Multimedium getMultimedium() {

        String storyId = getString(getColumnIndex(DatabaseContract.MediaTable.STORY_ID));
        String url = getString(getColumnIndex(DatabaseContract.MediaTable.URL));
        String format = getString(getColumnIndex(DatabaseContract.MediaTable.FORMAT));
        int height = getInt(getColumnIndex(DatabaseContract.MediaTable.HEIGHT));
        int width = getInt(getColumnIndex(DatabaseContract.MediaTable.WIDTH));
        String type = getString(getColumnIndex(DatabaseContract.MediaTable.TYPE));
        String subtype = getString(getColumnIndex(DatabaseContract.MediaTable.SUBTYPE));
        String caption = getString(getColumnIndex(DatabaseContract.MediaTable.CAPTION));
        String copyright = getString(getColumnIndex(DatabaseContract.MediaTable.COPYRIGHT));

        return new Multimedium(storyId, url, format, height, width, type, subtype, caption, copyright);
    }
}
