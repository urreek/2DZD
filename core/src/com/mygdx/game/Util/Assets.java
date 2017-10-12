package com.mygdx.game.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;



/**
 * Created by Urree on 2016-07-26.
 */
public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public AssetPlayer player;
    public AssetBox box;
    public AssetGrass grass;
    public AssetFonts fonts;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("fonts/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("fonts/arial-15.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("fonts/arial-15.fnt"), true);
            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
        }
    }


    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
                TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: "
                + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        TextureAtlas atlas =
                assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        // create game resource objects
        fonts = new AssetFonts();
        player = new AssetPlayer(atlas);
        box = new AssetBox(atlas);
        grass = new AssetGrass(atlas);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" +
                asset.fileName + "'", (Exception) throwable);
    }

    public class AssetPlayer {
        public final Animation<TextureRegion> playerDown;
        public final Animation<TextureRegion> playerLeft;
        public final Animation<TextureRegion> playerRight;
        public final Animation<TextureRegion> playerUp;

        public final TextureRegion playerStandingDown;
        public final TextureRegion playerStandingLeft;
        public final TextureRegion playerStandingRight;
        public final TextureRegion playerStandingUp;

        public AssetPlayer(TextureAtlas atlas) {
            Array<TextureRegion> down = new Array<TextureRegion>(Constants.ANIMATION_FRAMES);
            Array<TextureRegion> right = new Array<TextureRegion>(Constants.ANIMATION_FRAMES);
            Array<TextureRegion> left = new Array<TextureRegion>(Constants.ANIMATION_FRAMES);
            Array<TextureRegion> up = new Array<TextureRegion>(Constants.ANIMATION_FRAMES);
            TextureRegion playerTexture = atlas.findRegion("player");
            TextureRegion[][] temp = playerTexture.split(playerTexture.getRegionWidth() / Constants.SPRITESHEET_COLUMNS, playerTexture.getRegionHeight() / Constants.SPRITESHEET_COLUMNS);
            for (int x = 0; x < Constants.ANIMATION_FRAMES; x++) {
                down.add(temp[0][x]);
                right.add(temp[1][x]);
                left.add(temp[2][x]);
                up.add(temp[3][x]);
            }
            playerDown = new Animation(Constants.ANIMATION_FRAME_DURATION, down, Animation.PlayMode.LOOP);
            playerLeft = new Animation(Constants.ANIMATION_FRAME_DURATION, left, Animation.PlayMode.LOOP);
            playerRight = new Animation(Constants.ANIMATION_FRAME_DURATION, right, Animation.PlayMode.LOOP);
            playerUp = new Animation(Constants.ANIMATION_FRAME_DURATION, up, Animation.PlayMode.LOOP);

            playerStandingDown = temp[0][0];
            playerStandingRight = temp[1][0];
            playerStandingLeft = temp[2][0];
            playerStandingUp = temp[3][0];

        }
    }

    public class AssetBox {
        public final AtlasRegion regBox;

        public AssetBox(TextureAtlas atlas) {
            regBox = atlas.findRegion("box");
        }
    }

    public class AssetGrass {
        public final AtlasRegion regGrass;

        public AssetGrass(TextureAtlas atlas) {
            regGrass = atlas.findRegion("grass");
        }
    }

}
