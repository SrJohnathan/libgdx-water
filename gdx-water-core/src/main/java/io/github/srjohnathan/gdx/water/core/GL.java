package io.github.srjohnathan.gdx.water.core;

public interface GL {
  int   glGenVertexArrays();
   void   glBindVertexArray(int n);
   int getImageId(String path);
   void glDrawBuffer();
   void glUniformMatrix4f(int location, float[] matrix);
   void glDrawArrays(int n , int c, int i);

   int createTextureAttachment(int width, int height);
 int createDepthTextureAttachment(int width, int height);
 int createDepthBufferAttachment(int width, int height);
 int glGetProgrami(int program ,int glInt);
}
