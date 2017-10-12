package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Urree on 2016-07-30.
 */
public class Light {

    private Vector2 position;
    private Color color;
    private int radius;


    public void setColor(Color color) {
        this.color = color;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public int getRadius() {
        return radius;
    }

    public Light(int radius, Vector2 position, Color color) {
        this.radius = radius;
        this.position = position;
        this.color = color;
    }
}
