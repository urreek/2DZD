package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Util.Constants;


/**
 * Created by Urree on 2016-07-22.
 */
public class WorldRenderer implements Disposable {
    private LightRenderer lightRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    private float aspectRatio;




    public WorldRenderer (WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init () {
        batch = new SpriteBatch();
        aspectRatio = Constants.APP_HEIGHT / Constants.APP_WIDTH;
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * aspectRatio);
        lightRenderer = new LightRenderer(worldController, camera, batch);

    }

    public void render () {
        camera.setToOrtho(false,Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT * aspectRatio);
        worldController.getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setShader(null); //default shader
        worldController.getLevel().renderLayer(batch,1);
        batch.end();
        camera.zoom = 1;
        lightRenderer.render();
        //STEP 4. render sprites in full colour
        worldController.getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.getLevel().renderLayerUp(batch, 2);
        batch.end();
    }

    public void resize (int width, int height)
    {
        aspectRatio = (float)height/(float)width;
        lightRenderer.resize(width, height);
    }
    @Override public void dispose () {
        batch.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

}