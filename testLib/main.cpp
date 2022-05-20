//
// Created by pc on 16/04/2022.
//

#include <Lib.h>
#include <GLFW/glfw3.h>
#include "camera.h"

int main () {


    glfwInit();
    // Set all the required options for GLFW
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
    glfwWindowHint(GLFW_SAMPLES, 16);

    GLFWwindow* window = glfwCreateWindow(1024, 720, "Ocean Surface", nullptr, nullptr);

    glfwMakeContextCurrent(window);
    printf("OpenGL Version: %s\n", glGetString(GL_VERSION));

  if(  Waater::InitializeAsOpenGL(OpenGLDeviceType::OpenGL3)){
      cout << "Load GL" << endl;
  }

  Lib lib;
       Wave  wave(64,64, 1000, 1000, 1,1, 30, 1);
    Camera  camera(30.0f, 30.0f, 60.0f, 0, 1, 0, 0, 0, 0);
     camera.MovementSpeed = 30;

     lib.setWave(&wave);
     lib.setPcamera(camera.Position.x, camera.Position.y, camera.Position.z);
     lib.setLampPos(0.0f,4.0f,0.0f);

    glm::mat4 view;
    view = camera.GetViewMatrix();
    glm::mat4 projection = glm::perspective(camera.Zoom, (GLfloat)1024 / (GLfloat)720, 0.1f, 1000.0f);
    lib.setProjection(glm::value_ptr(projection));
    lib.setView(glm::value_ptr(view));

    while (!glfwWindowShouldClose(window))
    {

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        lib.draw(1.0/2);


        glfwSwapBuffers(window);
    }

    glfwTerminate();
    return 0;
}