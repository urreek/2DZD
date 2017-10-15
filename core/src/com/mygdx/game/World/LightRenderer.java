package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Entity.Light;
import com.mygdx.game.Util.Constants;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Urree on 2016-07-22.
 */
public class LightRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    private float aspectRatio;

    private int lightRadius;
    private int lightSize;

    private float upScale = 1f; //for example; try lightSize=128, upScale=1.5f

    private TextureRegion finalTex;
    private TextureRegion shadowMap1D; //1 dimensional shadow map
    private TextureRegion occluders;   //occluder map
    private FrameBuffer shadowMapFBO;
    private FrameBuffer occludersFBO;
    private FrameBuffer finalFBO;


    private ShaderProgram shadowMapShader, shadowRenderShader, ambientShader;

    private ArrayList<Light> lights;

    private boolean additive = true;
    private boolean softShadows = true;
    public static final float ambientIntensity = 0.7f;
    public static final Vector3 ambientColor = new Vector3(0.3f, 0.3f, 0.7f);

    public LightRenderer (WorldController worldController,OrthographicCamera camera, SpriteBatch batch) {
        this.worldController = worldController;
        this.batch = batch;
        this.camera = camera;
        init();
    }
    private void init () {
        lights = worldController.getLights();
        ShaderProgram.pedantic = false;

        //read vertex pass-through shader
        final String VERT_SRC = Gdx.files.internal("shaders/pass.vert").readString();


        // renders occluders to 1D shadow map
        shadowMapShader = createShader(VERT_SRC, Gdx.files.internal("shaders/shadowMap.frag").readString());
        // samples 1D shadow map to create the blurred soft shadow
        shadowRenderShader = createShader(VERT_SRC, Gdx.files.internal("shaders/shadowRender.frag").readString());

        ambientShader = createShader(VERT_SRC, Gdx.files.internal("shaders/ambientLight.frag").readString());

        lightSize = 1024;
        lightRadius = 1;
        //build frame buffers
        finalFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        finalTex = new TextureRegion(finalFBO.getColorBufferTexture());
        finalTex.flip(false, true);

        occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
        occluders.flip(false, true);

        //our 1D shadow map, lightSize x 1 pixels, no depth
        shadowMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, 1, false);
        Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();

        //use linear filtering and repeat wrap mode when sampling
        shadowMapTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        //for debugging only; in order to render the 1D shadow map FBO to screen
        shadowMap1D = new TextureRegion(shadowMapTex);
        shadowMap1D.flip(false, true);

        ambientShader.begin();
        ambientShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        ambientShader.end();

        aspectRatio = Constants.APP_HEIGHT / Constants.APP_WIDTH;
    }

    public void render () {
        //clear frame
        finalFBO.begin();
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        finalFBO.end();

        if (additive)
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        for (Light light:lights) {
            renderLight(light);
        }
        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        camera.setToOrtho(false,Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * aspectRatio);
        worldController.getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();
        batch.draw(finalTex, camera.position.x-Constants.VIEWPORT_WIDTH*camera.zoom/2, camera.position.y-Constants.VIEWPORT_HEIGHT*camera.zoom * aspectRatio/2, Constants.VIEWPORT_WIDTH*camera.zoom, Constants.VIEWPORT_HEIGHT*camera.zoom * aspectRatio);
        batch.end();
        camera.setToOrtho(false,Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * aspectRatio);
        //reset color
        batch.setShader(null);

        if (additive)
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }


    public void renderLight(Light o) {

        float mx = o.getPosition().x;
        float my = o.getPosition().y;

        //STEP 1. render light region to occluder FBO
        lightRadius = o.getRadius();
        //bind the occluder FBO
        occludersFBO.begin();

        //clear the FBO
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set the orthographic camera to the size of our FBO
        camera.viewportWidth = lightRadius*2;
        camera.viewportHeight = lightRadius*2;
        //translate camera so that light is in the center
        camera.position.x = mx;
        camera.position.y = my;
        //update camera matrices
        camera.update();

        //set up our batch for the occluder pass
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null); //use default shader
        batch.begin();
        // ... draw any sprites that will cast shadows here ... //
        //batch.draw(casterSprites, 0, 0);
        worldController.getLevel().renderShadows(batch);
        //end the batch before unbinding the FBO
        batch.end();

        //unbind the FBO
        occludersFBO.end();

        //STEP 2. build a 1D shadow map from occlude FBO

        //bind shadow map
        shadowMapFBO.begin();

        //clear it
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set our shadow map shader
        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", lightSize, lightSize);
        shadowMapShader.setUniformf("upScale", upScale);

        //reset our projection matrix to the FBO size
        camera.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        batch.setProjectionMatrix(camera.combined);

        //draw the occluders texture to our 1D shadow map FBO
        batch.draw(occluders.getTexture(), 0, 0, lightSize, shadowMapFBO.getHeight());

        //flush batch
        batch.end();

        //unbind shadow map FBO
        shadowMapFBO.end();

        //STEP 3. render the blurred shadows
        finalFBO.begin();
        //reset projection matrix to VIEWPORT
        camera.setToOrtho(false,Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * aspectRatio);
        worldController.getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);

        //set the shader which actually draws the light/shadow
        batch.begin();

        shadowRenderShader.setUniformf("resolution", lightSize, lightSize);
        shadowRenderShader.setUniformf("softShadows", softShadows ? 1f : 0f);
        //set color to light
        batch.setColor(o.getColor());

        //draw centered on light position
        batch.setShader(shadowRenderShader);
        batch.draw(shadowMap1D.getTexture(), mx-lightRadius, my-lightRadius, lightRadius*2, lightRadius*2);
        //flush the batch before swapping shaders
        batch.end();
        finalFBO.end();

        batch.setColor(Color.WHITE);

    }


    public void resize (int width, int height)
    {
        aspectRatio = (float)height/(float)width;
       /* camera.viewportWidth = Constants.VIEWPORT_WIDTH;
        camera.viewportHeight = Constants.VIEWPORT_HEIGHT * aspectRatio;
        camera.update();*/
    }
    @Override public void dispose () {
        batch.dispose();
        occludersFBO.dispose();
        occluders.getTexture().dispose();
        shadowMapFBO.dispose();
        shadowMap1D.getTexture().dispose();

    }

    public static ShaderProgram createShader(String vert, String frag) {
        ShaderProgram prog = new ShaderProgram(vert, frag);
        if (!prog.isCompiled())
            throw new GdxRuntimeException("could not compile shader: " + prog.getLog());
        if (prog.getLog().length() != 0)
            Gdx.app.log("GpuShadows", prog.getLog());
        return prog;
    }
}