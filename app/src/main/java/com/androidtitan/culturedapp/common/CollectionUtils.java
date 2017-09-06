package com.androidtitan.culturedapp.common;

import java.util.List;

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
    public static <T> boolean isEmpty(List<T> listToCheck) {
        if(listToCheck == null || listToCheck.size() <= 0) {
            return true;
        }
        return false;
    }
}
