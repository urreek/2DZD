package com.mygdx.game.Util;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.Collider.Collider;

import java.util.Iterator;

import static com.badlogic.gdx.utils.JsonValue.ValueType.array;

/**
 * Created by Urree on 2016-08-22.
 */
public class GraphPathImp implements GraphPath<Node> {
    private Array<Node> nodes = new Array<Node>();

    public GraphPathImp(){
    }
    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    @Override
    public void add(Node node) {
        nodes.add(node);
    }

    public void remove(int index){
        nodes.removeIndex(index);
    }
    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public void straightPath(Node startNode, Node endNode){
        clear();
        add(endNode);
    }
}
