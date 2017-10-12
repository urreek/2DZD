package com.mygdx.game.Entity.Collider;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entity.GameObject;

/**
 * Created by Urree on 2016-07-28.
 */
public abstract class Collider extends GameObject {


    public abstract boolean checkCollision(Collider collider);
    public abstract void onCollision(Collider collider);

    public Collider(Vector2 position, boolean isMovable, boolean isVisible){
        super(position, isMovable, isVisible);

    }

}
