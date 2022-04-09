package io.github.srjohnathan.gdx.water.desktop;



import java.io.FileInputStream;

import com.badlogic.gdx.graphics.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;



public class TextureLoade {
    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    public static int loadTexture(String fileName){
        int texture = 0;
        try {
            texture =  TextureLoader.loadTexture(TextureLoader.loadImage(fileName));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Loader: File not found: " + fileName);
            System.exit(-1);
        }

        return texture;
    }



}