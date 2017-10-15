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
    private GraphImp graph;
    private IndexedAStarPathFinder<Node> pathFinder;
    private Player player;
    private Camera camera;
    private CameraHelper cameraHelper;
    private Vector2 mousePosition;
    private Vector3 destPosition;
    private Node startNode,endNode;
    public InputHandler(WorldController worldController , WorldRenderer worldRenderer){
        this.worldController = worldController;
        this.player = worldController.getLevel().getPlayer();
        this.cameraHelper = worldController.getCameraHelper();
        this.camera = worldRenderer.getCamera();
        graph = worldController.getLevel().getGraph();
        pathFinder = new IndexedAStarPathFinder<Node>(graph, false);
    }

    public boolean isStaight(Vector2 start, Vector2 end,Vector2 bounds){
        boolean straight = true;
        Vector2 startTopLeft = new Vector2(start.x-0.3f,start.y+0.4f);
        Vector2 endTopLeft = new Vector2(end.x-0.3f,end.y+0.4f);
        Vector2 startTopRight = new Vector2(start.x+0.3f,start.y+0.4f);
        Vector2 endTopRight = new Vector2(end.x+0.3f,end.y+0.4f);
        Vector2 startBottomLeft = new Vector2(start.x-0.3f,start.y-0.4f);
        Vector2 endBottomLeft = new Vector2(end.x-0.3f,end.y-0.3f);
        Vector2 startBottomRight = new Vector2(start.x+0.3f,start.y-0.4f);
        Vector2 endtBottomRight = new Vector2(end.x+0.3f,end.y-0.4f);
        for(Collider object : worldController.getLevel().getStationaryObjects()){
            if(object instanceof BoxCollider) {
                if (    Intersector.intersectSegmentPolygon(startTopLeft, endTopLeft, ((BoxCollider) object).getPolygon())||
                        Intersector.intersectSegmentPolygon(startTopRight, endTopRight, ((BoxCollider) object).getPolygon())||
                        Intersector.intersectSegmentPolygon(startBottomLeft, endBottomLeft, ((BoxCollider) object).getPolygon())||
                        Intersector.intersectSegmentPolygon(startBottomRight, endtBottomRight, ((BoxCollider) object).getPolygon())){
                    straight = false;
                    break;
                }
            }
        }
        return  straight;
    }

    public void smoothPath(GraphPathImp path){

        if(isStaight(player.getResultPath().get(0).getXY(),player.getResultPath().get(path.getCount()-1).getXY(),player.getDimension())){
            player.getResultPath().straightPath(player.getResultPath().get(0),player.getResultPath().get(path.getCount()-1));
        }
        else {
            int i = 1;
                while(i < path.getCount()-1){
                    if(isStaight(path.get(i-1).getXY(),path.get(i+1).getXY(),player.getDimension())){
                        path.remove(i);
                    }
                    else{
                        i++;
                    }
                }
                path.remove(0);
        }
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            mousePosition = new Vector2(Gdx.input.getX(),Gdx.input.getY());
            destPosition = camera.unproject(new Vector3(mousePosition,0));
            player.setDistance(player.getCenterPosition().dst(destPosition.x, destPosition.y));
            player.getVelocity().set(0,0);
            player.index = 0;
            player.getResultPath().clear();
            startNode = graph.getNodeByXY(player.getCenterPosition().x,player.getCenterPosition().y);
            endNode = graph.getNodeByXY(destPosition.x,destPosition.y);
            pathFinder.searchNodePath(startNode,endNode,new HeuristicImp(),player.getResultPath());
            if(player.getResultPath().getCount()>0) {
                smoothPath(player.getResultPath());
            }

            return true;
        }
        return false;
    }
    /*public void handleInputGame (float deltaTime) {

       if (cameraHelper.hasTarget(player)) {
            // Player Movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.getVelocity().x = -player.getMaxVelocity().x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.getVelocity().x =   player.getMaxVelocity().x;
            } else {
                player.getVelocity().x = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.getVelocity().y = player.getMaxVelocity().y;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.getVelocity().y = -player.getMaxVelocity().y;
            } else{
                player.getVelocity().y = 0;
            }
        }
    }*/


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
