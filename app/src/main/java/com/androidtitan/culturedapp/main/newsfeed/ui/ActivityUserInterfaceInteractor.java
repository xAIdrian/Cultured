package com.androidtitan.culturedapp.main.newsfeed.ui;

/**
 * Created by Adrian Mohnacs on 3/28/17.
 */

public interface ActivityUserInterfaceInteractor {

    void showToolbarBy(int dy);
    void hideToolbarBy(int dy);
    boolean cannotHideMore(int dy);
    boolean cannotShowMore(int dy);
    void scrollViewParallax(int dy);
    void showColoredSnackbar();
    void setAppBarElevation(int ele);

    void onLoadComplete();
}
