package io.github.srjohnathan.gdx.water.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TestShader implements Shader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;
    int u_projTrans;
    int u_worldTrans;

    @Override
    public void init() {

        program = new ShaderProgram("attribute vec3 a_position;\n" +
                "attribute vec3 a_normal;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "\n" +
                "uniform mat4 u_worldTrans;\n" +
                "uniform mat4 u_projViewTrans;\n" +
                "\n" +
                "varying vec2 v_texCoord0;\n" +
                "\n" +
                "void main() {\n" +
                "    v_texCoord0 = a_texCoord0;\n" +
                "    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);\n" +
                "}", "#ifdef GL_ES \n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "\n" +
                "varying vec2 v_texCoord0;\n" +
                "\n" +
                "void main() {\n" +
                "    gl_FragColor = vec4(v_texCoord0, 0.0, 1.0);\n" +
                "}");
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        u_projTrans = program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
    }

    @Override
    public void dispose() {
        program.dispose();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;

        context.setCullFace(GL20.GL_FRONT);
        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        program.setUniformMatrix(u_projTrans, camera.combined);
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }





    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }
}