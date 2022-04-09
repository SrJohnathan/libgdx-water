package io.github.srjohnathan.gdx.water.android;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.List;

import android.opengl.*;
import com.badlogic.gdx.graphics.TextureData;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Build;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Loader {
    private Context context;

    private List<int[]> vaos = new ArrayList<int[]>();
    private List<int[]> vbos = new ArrayList<int[]>();
    private static List<int[]> textures = new ArrayList<int[]>();

    public Loader(Context context) {
        this.context = context;
    }

    public static int loadTexture(Context context,String resourceId) {
        final int[] textureHandle = new int[1];

        GLES30.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false; // No pre-scaling

            // Read  in the resource
            InputStream ims = null;
            try {
                 ims = context.getAssets().open(resourceId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(ims);

            // Bind to the texture in OpenGL.
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle[0]);





            // Set filtering.
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);
        // GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_LOD_BIAS, -0.4f);

        textures.add(textureHandle);

        return textureHandle[0];
    }

    public void cleanUp() {
        for (int[] texture : textures) {
            GLES30.glDeleteTextures(1, texture, 0);
        }
    }

    public int loadCubeMap(int[] textureFiles) {
        int[] texID = new int[1];
        GLES30.glGenTextures(1, texID, 0);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, texID[0]);

        for (int i = 0; i < textureFiles.length; i++) {
            // TextureData data = decodeTextureFile("res/drawable-xhdpi/" + textureFiles[i] + ".png");
          //  TextureData data = decodeTextureFile(textureFiles[i]);
          //  GLES30.glTexImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GLES30.GL_RGBA, data.getWidth(), data.getHeight(), 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

        textures.add(texID);
        return texID[0];
    }

   /* private TextureData decodeTextureFile(int fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            // FileInputStream in = new FileInputStream(fileName);
            // InputStream in = getClass().getResourceAsStream(fileName);
            InputStream in = this.context.getResources().openRawResource(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }

       // return new TextureData(buffer, width, height);

        return  null;
    } */




}