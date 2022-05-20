package io.github.srjohnathan.gdx.water.desktop;

import io.github.srjohnathan.gdx.water.core.GL;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class DGL implements GL {


    @Override
    public int glGenVertexArrays() {

        int t = GL30.glGenVertexArrays();
        return t;
    }

    @Override
    public void glBindVertexArray(int n) { GL30.glBindVertexArray(n); }

    @Override
    public int getImageId(String path) {
        return TextureLoade.loadTexture(path);
    }

    @Override
    public void glDrawBuffer() {
        GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
    }

    @Override
    public void glUniformMatrix4f(int location, float[] matrix) {

    }

    @Override
    public void glDrawArrays(int n, int c, int i) {
        GL11.glDrawArrays(n,c,i);
    }

    @Override
    public int createTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,GL11.GL_TEXTURE_2D, texture, 0);
        return texture;
    }

    @Override
    public int createDepthTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
                0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,GL11.GL_TEXTURE_2D,
                texture, 0);
        return texture;
    }

    @Override
    public int createDepthBufferAttachment(int width, int height) {


        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }

    @Override
    public int glGetProgrami(int program, int glInt) {return GL20.glGetProgrami(program, glInt);}


}
