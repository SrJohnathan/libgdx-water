package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.math.Vector3;


public interface Camera {
	public void move();
    public void invertPitch();
    public Vector3 getPosition();
    public void printPosition();
}
