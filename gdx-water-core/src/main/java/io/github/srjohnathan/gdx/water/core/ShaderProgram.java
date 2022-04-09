package io.github.srjohnathan.gdx.water.core;



import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer;

    public ShaderProgram(String vertexFile, String fragmentFile) {

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            matrixBuffer = Loader.createFloatBuffer(16);
        }else {
            matrixBuffer =   BufferUtils.newFloatBuffer(16);
        }



        com.badlogic.gdx.graphics.glutils.ShaderProgram program = new com.badlogic.gdx.graphics.glutils.ShaderProgram(vertexFile,fragmentFile);



        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = Gdx.gl.glCreateProgram();
        Gdx.gl.glAttachShader(programID, vertexShaderID);
        Gdx.gl.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        Gdx.gl.glLinkProgram(programID);
        Gdx.gl.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return Gdx.gl.glGetUniformLocation(programID, uniformName);
    }

    public void start() {
        Gdx.gl.glUseProgram(programID);
    }

    public void stop() {
        Gdx.gl.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        Gdx.gl.glDetachShader(programID, vertexShaderID);
        Gdx.gl.glDetachShader(programID, fragmentShaderID);
        Gdx.gl.glDeleteShader(vertexShaderID);
        Gdx.gl.glDeleteShader(fragmentShaderID);
        Gdx.gl.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        Gdx.gl.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        Gdx.gl.glUniform1f(location, value);
    }

    protected void loadInt(int location, int value) {
        Gdx.gl.glUniform1i(location, value);
    }

    protected void load2DVector(int location, Vector2 vector) {
        Gdx.gl.glUniform2f(location, vector.x, vector.y);
    }

    protected void loadVector(int location, Vector3 vector) {
        Gdx.gl.glUniform3f(location, vector.x, vector.y, vector.z);
    }



    protected void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        Gdx.gl.glUniform1f(location, toLoad);
    }




    protected void loadMatrix(int location, Matrix4 matrix) {






         if(Gdx.app.getType() == Application.ApplicationType.Android){
             CGL.newInstance().glUniformMatrix4f(location,matrix.val);
         }else {


             matrixBuffer.put(matrix.val[0]);
             matrixBuffer.put(matrix.val[1]);
             matrixBuffer.put(matrix.val[2]);
             matrixBuffer.put(matrix.val[3]);
             matrixBuffer.put(matrix.val[4]);
             matrixBuffer.put(matrix.val[5]);
             matrixBuffer.put(matrix.val[6]);
             matrixBuffer.put(matrix.val[7]);
             matrixBuffer.put(matrix.val[8]);
             matrixBuffer.put(matrix.val[9]);
             matrixBuffer.put(matrix.val[10]);
             matrixBuffer.put(matrix.val[11]);
             matrixBuffer.put(matrix.val[12]);
             matrixBuffer.put(matrix.val[13]);
             matrixBuffer.put(matrix.val[14]);
             matrixBuffer.put(matrix.val[15]);

             matrixBuffer.flip();
             Gdx.gl.glUniformMatrix4fv(location,1, false, matrixBuffer);
         }





    }

    private static int loadShader(String file, int type) {
        int shaderID = Gdx.gl.glCreateShader(type);
        Gdx.gl.glShaderSource(shaderID, file);
        Gdx.gl.glCompileShader(shaderID);

         IntBuffer  g = BufferUtils.newIntBuffer(1);

        Gdx.gl.glGetShaderiv(shaderID, GL20.GL_COMPILE_STATUS, g);
        if (g.get(0) == 0) {
            Gdx.gl.glDeleteShader(shaderID);
            shaderID = 0;
            Gdx.app.log("LOG",Gdx.gl.glGetShaderInfoLog(shaderID));
            // throw new GdxRuntimeException(Gdx.gl.glGetShaderInfoLog(shaderID));
        }

        return shaderID;
    }
}
