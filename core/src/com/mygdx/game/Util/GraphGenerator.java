package com.mygdx.game.Util;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.Box;
import com.mygdx.game.Entity.Collider.BoxCollider;
import com.mygdx.game.Entity.Collider.Collider;
import com.mygdx.game.Entity.GameObject;
import com.mygdx.game.World.Level;

import java.util.ArrayList;

/**
 * Created by Urree on 2016-08-22.
 */
public class GraphGenerator {
    public static final int nodesPerTile = 1;

    public static GraphImp generateGraph(Level level){
        Array<Node> nodes = new Array<Node>();
        ArrayList<ArrayList<GameObject>> gameObjects = level.getGameObjects();
        ArrayList<Collider> stationaryObjects = level.getStationaryObjects();
        int mapHeight = Level.levelHeight;
        int mapWidth = Level.levelWidth;

        for(int y = 0; y < mapHeight*nodesPerTile; y++){
            for(int x = 0; x < mapWidth*nodesPerTile; x++){
                Node node = new Node();
                node.type = Node.Type.REGULAR;
                nodes.add(node);
            }
        }
        for(int y = 0; y < mapHeight*nodesPerTile; y++){
            int baseHeight = mapHeight*nodesPerTile-y-1;

            for(int x = 0; x < mapWidth*nodesPerTile; x++) {

                int target = level.pixmap.getPixel(x/nodesPerTile,baseHeight/nodesPerTile);
                int up = level.pixmap.getPixel(x/nodesPerTile,(baseHeight-1)/nodesPerTile);
                int left = level.pixmap.getPixel((x-1)/nodesPerTile,baseHeight/nodesPerTile);
                int right = level.pixmap.getPixel((x+1)/nodesPerTile,baseHeight/nodesPerTile);
                int down = level.pixmap.getPixel(x/nodesPerTile,(baseHeight+1)/nodesPerTile);
                int upLeft = level.pixmap.getPixel((x-1)/nodesPerTile,(baseHeight-1)/nodesPerTile);
                int upRight = level.pixmap.getPixel((x+1)/nodesPerTile,(baseHeight-1)/nodesPerTile);
                int downLeft = level.pixmap.getPixel((x-1)/nodesPerTile,(baseHeight+1)/nodesPerTile);
                int downRight = level.pixmap.getPixel((x+1)/nodesPerTile,(baseHeight+1)/nodesPerTile);

                Node targetNode = nodes.get(mapWidth*nodesPerTile * y + x);

                if(isBlockEmpty(target)){

                    if(y != 0 && isBlockEmpty(down)&& isFree(nodes.get(mapWidth*nodesPerTile * (y - 1) + x).getXY(),stationaryObjects)){
                        Node downNode = nodes.get(mapWidth*nodesPerTile * (y - 1) + x);
                        targetNode.createConnection(downNode,1);
                    }
                    if(x != 0 && isBlockEmpty(left) && isFree(nodes.get(mapWidth*nodesPerTile * y + x - 1).getXY(),stationaryObjects)){
                        Node leftNode = nodes.get(mapWidth*nodesPerTile * y + x - 1);
                        targetNode.createConnection(leftNode,1);
                    }
                    if(x != mapWidth*nodesPerTile - 1 && isBlockEmpty(right) && isFree(nodes.get(mapWidth*nodesPerTile * y + x + 1).getXY(),stationaryObjects)){
                        Node rightNode = nodes.get(mapWidth*nodesPerTile * y + x + 1);
                        targetNode.createConnection(rightNode,1);
                    }
                    if(y != mapHeight*nodesPerTile - 1 && isBlockEmpty(up) && isFree(nodes.get(mapWidth*nodesPerTile * (y + 1) + x).getXY(),stationaryObjects)){
                        Node upNode = nodes.get(mapWidth*nodesPerTile * (y + 1) + x);
                        targetNode.createConnection(upNode,1);
                    }

                    if(y != mapHeight*nodesPerTile - 1 && x != 0 && isBlockEmpty(upLeft) && isBlockEmpty(up) && isBlockEmpty(left) && isFree(nodes.get(mapWidth*nodesPerTile * (y + 1) + (x-1)).getXY(),stationaryObjects)){
                        Node upLeftNode = nodes.get(mapWidth*nodesPerTile * (y + 1) + (x-1));
                        targetNode.createConnection(upLeftNode,1.4f);
                    }
                    if(y != mapHeight*nodesPerTile - 1 && x != mapWidth*nodesPerTile - 1 && isBlockEmpty(upRight) && isBlockEmpty(up) && isBlockEmpty(right) && isFree(nodes.get(mapWidth*nodesPerTile * (y + 1) + (x+1)).getXY(),stationaryObjects)){
                        Node upRightNode = nodes.get(mapWidth*nodesPerTile * (y + 1) + (x+1));
                        targetNode.createConnection(upRightNode,1.4f);
                    }

                    if(y != 0 && x != 0 && isBlockEmpty(downLeft) && isBlockEmpty(down) && isBlockEmpty(left) && isFree(nodes.get(mapWidth*nodesPerTile * (y - 1) + (x-1)).getXY(),stationaryObjects)){
                        Node downLeftNode = nodes.get(mapWidth*nodesPerTile * (y - 1) + (x-1));
                        targetNode.createConnection(downLeftNode,1.4f);
                    }
                    if(y != 0 && x != mapWidth*nodesPerTile - 1 && isBlockEmpty(downRight) && isBlockEmpty(down) && isBlockEmpty(right) && isFree(nodes.get(mapWidth*nodesPerTile * (y - 1) + (x+1)).getXY(),stationaryObjects)){
                        Node downRightNode = nodes.get(mapWidth*nodesPerTile * (y - 1) + (x+1));
                        targetNode.createConnection(downRightNode,1.4f);
                    }
                }
                else {
                    //System.out.println(targetNode.getIndex());
                }
            }
        }
        return new GraphImp(nodes);
    }

    public static boolean isFree(Vector2 position, ArrayList<Collider> stationaryObjects){

        Rectangle p1 = new Rectangle(position.x-0.5f,position.y-0.5f,1,1);
        for(int i = 0;i< stationaryObjects.size();i++){

            Rectangle p2 = new Rectangle(stationaryObjects.get(i).getPosition().x,stationaryObjects.get(i).getPosition().y,1,1);
            if(p1.overlaps(p2)){
                //System.out.println("RED");
                return false;
            }
        }
        return true;
    }
    public static boolean isBlockEmpty(int color){
        if(Level.BLOCK_TYPE.EMPTY.sameColor(color) || Level.BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(color)){
            return true;
        }
        else {
            return false;
        }
    }
}
