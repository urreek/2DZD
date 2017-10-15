package com.mygdx.game.Util;

/**
 * Created by Urree on 2016-07-22.
 */
public class Constants {
    //App width and height
    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 1024;

    // Visible game world is 5 meters wide
    public static final float VIEWPORT_WIDTH = 30.0f;
    // Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = 30.0f;

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS =
            "textureAtlas/LibgdxGame.atlas";

    // Location of image file for level 01
    public static final String LEVEL_01 = "levels/level-01.png";

    //Layers
    public static final int LEVEl_LAYERS = 4;
    public static final int BOX_LAYER = 2;
    public static final int PLAYER_LAYER = 3;
    public static final int GRASS_LAYER = 1;

    //Player constants
    public static final int PLAYER_SPEED = 5;
    public static final int ANIMATION_FRAMES = 4;
    public static final int SPRITESHEET_COLUMNS = 8;
    public static final float ANIMATION_FRAME_DURATION = 0.10f;
    public static final int PLAYER_MAX_HP = 100;

}