package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;





import java.nio.ByteBuffer;

public class WaterFrameBuffers {

    static final int divisor1 = 2;
    static final int divisor2 = 1;

    protected static final int REFLECTION_WIDTH = 320 / divisor1;
    private static final int REFLECTION_HEIGHT = 240 / divisor1;
    
    protected static final int REFRACTION_WIDTH = 320 / divisor2;
    private static final int REFRACTION_HEIGHT = 240 / divisor2;

    private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
    
    private int refractionFrameBuffer;
    private int refractionTexture;
    private int refractionDepthTexture;

    public WaterFrameBuffers() {//call when loading the game
        initializeReflectionFrameBuffer();
        initializeRefractionFrameBuffer();
    }

    public void cleanUp() {//call when closing the game
        Gdx.gl.glDeleteFramebuffer(reflectionFrameBuffer);
        Gdx.gl.glDeleteTexture(reflectionTexture);
        Gdx.gl.glDeleteRenderbuffer(reflectionDepthBuffer);
        Gdx.gl.glDeleteFramebuffer(refractionFrameBuffer);
        Gdx.gl.glDeleteTexture(refractionTexture);
        Gdx.gl.glDeleteTexture(refractionDepthTexture);
    }

    public void bindReflectionFrameBuffer() {//call before rendering to this FBO
        bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
    }
    
    public void bindRefractionFrameBuffer() {//call before rendering to this FBO
        bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);
    }
    
    public void unbindCurrentFrameBuffer() {//call after rendering to texture
        Gdx.gl.glBindFramebuffer(Gdx.gl.GL_FRAMEBUFFER, 0);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public int getReflectionTexture() {//get the resulting texture
        return reflectionTexture;
    }
    
    public int getRefractionTexture() {//get the resulting texture
        return refractionTexture;
    }
    
    public int getRefractionDepthTexture(){//get the resulting depth texture
        return refractionDepthTexture;
    }

    private void initializeReflectionFrameBuffer() {
        reflectionFrameBuffer = createFrameBuffer();
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }
    
    private void initializeRefractionFrameBuffer() {
        refractionFrameBuffer = createFrameBuffer();
        refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }
    
    private void bindFrameBuffer(int frameBuffer, int width, int height){
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        Gdx.gl.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        Gdx.gl.glViewport(0, 0, width, height);
    }

    private int createFrameBuffer() {
        //generate name for frame buffer
        int frameBuffer = Gdx.gl.glGenFramebuffer();
        //generate name for frame buffer
        Gdx.gl.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //indicate that we will always render to color attachment 0
      //  Gdx.gl30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        CGL.newInstance().glDrawBuffer();
        return frameBuffer;
    }

    private int createTextureAttachment( int width, int height) {
        return CGL.newInstance().createTextureAttachment(width,height);
    }
    
    private int createDepthTextureAttachment(int width, int height){
       return CGL.newInstance().createDepthTextureAttachment(width,height);
    }

    private int createDepthBufferAttachment(int width, int height) {
       return  CGL.newInstance().createDepthBufferAttachment(width,height);
    }

}
