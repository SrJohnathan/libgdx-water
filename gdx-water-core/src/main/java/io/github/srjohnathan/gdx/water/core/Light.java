package io.github.srjohnathan.gdx.water.core;


import com.badlogic.gdx.math.Vector3;

public class Light {

    private Vector3 position;
    private Vector3 color;
    private Vector3 attenuation = new Vector3(1, 0, 0);

    public Light(Vector3 position, Vector3 color) {
        this.position = position;
        this.color = color;
    }

    public Light(Vector3 position, Vector3 color, Vector3 attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3 getAttenuation() {
        return attenuation;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getColor() {
        return color;
    }
    public float[] getColorArray() {
        return new float[]{color.x,color.y,color.z};
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }
}
