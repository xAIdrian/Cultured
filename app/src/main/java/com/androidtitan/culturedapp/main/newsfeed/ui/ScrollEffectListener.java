package com.androidtitan.culturedapp.main.newsfeed.ui;

/**
 * Created by Adrian Mohnacs on 3/28/17.
 */

public interface ScrollEffectListener {

    void showToolbarBy(int dy);
    void hideToolbarBy(int dy);
    boolean cannotHideMore(int dy);
    boolean cannotShowMore(int dy)
}
