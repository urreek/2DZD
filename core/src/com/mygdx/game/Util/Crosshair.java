package com.mygdx.game.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by urimkrasniqi on 2017-04-14.
 */
public class Crosshair {

    public Crosshair(){
        Gdx.input.setCursorCatched(true);
    }

    public void render (ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        float x = MathUtils.clamp(Gdx.input.getX(),0,Gdx.graphics.getWidth());
        float y = MathUtils.clamp(Gdx.input.getY(),0,Gdx.graphics.getHeight());
        Gdx.input.setCursorPosition((int)x,(int)y);

        Vector3 screenPosition = new Vector3(x,y,0);
        Vector3 position = camera.unproject(screenPosition);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rectLine(position.x-10f, position.y, position.x-5f, position.y, 2);
        shapeRenderer.rectLine(position.x+5f, position.y, position.x+10f, position.y, 2);
        shapeRenderer.rectLine(position.x, position.y-10, position.x, position.y-5, 2);
        shapeRenderer.rectLine(position.x, position.y+5, position.x, position.y+10, 2);
        shapeRenderer.end();
    }
}
