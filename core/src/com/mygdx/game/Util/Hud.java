package com.mygdx.game.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Entity.Player;

/**
 * Created by Urree on 2017-01-22.
 */
public class Hud {

    private ShapeRenderer shapeRenderer;
    private Player player;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Crosshair crosshair;

    public Hud(Player player){
        this.player = player;
        this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera(800, 600);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        crosshair = new Crosshair();
    }

    public void render (SpriteBatch batch) {
        batch.end();
        drawHpBar(batch);
        crosshair.render(shapeRenderer, camera);
        batch.begin();
    }

    public void drawHpBar (SpriteBatch batch){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(new Color(0.1f,0.1f,0.1f,1f));
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(-400, -300, 800, 100);
        shapeRenderer.setColor(new Color(1f,0f,0f,1f));
        shapeRenderer.rect(-300, -230, 150, 10);
        shapeRenderer.end();
        batch.begin();
        batch.setShader(null);
        batch.setProjectionMatrix(camera.combined);
        font.draw(batch, "Health", -350, -230 + font.getCapHeight());
        batch.end();
    }

}
