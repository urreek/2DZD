package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Util.Assets;
import com.mygdx.game.Util.InputHandler;
import com.mygdx.game.World.WorldController;
import com.mygdx.game.World.WorldRenderer;

public class MyGdxGame implements ApplicationListener {

	private static final String TAG = MyGdxGame.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
    private InputHandler inputHandler;
    private boolean paused;

	@Override public void create () {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());

        // Initialize controller and renderer
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
        inputHandler = new InputHandler(worldController,worldRenderer);
        Gdx.input.setInputProcessor(inputHandler);
        paused = false;
    }
	@Override public void render () {
        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed
            // since last rendered frame.
            inputHandler.handleDebugInput(Gdx.graphics.getDeltaTime());
            //inputHandler.handleInputGame(Gdx.graphics.getDeltaTime());
            worldController.update(Gdx.graphics.getDeltaTime());
            // Sets the clear screen color to: Cornflower Blue
            Gdx.gl.glClearColor(0f,0f,0f,0f);
            // Clears the screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // Render game world to screen
            worldRenderer.render();
        }
    }
	@Override public void resize (int width, int height) {
        worldRenderer.resize(width, height);
    }
	@Override public void pause () {
        paused = true;
    }
	@Override public void resume () {
        paused = false;
    }

	@Override public void dispose () {
	    worldRenderer.dispose();
        Assets.instance.dispose();
    }
}
