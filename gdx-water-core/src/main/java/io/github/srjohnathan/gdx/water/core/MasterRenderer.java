package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Matrix4;


import java.util.List;


public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 100000f; // thinmatrix has 1000

    //public static final float RED = // 0.1f; // 0.9444f; // 0.5444f;
    //public static final float GREEN = // 0.4f; // 0.52f; // 0.62f;
    //public static final float BLUE = // 0.2f; // 0.79f;  // 0.69f;
    public static final float RED = 0.5444f;
    public static final float GREEN = 0.62f;
    public static final float BLUE = 0.69f;

    // 0.0035 -> 0.007
    //private static final float MAX_FOG_DENSITY = 0.007f;
    private static final float MIN_FOG_DENSITY = 0.000f;
    // 1.5 -> 5.0
    private static final float MAX_FOG_GRADIENT = 5.0f;
    //private static final float MIN_FOG_GRADIENT = 1.5f;

    //private double fogTime = 0.0;

    private Matrix4 projectionMatrix;
    private Camera35 camera35;

   // private StaticShader shader = new StaticShader();

  //  private TerrainRenderer terrainRenderer;
  //  private TerrainShader terrainShader = new TerrainShader();



   // private NormalMappingRenderer normalMapRenderer;

  //  private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(Camera35 camera35) {

        this.camera35 = camera35;
        enableCulling();
    }

    public static void enableCulling() {
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    }

    public static void disableCulling() {
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    public void cleanUp() {
     //   shader.cleanUp();
     //   terrainShader.cleanUp();
     //   normalMapRenderer.cleanUp();
    }

    public void update() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
    }


    public Matrix4 getProjectionMatrix() {
        return camera35.getProjectionMatrix();
    }

    public float getNearPlane() {
        return NEAR_PLANE;
    }

    public float getFarPlane() {
        return FAR_PLANE;
    }
}
