//
// Created by pc on 15/04/2022.
//

#ifndef GDXWATER_LIB_H
#define GDXWATER_LIB_H

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <vector>
#include "wave.h"
#include "OpenGLDeviceType.h"
#include "shader.h"

#ifndef SWIG
using namespace  glm;
 #endif
using namespace std;



class  Waater {

private:
public:
    static bool InitializeAsOpenGL(OpenGLDeviceType deviceType);
};


class Lib {

private:
    int MESH_RESOLUTION = 64;
public:
    void setMeshResolution(int meshResolution);

private:
    int N = MESH_RESOLUTION;
    int M = MESH_RESOLUTION;
    float L_x = 100;
    float L_z = 100;
    float A = 4e-7f;
// Wind speed
    float V = 30;
// Wind direction


    vec2 *omega;
    GLuint WIDTH = 1280, HEIGHT = 720;

// Camera

    GLfloat lastX  =  WIDTH  / 2.0;
    GLfloat lastY  =  HEIGHT / 2.0;
    bool    keys[1024];

// Light attributes
    vec3 *lampPos;
    vec3 *sundir;
    vec3 *suncol;
    vec3 *seacolor;
    vec3 *pcamera;
public:
    void setPcamera(float x , float y , float z );

    void setWidth(int width,int height);

private:


    // Deltatime
    GLfloat deltaTime = 0.0f;	// Time between current frame and last frame
    GLfloat lastFrame = 0.0f;  	// Time of last frame

    vec3* vertexArray;
    GLuint surfaceVAO = 0;
    GLuint surfaceVBO, EBO;
    int indexSize;
    float scale = 0.1;
public:
    void setScale(float scale);
    void setPosition(float x ,float y , float z);

public:
    void setWave(Wave *wave);

private:
    float timer = 0;
    Wave *wave;
    GLuint VBO;
    GLuint lightVAO;
    int nWaves = 60;
    vec4* waveParams;

    float x,y,z;

    float height;
    vector<float> view;
    vector<float> projection;
public:
    void setProjection(const vector<float> &projection);
#ifndef SWIG
    void setProjection(const float *projection);
    void setView(const float *view);

#endif

public:
    void setView(const vector<float> &view);
    void setViewport(int x , int y , int width,int height);



private:
    float heightMax = 0;
    float heightMin = 0;

    void buildTessendorfWaveMesh(Wave* wave_model);
    void initBufferObjects();
    void deleteBufferObjects();

    Shader *lightingShader;
    Shader *lampShader;

public:

    Lib(const char *vl,const char *fl,const char *vw,const char *fw);
    Lib();


    void setLampPos(float x, float y, float z);

    void setSundir(float x, float y, float z);

    void setSuncol(float x, float y, float z);

    void setSeacolor(float x, float y, float z);

    virtual ~Lib();

    void draw(float delta);
};


#endif //GDXWATER_LIB_H
