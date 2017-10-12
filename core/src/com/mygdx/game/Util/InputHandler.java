package com.mygdx.game.Util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.Collider.BoxCollider;
import com.mygdx.game.Entity.Collider.Collider;
import com.mygdx.game.Entity.Player;
import com.mygdx.game.World.WorldController;
import com.mygdx.game.World.WorldRenderer;

/**
 * Created by Urree on 2016-08-20.
 */
public class InputHandler extends InputAdapter {

    private static final String TAG = InputHandler.class.getName();
    private WorldController worldController;
    private Player player;
    private Camera camera;
    private CameraHelper cameraHelper;
    private Vector2 mousePosition;
    private Vector3 destPosition;

    public InputHandler(WorldController worldController , WorldRenderer worldRenderer){
        this.worldController = worldController;
        this.player = worldController.getLevel().getPlayer();
        this.cameraHelper = worldController.getCameraHelper();
        this.camera = worldRenderer.getCamera();
    }

    public void handleInput(float deltaTime){
        handleDebugInput(deltaTime);
        handlePlayerInput(deltaTime);
    }

    public void handlePlayerInput(float deltaTime){
        player.setVelocity(0,0);
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            player.getVelocity().add(3,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            player.getVelocity().add(-3,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            player.getVelocity().add(0,3);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            player.getVelocity().add(0,-3);
        }

    }
    public void handleDebugInput (float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        if (!cameraHelper.hasTarget(player)) {
            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *=
                    camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed,
                    0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed,
                    0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0,
                    -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
                cameraHelper.setPosition(0, 0);
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *=
                camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(
                -camZoomSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);

    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean keyUp (int keycode) {

        // Reset game world
        if (keycode == Input.Keys.R) {
            Gdx.app.debug(TAG, "Game world resetted");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget()
                    ? null: player);
            Gdx.app.debug(TAG, "Camera follow enabled: "
                    + cameraHelper.hasTarget());
        }
        return false;
    }
}
