package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;



public class Camera35 implements Camera {

    private final float MIN_DISTANCE_FROM_PLAYER = 1;
    //private final float MAX_DISTANCE_FROM_PLAYER = 1000;
    private final float MIN_PITCH = -90;
    private final float MAX_PITCH = 90;
    private final float CAMERA_Y_OFFSET = 7;
    private final float CAMERA_PITCH_OFFSET = 0; // ThinMatrix has 4
    private final float ZOOM_LEVEL_FACTOR = 0.1f;
    private final float PITCH_CHANGE_FACTOR = 0.2f;
    private final float ANGLE_AROUND_PLAYER_CHANGE_FACTOR = 0.3f;

    private float distanceFromPlayer = 100;
    private float angleAroundPlayer = 0;

    private Vector3 position = new Vector3(0, 0, 0);
    private float pitch = 15.0f; // high or low
    private float yaw = 0;   // left or right
    private float roll;  // tilt: 180 deg = upside down
    private int moves = 0;


    private PerspectiveCamera perspectiveCamera;


    public Camera35(PerspectiveCamera perspectiveCamera) {

        this.perspectiveCamera = perspectiveCamera;

    }

    public void move() {

        perspectiveCamera.update();
        calculateCameraPosition();

    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getDirecion() {
        return perspectiveCamera.direction;
    }

    @Override
    public void printPosition() {

    }

    public Matrix4 getProjectionMatrix() {
        return perspectiveCamera.projection;
    }
    public void calculateCameraPosition() {

        position.x = perspectiveCamera.position.x;
        position.z = perspectiveCamera.position.z;
        position.y = perspectiveCamera.position.y;
    }

    public Matrix4 getViewMatrix() {
        return perspectiveCamera.view;
    }




}
