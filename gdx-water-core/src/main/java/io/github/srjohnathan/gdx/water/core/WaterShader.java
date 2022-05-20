package io.github.srjohnathan.gdx.water.core;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class WaterShader extends ShaderProgram {

    private final static String VERTEX_FILE =  GET_VERTEX();
    private final static String FRAGMENT_FILE = GET_FRAGMENT();



//    private final static String VERTEX_FILE =  Gdx.files.internal("data/shader/water_vertex_shader.vert").readString();
   // private final static String FRAGMENT_FILE = Gdx.files.internal("data/shader/water_fragment_shader.frag").readString();


    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflection;
    private int location_refraction;
    private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosition;
    private int location_normalMap;
    private int location_lightColour;
    private int location_lightPosition;
    private int location_depthMap;
    private int location_skyColour;
    private int location_waterColour;

    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflection = getUniformLocation("reflectionTexture");
        location_refraction = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
        location_normalMap = getUniformLocation("normalMap");
        location_lightColour = getUniformLocation("lightColour");
        location_lightPosition = getUniformLocation("lightPosition");
        location_depthMap = getUniformLocation("depthMap");
        location_waterColour = getUniformLocation("waterColour");
        location_skyColour = super.getUniformLocation("skyColour");
    }

    public void connectTextureUnits() {
        super.loadInt(location_reflection, 0);
        super.loadInt(location_refraction, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_normalMap, 3);
        super.loadInt(location_depthMap, 4);
    }

    public void loadLight(Light sun) {
        super.loadVector(location_lightColour, sun.getColor());
        super.loadVector(location_lightPosition, sun.getPosition());
    }

    public void loadSkyColour(Vector3 colour){
        super.loadVector(location_skyColour, colour);
    }

    public void loadMoveFactor(float factor) {
        super.loadFloat(location_moveFactor, factor);
    }

    public void loadProjectionMatrix(Matrix4 projection) {
        loadMatrix(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera35 camera){
        Matrix4 viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix,viewMatrix);
        super.loadVector(location_cameraPosition, camera.getPosition());
    }

    public void setWaterColor(Color waterColor){
        if(waterColor == null){
            super.loadVector(location_waterColour, new Vector3(0.0f, 0.3f, 0.5f));

        }else {
            super.loadVector(location_waterColour, new Vector3(waterColor.r,waterColor.g,waterColor.b));

        }
    }

    public void loadModelMatrix(Matrix4 modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }


    public static  final  String GET_VERTEX (){

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            return  Gdx.files.classpath("io/github/srjohnathan/gdx/water/assets/waterVertex.vert").readString();
        }else {
            return  Gdx.files.classpath("io/github/srjohnathan/gdx/water/assets/vt.vsh").readString();
        }

    }

    public static  final  String GET_FRAGMENT (){

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            return  Gdx.files.classpath("io/github/srjohnathan/gdx/water/assets/waterFragment.frag").readString();
        }else {
            return  Gdx.files.classpath("io/github/srjohnathan/gdx/water/assets/ft.fsh").readString();
        }

    }

}
