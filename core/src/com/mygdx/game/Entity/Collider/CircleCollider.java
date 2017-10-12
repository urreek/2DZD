package com.mygdx.game.Entity.Collider;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Urree on 2016-07-28.
 */
public abstract class CircleCollider extends Collider {
    protected int radius;

    public CircleCollider(Vector2 position, int radius , boolean isMovable, boolean isVisible){
        super(position, isMovable, isVisible);
        this.radius = radius;
        setOrigin(new Vector2(radius,radius));
    }


    public Circle getCircle(){
        return new Circle(position, radius);
    }

    @Override
    public boolean checkCollision(Collider collider) {
        boolean isCollision = false;
        if(collider instanceof CircleCollider && Intersector.overlaps(getCircle(), ((CircleCollider) collider).getCircle())){
            isCollision = true;
            onCollision(collider);
            collider.onCollision(this);
        }
        else if(collider instanceof BoxCollider && Intersector.overlaps(getCircle(), ((BoxCollider)collider).getBounds()) ){
            isCollision = true;
            onCollision(collider);
            collider.onCollision(this);
        }
        return isCollision;
    }
}
