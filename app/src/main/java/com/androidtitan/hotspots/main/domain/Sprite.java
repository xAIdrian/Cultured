package com.androidtitan.hotspots.main.domain;

/**
 * Created by amohnacs on 3/15/16.
 */
public class Sprite {
    /*
"spriteLocation": {
      "spriteSheetUri": "https://content.halocdn.com/media/Default/games/halo-5-guardians/sprites/medalspritesheet-386e9216ae434f76ae317bccabe91fb4.png",
      "left": 675,
      "top": 375,
      "width": 74,
      "height": 74,
      "spriteWidth": 1350,
      "spriteHeight": 824
    }
 */

    private String spriteSheetUrl;
    private int left;
    private int top;
    private int width;
    private int height;
    private int spriteWidth;
    private int spriteHeight;


    public String getSpriteSheetUrl() {
        return spriteSheetUrl;
    }

    public void setSpriteSheetUrl(String spriteSheetUrl) {
        this.spriteSheetUrl = spriteSheetUrl;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }
}
