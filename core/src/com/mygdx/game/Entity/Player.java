package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.mygdx.game.Entity.Collider.BoxCollider;
import com.mygdx.game.Entity.Collider.Collider;
import com.mygdx.game.Util.*;

/**
 * Created by Urree on 2016-07-27.
 */
public class Player extends BoxCollider {
    public static final String TAG = Player.class.getName();
    private ShapeRenderer shapeRenderer;
    public enum VIEW_DIRECTION { DOWN, LEFT, RIGHT, UP }
    private boolean walking;
    private VIEW_DIRECTION viewDirection;
    private VIEW_DIRECTION lastDirection;
    private float startTime;
    private Vector2 centerPosition;
    private Vector2 oldPosition;
    private Vector2 maxVelocity;
    private Vector2 velocity;
    private float distance;
    private Vector3 destPosition;
    private Light light;
    public int index = 0;

    private Hud hud;
    private int hp;

    public Player (Vector2 position, Vector2 dimension, boolean isMovable , boolean isVisible) {
        super(position, dimension, isMovable, isVisible);
        init();
    }
    private void init () {
        hp = Constants.PLAYER_MAX_HP;
        centerPosition = new Vector2(position.x+origin.x,position.y+origin.y);
        light = new Light(4,centerPosition, Color.WHITE);
        shapeRenderer = new ShapeRenderer();
        oldPosition = new Vector2(position.x,position.y);
        boundsOffset.set(0.6f,0.8f);
        maxVelocity = new Vector2(Constants.PLAYER_SPEED,Constants.PLAYER_SPEED);
        velocity = new Vector2(0,0);
        viewDirection = VIEW_DIRECTION.DOWN;
        walking = false;
        hp = 100;
        startTime = 0f;
        hud = new Hud(this);
    };

    @Override
    public void render (SpriteBatch batch) {
        startTime += Gdx.graphics.getDeltaTime();

        switch (viewDirection){
            case DOWN:
                if(walking){
                batch.draw(Assets.instance.player.playerDown.getKeyFrame(startTime), position.x,position.y,dimension.x,dimension.y);
                    break;
            }
            else{
                batch.draw(Assets.instance.player.playerStandingDown,position.x,position.y,dimension.x,dimension.y);
                    break;
            }
            case LEFT:
                if(walking){
                    batch.draw(Assets.instance.player.playerLeft.getKeyFrame(startTime), position.x,position.y,dimension.x,dimension.y);
                    break;
                }
                else{
                    batch.draw(Assets.instance.player.playerStandingLeft,position.x,position.y,dimension.x,dimension.y);
                    break;
                }
            case RIGHT:
                if(walking){
                    batch.draw(Assets.instance.player.playerRight.getKeyFrame(startTime), position.x,position.y,dimension.x,dimension.y);
                    break;
                }
                else{
                    batch.draw(Assets.instance.player.playerStandingRight,position.x,position.y,dimension.x,dimension.y);
                    break;
                }
            case UP:
                if(walking){
                    batch.draw(Assets.instance.player.playerUp.getKeyFrame(startTime), position.x,position.y,dimension.x,dimension.y);
                    break;
                }
                else{
                    batch.draw(Assets.instance.player.playerStandingUp,position.x,position.y,dimension.x,dimension.y);
                    break;
                }
        }
    /*
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.rect(getBounds().x, getBounds().y, getBounds().getWidth(), getBounds().getHeight());
        shapeRenderer.end();
        batch.begin();*/
        hud.render(batch);
    }

    @Override
    public void update (float deltaTime) {
        if(velocity.x != 0 || velocity.y != 0){
            walking = true;
            updateViewDirection();
            updatePosition(deltaTime);
        }   else{
            walking = false;
        }
    }



    public void updateViewDirection(){
        if(velocity.x > 0){
            viewDirection = VIEW_DIRECTION.RIGHT;
        }
        if(velocity.x < 0){
            viewDirection = VIEW_DIRECTION.LEFT;
        }
        if(velocity.y > 0){
            viewDirection = VIEW_DIRECTION.UP;
        }
        if(velocity.y < 0){
            viewDirection = VIEW_DIRECTION.DOWN;
        }

    }

    public void updatePosition(float deltaTime){
        oldPosition.set(position);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        centerPosition.set(position.x+origin.x, position.y+origin.y);
    }


    @Override
    public void onCollision(Collider collider) {
        if(collider instanceof Box){
            if(Intersector.overlaps(new Rectangle(oldPosition.x+((1-boundsOffset.x)/2),position.y+((1-boundsOffset.y)/2),dimension.x*boundsOffset.x,dimension.y*boundsOffset.y), ((Box) collider).getBounds())){
                position.y = oldPosition.y;
            }
            if(Intersector.overlaps(new Rectangle(position.x+((1-boundsOffset.x)/2),oldPosition.y+((1-boundsOffset.y)/2),dimension.x*boundsOffset.x,dimension.y*boundsOffset.y), ((Box) collider).getBounds())){
                position.x = oldPosition.x;
            }
        }
    }

    public void setViewDirection(VIEW_DIRECTION viewDirection) {
        this.viewDirection = viewDirection;
    }

    public Light getLight() {
        return light;
    }
    public Vector2 getMaxVelocity() {
        return maxVelocity;
    }
    public Vector2 getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }
    public void setVelocity(float vx, float vy) {
        this.velocity.set(vx, vy);
    }

    public float getDistance() {
        return distance;
    }
    public Vector2 getCenterPosition() {
        return centerPosition;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }
    public Vector3 getDestPosition() {
        return destPosition;
    }
    public void setDestPosition(Vector3 destPosition) {
        this.destPosition = destPosition;
    }
};

