package com.mygdx.game.Util;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by Urree on 2016-08-22.
 */
public class ConnectionImp implements Connection<Node> {

    private Node toNode;
    private Node fromNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}
