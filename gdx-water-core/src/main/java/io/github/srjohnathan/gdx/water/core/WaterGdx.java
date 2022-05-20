package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WaterGdx implements Disposable {

    private  static   String className;

    public static boolean init (){
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            try {
                System.loadLibrary("wt");
                return  true;
            }catch (Exception e){
                e.printStackTrace();
                return  false;
            }

        }else {
            className = "io.github.srjohnathan.gdx.water.desktop.WaterGdxLouder";
            try {
                Class cls = ClassReflection.forName(className);
                try {
                    Method m = cls.getMethod("init");
                  return  (boolean)  m.invoke(null);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    return  false;
                }
            } catch (ReflectionException e) {
                e.printStackTrace();
                return  false;
            }
        }

    }


    @Override
    public void dispose() {

    }
}
