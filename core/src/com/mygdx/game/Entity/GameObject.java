package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Urree on 2016-07-26.
 */
public abstract class GameObject {
    protected Vector2 position;
    protected Vector2 origin;
    protected boolean isMovable;
    protected boolean isVisible;


    public GameObject (Vector2 position, boolean isMovable, boolean isVisible) {
        this.position = position;
        this.origin = new Vector2();
        this.isMovable = isMovable;
        this.isVisible = isVisible;
    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    public abstract void render (SpriteBatch batch);
    public abstract void update (float deltaTime);
}

