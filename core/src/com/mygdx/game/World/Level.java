package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entity.*;
import com.mygdx.game.Entity.Collider.Collider;
import com.mygdx.game.Util.CameraHelper;
import com.mygdx.game.Util.Constants;
import com.mygdx.game.Util.GraphGenerator;
import com.mygdx.game.Util.GraphImp;

import java.util.ArrayList;

/**
 * Created by Urree on 2016-07-26.
 */
public class Level {
    public static final String TAG = Level.class.getName();

    public enum BLOCK_TYPE {
        EMPTY(255, 255, 255), // white
        PLAYER_SPAWNPOINT(255, 0, 0), // red
        BOX(0, 0, 0); //black
        private int color;

        private BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }
    }

    // objects
    private GraphImp graph;
    private Player player;
    private ArrayList<Collider> movableObjects;
    private ArrayList<Collider> stationaryObjects;
    private ArrayList<Collider> shadowObjects;
    private ArrayList<ArrayList<GameObject>> gameObjects;
    private ArrayList<Light> lights;
    public static int levelWidth;
    public static int levelHeight;
    public static float tileWidth;
    public static float tileHeight;
    public Pixmap pixmap;
    public Level(String filename) {
        init(filename);
    }

    private void init(String filename) {
        // player character
        player = null;
        // objects
        movableObjects = new ArrayList<Collider>();
        stationaryObjects = new ArrayList<Collider>();
        shadowObjects = new ArrayList<Collider>();
        gameObjects = new ArrayList<ArrayList<GameObject>>();
        lights = new ArrayList<Light>();

        for (int i = 0; i < Constants.LEVEl_LAYERS; i++) {
            gameObjects.add(new ArrayList<GameObject>());
        }
        // load image file that represents the level data
        pixmap = new Pixmap(Gdx.files.internal(filename));
        levelWidth = pixmap.getWidth();
        levelHeight = pixmap.getHeight();
        tileWidth = 1f;
        tileHeight = 1f;


        // scan pixels from top-left to bottom-right
        int counter=0;
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {


                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY - 1;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match
                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // do nothing
                    Grass obj = new Grass(new Vector2(pixelX, baseHeight), new Vector2(1,1), true, true);
                    gameObjects.get(Constants.GRASS_LAYER).add(obj);
                }
                // box
                else if (BLOCK_TYPE.BOX.sameColor(currentPixel)) {
                    Box obj = new Box(new Vector2(pixelX, baseHeight), new Vector2(1,1), true, true);
                    stationaryObjects.add(obj);
                    shadowObjects.add(obj);
                    gameObjects.get(Constants.BOX_LAYER).add(obj);
                }
                // player spawn point
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    Player obj = new Player(new Vector2(pixelX, baseHeight), new Vector2(1,1), true, true, graph);
                    movableObjects.add(obj);
                    gameObjects.get(Constants.PLAYER_LAYER).add(obj);
                    lights.add(obj.getLight());
                    //lights.add(new Light(1,new Vector2(obj.getPosition().add(10,0)),Color.WHITE));
                    player = obj;
                    Grass obj2 = new Grass(new Vector2(pixelX, baseHeight), new Vector2(1,1), true, true);
                    gameObjects.get(Constants.GRASS_LAYER).add(obj2);
                }
                // unknown object/pixel color
                else {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                            + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
            }
        }
        graph = GraphGenerator.generateGraph(this);
        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }

    public void renderLayer(SpriteBatch batch, int layer) {
        for (GameObject obj : gameObjects.get(layer)) {
                obj.render(batch);
        }
    }
    public void renderLayerUp(SpriteBatch batch, int layer) {
        for (int i = layer; i < Constants.LEVEl_LAYERS; i++) {
            for (int j = 0; j < gameObjects.get(i).size(); j++) {
                gameObjects.get(i).get(j).render(batch);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < Constants.LEVEl_LAYERS; i++) {
            for (int j = 0; j < gameObjects.get(i).size(); j++) {
                gameObjects.get(i).get(j).render(batch);
            }
        }
    }

    public void renderShadows(SpriteBatch batch) {
        for (int i = 0; i < shadowObjects.size(); i++) {
            shadowObjects.get(i).render(batch);
        }
    }

    public void update(float deltaTime) {
        for (int i = 0; i < Constants.LEVEl_LAYERS; i++) {
            for (int j = 0; j < gameObjects.get(i).size(); j++) {
                gameObjects.get(i).get(j).update(deltaTime);
            }
        }
    }
    public Player getPlayer(){
        return player;
    }
    public ArrayList<ArrayList<GameObject>> getGameObjects(){
        return gameObjects;
    };
    public ArrayList<Collider> getMovableObjects(){
        return movableObjects;
    }
    public ArrayList<Collider> getStationaryObjects(){
        return stationaryObjects;
    }
    public ArrayList<Collider> getShadowObjects(){
        return stationaryObjects;
    }
    public ArrayList<Light> getLights(){
        return lights;
    }

    public GraphImp getGraph() {
        return graph;
    }
}