package com.mygdx.game.Util;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.mygdx.game.World.Level;

/**
 * Created by Urree on 2016-08-22.
 */
public class HeuristicImp implements Heuristic<Node> {
    @Override
    public float estimate(Node startNode, Node endNode) {
        int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startY = startIndex / (Level.levelHeight*GraphGenerator.nodesPerTile);
        int startX = startIndex % (Level.levelWidth*GraphGenerator.nodesPerTile);

        int endY = endIndex / (Level.levelHeight*GraphGenerator.nodesPerTile);
        int endX = endIndex % (Level.levelWidth*GraphGenerator.nodesPerTile);

        float distance = (float)Math.sqrt(Math.pow(Math.abs(startX - endX),2) + Math.pow(Math.abs(startY - endY),2));

        return distance;
    }
}
