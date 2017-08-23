package com.androidtitan.culturedapp.common;

import java.util.ArrayList;

/**
 * Created by Adrian Mohnacs on 8/3/17.
 */

public class CollectionUtils {

    /**
     *  Consolidates the check of the list's nullability and emptiness
     *
     * @param listToCheck
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(ArrayList<T> listToCheck) {
        if(listToCheck == null || listToCheck.size() <= 0) {
            return false;
        }
        return true;
    }
}
