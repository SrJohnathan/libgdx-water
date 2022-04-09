package io.github.srjohnathan.gdx.water.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Build;
import android.widget.Toast;
import io.github.srjohnathan.gdx.water.core.GL;

import java.nio.ByteBuffer;
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AGL implements GL {
    @Override
    public int glGenVertexArrays() {
        int[] v = new int[1];
        GLES30.glGenVertexArrays(1,v,0);
        return v[0];
    }

    @Override
    public void glBindVertexArray(int n) {
        GLES30.glBindVertexArray(n);
    }

    @Override
    public int getImageId(String path) {
        int i = Loader.loadTexture(ContextWater.context,path);
        return   i;
    }

    @Override
    public void glDrawBuffer() {
        int[] targets = {GLES30.GL_COLOR_ATTACHMENT0};
        GLES30.glDrawBuffers(1, targets, 0);

      //  GLES20.GL_DEPTH_
    }

    @Override
    public void glUniformMatrix4f(int location, float[] matrix) {
        GLES30.glUniformMatrix4fv(location, 1,false,matrix ,0);
    }

    @Override
    public void glDrawArrays(int n, int c, int i) {

        GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,c,i);
        GLES11.glDisableClientState(GLES11.GL_VERTEX_ARRAY);


    }

    @Override
    public int createTextureAttachment(int width, int height) {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0]);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB, width, height, 0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, texture[0], 0);
        return texture[0];
    }

    @Override
    public int createDepthTextureAttachment(int width, int height) {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture[0]);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_DEPTH_COMPONENT32F, width, height, 0, GLES30.GL_DEPTH_COMPONENT, GLES30.GL_FLOAT, (ByteBuffer) null);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_TEXTURE_2D, texture[0], 0);
        return texture[0];
    }

    @Override
    public int createDepthBufferAttachment(int width, int height) {
        int[] depthBuffer = new int[1];
        GLES30.glGenRenderbuffers(1, depthBuffer, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, depthBuffer[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, width, height);
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, depthBuffer[0]);
        return depthBuffer[0];
    }


}
