package com.mygdx.game.Util;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World.Level;

/**
 * Created by Urree on 2016-08-22.
 */

public class Node implements IndexedNode<Node> {

    private Array<Connection<Node>> connections = new Array<Connection<Node>>();
    public int type;
    public int index;

    public Node() {
        index = Node.Indexer.getIndex();
    }
    private static class Indexer {
        private static int index = 0;

        public static int getIndex() {
            return index++;
        }
    }
    public void createConnection(Node toNode, float cost){
        connections.add(new ConnectionImp(this, toNode, cost));
    }

    public Vector2 getXY(){
        return new Vector2(0.5f+(float)(index % (Level.levelWidth*GraphGenerator.nodesPerTile))/GraphGenerator.nodesPerTile,0.5f+(float)(index / (Level.levelWidth*GraphGenerator.nodesPerTile))/GraphGenerator.nodesPerTile);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public static class Type {
        public static final int REGULAR = 1;
    }
}
