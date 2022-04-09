package io.github.srjohnathan.gdx.water.core;



import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.profiling.GL30Interceptor;
import com.badlogic.gdx.utils.BufferUtils;

import java.io.FileInputStream;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

public class Loader {

	// OpenGL 3D Game Tutorial 20: Mipmapping: level of detail bias
    private final static float LOD_BIAS = -0.4f;

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    // OpenGL 3D Game Tutorial 24: Rendering GUIs
    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions);
    }

    public int loadTexture(String filepath) {
        Texture texture = new Texture( Gdx.files.internal(filepath) );
        return texture.getTextureObjectHandle();
    }


    public int loadTextureNative(String filepath) {
       return   CGL.newInstance().getImageId(filepath);
    }

    public Texture loadTextureT(String filepath) {
        Texture texture = new Texture( Gdx.files.internal(filepath) );


        textures.add(texture.getTextureObjectHandle());
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        //-If the texture on the terrain only looks good in one corner then try adding these 2 lines into your loadTexture
        // method before returning the texture's ID:
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return texture;
    }

    private int[] convert(List<Integer> integers){
        int[] i = new int[integers.size()];
        for (int v = 0 ; v < integers.size() ; v++){
            i[v] = integers.get(v);
        }

        return  i;
    }

    public void cleanUp() {

        int[] v = convert(vaos);

            Gdx.gl30.glDeleteVertexArrays(v.length,v,0);

        for (int vbo : vbos) {
            Gdx.gl.glDeleteBuffer(vbo);
        }
        for (int texture : textures) {
            Gdx.gl.glDeleteTexture(texture);
        }
    }

    private int createVAO() {


        int vaoID =  CGL.newInstance().glGenVertexArrays();
        vaos.add(vaoID);
        CGL.newInstance().glBindVertexArray(vaoID);
        return vaoID;
    }

    // attributes are the rows in the VAO: 0, 1, 2, etc.
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {

        int vboID = Gdx.gl.glGenBuffer();
        vbos.add(vboID);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer;
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            buffer = storeDataInFloatBufferA(data);
        }else  {
            buffer = storeDataInFloatBuffer(data);
        }
        Gdx.gl.glBufferData(GL20.GL_ARRAY_BUFFER,data.length*4,buffer, GL20.GL_STATIC_DRAW);
        Gdx.gl.glVertexAttribPointer(attributeNumber, coordinateSize, GL20.GL_FLOAT, false, 0, 0);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);


    }

    private void unbindVAO() {
        CGL.newInstance().glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = Gdx.gl.glGenBuffer();
        vbos.add(vboID);
        Gdx.gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer;
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            buffer  = storeDataInIntBufferA(indices);
        }else {
            buffer  = storeDataInIntBuffer(indices);
        }


        Gdx.gl.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER,16, buffer, GL20.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.newIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.newFloatBuffer(data.length);
        buffer.put(data);
         buffer.flip();
        return buffer;
    }



    private IntBuffer storeDataInIntBufferA(int[] data) {
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length*4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer buffer = bb.asIntBuffer();
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBufferA(float[] data) {
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();

        buffer.put(data);
        ( (Buffer)  buffer).flip();
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();

        return buffer;
    }

    public static IntBuffer createIntBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size*1);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer buffer = bb.asIntBuffer();

        return buffer;
    }

}
