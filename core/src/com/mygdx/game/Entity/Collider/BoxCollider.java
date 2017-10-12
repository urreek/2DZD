package com.mygdx.game.Entity.Collider;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Created by Urree on 2016-07-28.
 */
public abstract class BoxCollider extends Collider{
    protected Vector2 dimension;
    protected Rectangle bounds;
    protected Polygon polygon;
    protected Vector2 boundsOffset;
    public BoxCollider(Vector2 position, Vector2 dimension, boolean isMovable, boolean isVisible){
        super(position, isMovable, isVisible);
        this.dimension = dimension;
        this.bounds = new Rectangle(position.x, position.y, dimension.x, dimension.y);
        this.boundsOffset = new Vector2(1,1);
        this.polygon = new Polygon(new float[]{bounds.getX(),bounds.getY(),bounds.getX()+bounds.getWidth(),bounds.getY(),bounds.getX(),bounds.getY()+bounds.getHeight(),
                bounds.getX()+bounds.getWidth(),bounds.getY()+bounds.getHeight()});

        setOrigin(new Vector2(dimension.x/2,dimension.y/2));
    }
    public Rectangle getBounds(){
        return bounds.set(position.x+((1-boundsOffset.x)/2),position.y+((1-boundsOffset.y)/2),dimension.x*boundsOffset.x,dimension.y*boundsOffset.y);
    }
    public Polygon getPolygon(){
        return polygon;
    }

    public Vector2 getDimension() {
        return dimension;
    }

    public void setDimension(Vector2 dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean checkCollision(Collider collider) {
        boolean isCollision = false;
        if(collider instanceof BoxCollider && Intersector.overlaps(getBounds(), ((BoxCollider) collider).getBounds())){
            isCollision = true;
            onCollision(collider);
            collider.onCollision(this);
        }
        else if(collider instanceof CircleCollider && Intersector.overlaps(((CircleCollider) collider).getCircle(), getBounds()) ){
            isCollision = true;
            onCollision(collider);
            collider.onCollision(this);
        }
        return isCollision;
    }

}