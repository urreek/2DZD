package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entity.Collider.BoxCollider;
import com.mygdx.game.Entity.Collider.Collider;
import com.mygdx.game.Util.Assets;

/**
 * Created by Urree on 2016-07-26.
 */
public class Box extends BoxCollider {
    private TextureRegion regBox;

    public Box(Vector2 position, Vector2 dimension, boolean isMovable, boolean isVisible) {
        super(position, dimension, isMovable, isVisible);
        init();
    }

    private void init() {
        regBox = Assets.instance.box.regBox;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(regBox, position.x, position.y, dimension.x, dimension.y);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void onCollision(Collider collider) {

    }
}

