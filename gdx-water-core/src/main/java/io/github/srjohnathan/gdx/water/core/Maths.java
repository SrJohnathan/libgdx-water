package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class Maths {


    public static Matrix4 createTransformationMatrixA(Vector3 translation, float rx, float ry, float rz, float scale) {
        Matrix4 matrix = new Matrix4();
        matrix.idt();
        matrix.translate(translation);
        matrix.rotate(rx, 1, 0, 0);
        matrix.rotate(ry, 0, 1, 0);
        matrix.rotate(rz, 0, 0, 1);
        matrix.scale(scale, scale, scale);

        return matrix;
    }



    public static Matrix4 createViewMatrix(Camera35 camera) {
        return camera.getViewMatrix();
    }


}
