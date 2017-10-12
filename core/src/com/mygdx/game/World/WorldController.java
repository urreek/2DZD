package com.mygdx.game.World;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.Collider.Collider;
import com.mygdx.game.Entity.GameObject;
import com.mygdx.game.Entity.Light;
import com.mygdx.game.Entity.Player;
import com.mygdx.game.Util.*;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.util.ArrayList;


/**
 * Created by Urree on 2016-07-22.
 */
public class WorldController extends InputAdapter implements InputProcessor
{
    private static final String TAG = WorldController.class.getName();

    private Player player;
    private CameraHelper cameraHelper;
    private Level level;
    private ArrayList<ArrayList<GameObject>> gameObjects;
    private ArrayList<Collider> movableObjects;
    private ArrayList<Collider> stationaryObjects;
    private ArrayList<Collider> shadowObjects;
    private ArrayList<Light> lights;
    public WorldController () {
        init();
    }

    private void init () {
        cameraHelper = new CameraHelper();
        initLevel();
        initObjects();
    }

    private void initLevel () {
        level = new Level(Constants.LEVEL_01);
    }

    private void initObjects() {
        player = level.getPlayer();
        movableObjects = level.getMovableObjects();
        stationaryObjects = level.getStationaryObjects();
        shadowObjects = level.getShadowObjects();
        gameObjects = level.getGameObjects();
        lights = level.getLights();
        cameraHelper.setTarget(player);
    }

    public CameraHelper getCameraHelper() {
        return cameraHelper;
    }

    public Level getLevel() {
        return level;
    }

    public ArrayList<Light> getLights(){
        return lights;
    }

    public void update (float deltaTime) {
        level.update(deltaTime);
        checkCollision();
        cameraHelper.update(deltaTime);
    }

    private void checkCollision(){
        int index = 1;
        for(int i = 0; i < movableObjects.size(); i++){
            for(int j = index; j < movableObjects.size(); j++){
                movableObjects.get(i).checkCollision(movableObjects.get(j));
            }
            index++;
            for(int k = 0; k < stationaryObjects.size() && i < movableObjects.size(); k++){
                movableObjects.get(i).checkCollision(stationaryObjects.get(k));
            }
        }
    }


}