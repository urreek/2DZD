package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entity.GameObject;
import com.mygdx.game.Util.Assets;

/**
 * Created by Urree on 2016-08-10.
 */
public class Grass extends GameObject {

    private Vector2 dimension;

    public Grass(Vector2 position, Vector2 dimension, boolean isMovable, boolean isVisible) {
        super(position, isMovable, isVisible);
        this.dimension = dimension;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Assets.instance.grass.regGrass, position.x, position.y, dimension.x, dimension.y);
    }

    @Override
    public void update(float deltaTime) {

    }
}
