package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


import java.util.List;

public class WaterRenderer {

    private static final String DUDV_MAP = "data/shader/waterDUDV.png";
    private static final String NORMAL_MAP = "data/shader/waterNormalMap.png";
    private static final float WAVE_SPEED = 0.08f;

    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    private Color waterColor;

    private float moveFactor = 0;

    private int dudvTexture;
    private int normalMapTexture;

    private FrameBuffer fbo1,fbo2;

    public WaterRenderer(Loader loader, WaterShader shader, Matrix4 projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            dudvTexture = loader.loadTextureNative( Gdx.files.internal( DUDV_MAP).path());
            normalMapTexture = loader.loadTextureNative(Gdx.files.internal( NORMAL_MAP).path());


        }else {
            dudvTexture = loader.loadTextureNative( Gdx.files.internal( DUDV_MAP).path());
            normalMapTexture = loader.loadTextureNative(Gdx.files.internal( NORMAL_MAP).path());
        }


        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);

        fbo1 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        FrameBuffer.FrameBufferBuilder builder = new GLFrameBuffer.FrameBufferBuilder(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        builder.addBasicColorTextureAttachment(Pixmap.Format.RGBA8888);
        fbo2 = builder.build();

    }

    public void render(List<WaterTile> water, Camera35 camera, Light sun) {

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        Gdx.gl.glCullFace(GL20.GL_FRONT);

        prepareRender(camera, sun);
        for (WaterTile tile : water) {
            Matrix4 modelMatrix = Maths.createTransformationMatrixA(new Vector3(tile.getX(), tile.getHeight(), tile.getZ()), 5, 5, 5, 4f);
            shader.loadModelMatrix(modelMatrix);
            CGL.newInstance().glDrawArrays(Gdx.gl.GL_TRIANGLES, 0, quad.getVertexCount());


        }
        unbind();

        Gdx.gl.glCullFace(GL20.GL_BACK);

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

    }

    private void prepareRender(Camera35 camera, Light sun) {
        shader.start();
        shader.loadProjectionMatrix(camera.getProjectionMatrix());
        shader.loadViewMatrix(camera);
        moveFactor += WAVE_SPEED * Gdx.graphics.getDeltaTime();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadLight(sun);
        shader.setWaterColor(waterColor);
        shader.loadSkyColour(new Vector3(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE));
        CGL.newInstance().glBindVertexArray(quad.getVaoID());
        Gdx.gl.glEnableVertexAttribArray(0);

        Gdx.gl.glActiveTexture(Gdx.gl.GL_TEXTURE0);
        Gdx.gl.glBindTexture(Gdx.gl.GL_TEXTURE_2D, fbos.getReflectionTexture());



        Gdx.gl.glActiveTexture(Gdx.gl.GL_TEXTURE1);
        Gdx.gl.glBindTexture(Gdx.gl.GL_TEXTURE_2D, fbos.getRefractionTexture());



        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE2);
        Gdx.gl.glBindTexture(Gdx.gl.GL_TEXTURE_2D, dudvTexture);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE3);
        Gdx.gl.glBindTexture(Gdx.gl.GL_TEXTURE_2D, normalMapTexture);




        Gdx.gl.glActiveTexture(Gdx.gl.GL_TEXTURE4);
        Gdx.gl.glBindTexture(Gdx.gl.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);



    }

    public void setWaterColor(Color waterColor) {
        this.waterColor = waterColor;
    }

    private void unbind() {
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
        Gdx.gl.glDisableVertexAttribArray(0);
        CGL.newInstance().glBindVertexArray(0);
        shader.stop();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader

        float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
        quad = loader.loadToVAO(vertices, 2);
    }

}
