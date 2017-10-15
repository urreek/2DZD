package com.mygdx.game.Util;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World.Level;

/**
 * Created by Urree on 2016-08-22.
 */
public class GraphImp extends DefaultIndexedGraph<Node> {

    public GraphImp(){
        super();
    }
    public GraphImp(int capacity) {
        super(capacity);
    }

    public GraphImp(Array<Node> nodes){
        super(nodes);
    }
    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return super.getConnections(fromNode);
    }

    @Override
    public int getNodeCount() {
        return super.getNodeCount();
    }

    public Node getNodeByXY(float x, float y){
        int modX = (int)(x*GraphGenerator.nodesPerTile);
        int modY = (int)(y*GraphGenerator.nodesPerTile);
        return nodes.get(Level.levelWidth*GraphGenerator.nodesPerTile * modY + modX);
    }

}
