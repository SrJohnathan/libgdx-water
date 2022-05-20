package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CGL implements GL {

    String className = "";
    private Class<? extends GL> gl;
    private GL cgl;
    private  static GL gll;


    public static  GL newInstance(){
        if(gll == null){
            gll = new CGL();
        }
        return  gll;
    }

    private CGL(){

        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            className = "io.github.srjohnathan.gdx.water.android.AGL";

        } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) {

            className = "io.github.srjohnathan.gdx.water.desktop.DGL";
        }


        try {
            gl = ClassReflection.forName(className);

            cgl = gl.newInstance();

            for (Method m : gl.getDeclaredMethods()){
                System.out.println(m.getName());
            }


        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }



    @Override
    public int glGenVertexArrays() {

        try {

            Method method = gl.getDeclaredMethod("glGenVertexArrays");
            Object o =  method.invoke(cgl);


            return  Integer.parseInt(o.toString());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void glBindVertexArray(int n) {

        try {

            Method method = gl.getDeclaredMethod("glBindVertexArray",int.class);
            Object o =  method.invoke(cgl,n);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getImageId(String path) {
        try {

            Method method = gl.getDeclaredMethod("getImageId",String.class);
            Object o =  method.invoke(cgl,path);

            return  Integer.parseInt(o.toString());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void glDrawBuffer() {

        try {

            Method method = gl.getDeclaredMethod("glDrawBuffer");
            Object o =  method.invoke(cgl);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void glUniformMatrix4f(int location, float[] matrix) {
        try {

            Method method = gl.getDeclaredMethod("glUniformMatrix4f",int.class,float[].class );
            Object o =  method.invoke(cgl,location,matrix);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void glDrawArrays(int n, int c, int i) {

        try {

            Method method = gl.getDeclaredMethod("glDrawArrays",int.class,int.class,int.class );
            Object o =  method.invoke(cgl,n,c,i);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTextureAttachment(int width, int height) {
        try {

            Method method = gl.getDeclaredMethod("createTextureAttachment",int.class,int.class );
            Object o =  method.invoke(cgl, width, height);

            return  Integer.parseInt(o.toString());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int createDepthTextureAttachment(int width, int height) {


        try {

            Method method = gl.getDeclaredMethod("createDepthTextureAttachment",int.class,int.class );
            Object o =  method.invoke(cgl, width, height);

            return  Integer.parseInt(o.toString());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }




        return 0;
    }

    @Override
    public int createDepthBufferAttachment(int width, int height) {

        try {

            Method method = gl.getDeclaredMethod("createDepthBufferAttachment",int.class,int.class );
            Object o =  method.invoke(cgl, width, height);

            return  Integer.parseInt(o.toString());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public int glGetProgrami(int program, int glInt) {
        return 0;
    }


}
