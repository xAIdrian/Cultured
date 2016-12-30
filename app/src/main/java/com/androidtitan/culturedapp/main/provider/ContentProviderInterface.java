package com.androidtitan.culturedapp.main.provider;

/**
 * Created by amohnacs on 9/19/16.
 */

public interface ContentProviderInterface {

    interface ProviderCallback {
        void SQLiteCreationComplete(boolean isSuccessful);
    }
}
