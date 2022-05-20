//
// Created by pc on 15/04/2022.
//

#include "Lib.h"
#include "Extension.h"
#include "shaders-str.h"


using namespace WT;

void logger(const char *str) {
    cout << str << endl;
}

void loggerI(float i) {
    cout << i << endl;
}

void Lib::setLampPos(float x, float y, float z) {

    this->lampPos = new vec3(x, y, z);

}

void Lib::setSundir(float x, float y, float z) {
    vec3 v = glm::normalize(vec3(x, y, z));
    this->sundir = &v;
}

void Lib::setSuncol(float x, float y, float z) {
    this->suncol = new vec3(x, y, z);
}

void Lib::setSeacolor(float x, float y, float z) {
    this->seacolor = new vec3(x, y, z);
}

void Lib::buildTessendorfWaveMesh(Wave *wave_model) {

    int nVertex = N * M;

    wave_model->buildField(timer);
    vec3 *heightField = wave_model->heightField;
    vec3 *normalField = wave_model->normalField;

    int p = 0;

    for (int i = 0; i < N; i++)
        for (int j = 0; j < M; j++) {
            int index = j * N + i;

            if (heightField[index].y > heightMax) heightMax = heightField[index].y;
            else if (heightField[index].y < heightMin) heightMin = heightField[index].y;
        }


    WT::glBindVertexArray(surfaceVAO);
    WT::glBindBuffer(GL_ARRAY_BUFFER, surfaceVBO);

    int fieldArraySize = sizeof(glm::vec3) * nVertex;
    WT::glBufferData(GL_ARRAY_BUFFER, fieldArraySize * 2, NULL, GL_STATIC_DRAW);

    // Copy height to buffer
    WT::glBufferSubData(GL_ARRAY_BUFFER, 0, fieldArraySize, heightField);
    WT::glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(GLfloat), (GLvoid *) 0);
    WT::glEnableVertexAttribArray(0);

    // Copy normal to buffer
    WT::glBufferSubData(GL_ARRAY_BUFFER, fieldArraySize, fieldArraySize, normalField);
    WT::glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(GLfloat), (GLvoid *) fieldArraySize);
    WT::glEnableVertexAttribArray(1);
}

void Lib::initBufferObjects() {

    indexSize = (N - 1) * (M - 1) * 6;
    GLuint *indices = new GLuint[indexSize];

    int p = 0;

    for (int j = 0; j < N - 1; j++)
        for (int i = 0; i < M - 1; i++) {
            indices[p++] = i + j * N;
            indices[p++] = (i + 1) + j * N;
            indices[p++] = i + (j + 1) * N;

            indices[p++] = (i + 1) + j * N;
            indices[p++] = (i + 1) + (j + 1) * N;
            indices[p++] = i + (j + 1) * N;
        }

    // Element buffer object
    WT::glGenVertexArrays(1, &surfaceVAO);
    WT::glBindVertexArray(surfaceVAO);
    WT::glGenBuffers(1, &surfaceVBO);
    WT::glGenBuffers(1, &EBO);
    WT::glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    WT::glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize * sizeof(GLuint), indices, GL_STATIC_DRAW);

    delete[] indices;
}

void Lib::deleteBufferObjects() {
    WT::glDeleteVertexArrays(1, &surfaceVAO);
    WT::glDeleteBuffers(1, &surfaceVBO);
    WT::glDeleteBuffers(1, &EBO);
}

Lib::Lib(const char *vl,const char *fl,const char *vw,const char *fw) {

    glEnable(GL_DEPTH_TEST);
    lightingShader = new Shader(string (vw), string (fw), string(""));
    //Shader lightingShader("lighting.vs", "lighting.glsl");
    lampShader = new Shader(string (vl), string (fl), string(""));

    GLfloat vertices[] = {
            // Positions          // Normals           // Texture Coords
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    // Light



    WT::glGenVertexArrays(1, &lightVAO);
    WT::glGenBuffers(1, &VBO);
    WT::glBindVertexArray(lightVAO);
    WT::glBindBuffer(GL_ARRAY_BUFFER, VBO);
    WT::glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
    WT::glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat),
                              (GLvoid *) 0); // Note that we skip over the other data in our buffer object (we don't need the normals/textures, only positions).
    WT::glEnableVertexAttribArray(0);
    WT::glBindVertexArray(0);
    initBufferObjects();


}

Lib::Lib()
{


  glEnable(GL_DEPTH_TEST);
  lightingShader = new Shader(vert_ocean(), frag_ocean(), string(""));
  //Shader lightingShader("lighting.vs", "lighting.glsl");
  lampShader = new Shader(vert_lamp(), frag_lamp(), string(""));

  GLfloat vertices[] = {
          // Positions          // Normals           // Texture Coords
          -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
          0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
          0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
          0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
          -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
          -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

          -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
          0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
          0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
          0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
          -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
          -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

          -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
          -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
          -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
          -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
          -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
          -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

          0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
          0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
          0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
          0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
          0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
          0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

          -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
          0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
          0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
          0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
          -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
          -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

          -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
          0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
          0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
          0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
          -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
          -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
  };

  // Light



  WT::glGenVertexArrays(1, &lightVAO);
  WT::glGenBuffers(1, &VBO);
  WT::glBindVertexArray(lightVAO);
  WT::glBindBuffer(GL_ARRAY_BUFFER, VBO);
  WT::glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
  WT::glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat),
                            (GLvoid *) 0); // Note that we skip over the other data in our buffer object (we don't need the normals/textures, only positions).
  WT::glEnableVertexAttribArray(0);
  WT::glBindVertexArray(0);
  initBufferObjects();


}



Lib::~Lib() {

}

void Lib::setWave(Wave *wave) {
    this->wave = wave;
}

void Lib::setScale(float scale) {
    this->scale = scale;
}

void Lib::setPosition(float x, float y, float z)
{
    this->x = x;
    this->y = y;
    this->z = z;
}

void Lib::setMeshResolution(int meshResolution) {
    MESH_RESOLUTION = meshResolution;
}

void Lib::draw(float delta) {

    if (lampPos == nullptr) {
        lampPos = new vec3(0.0f, 50, 0.0);
    }
    if (sundir == nullptr) {
        vec3 v = glm::normalize(vec3(0, 1, -2));
        sundir = &v;
    }
    if (suncol == nullptr) {
        suncol = new vec3(1.0f, 1.0f, 1.0f);
    }
    if (seacolor == nullptr) {
        seacolor = new vec3(0.0f, 0.0, 0.5);
    }


    this->timer += delta;


    buildTessendorfWaveMesh(wave);


    //  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    lightingShader->Use();


    GLint lightPosLoc = WT::glGetUniformLocation(lightingShader->Program, "light.position");
    GLint viewPosLoc = WT::glGetUniformLocation(lightingShader->Program, "viewPos");


    WT::glUniform3f(lightPosLoc, this->lampPos->x, this->lampPos->y, this->lampPos->z);
    WT::glUniform3f(viewPosLoc, this->pcamera->x, this->pcamera->y, this->pcamera->z);


    WT::glUniform1f(WT::glGetUniformLocation(lightingShader->Program, "heightMin"), heightMin * scale);
    WT::glUniform1f(WT::glGetUniformLocation(lightingShader->Program, "heightMax"), heightMax * scale);




    // Set lights properties
    WT::glUniform3f(WT::glGetUniformLocation(lightingShader->Program, "light.ambient"), 1.0f, 1.0f, 1.0f);
    WT::glUniform3f(WT::glGetUniformLocation(lightingShader->Program, "light.diffuse"), 1.0f, 1.0f, 1.0f);
    WT::glUniform3f(WT::glGetUniformLocation(lightingShader->Program, "light.specular"), 1.0f, 0.9f, 0.7f);
    // Set material properties

    WT::glUniform1f(WT::glGetUniformLocation(lightingShader->Program, "material.shininess"), 32.0f);

    GLint modelLoc = WT::glGetUniformLocation(lightingShader->Program, "model");
    GLint viewLoc = WT::glGetUniformLocation(lightingShader->Program, "view");
    GLint projLoc = WT::glGetUniformLocation(lightingShader->Program, "projection");




    // Pass the matrices to the shader
    WT::glUniformMatrix4fv(viewLoc, 1, GL_FALSE, view.data());
    WT::glUniformMatrix4fv(projLoc, 1, GL_FALSE, projection.data());
    //  cout << glm::to_string(projection) << endl;



    // ===== Draw Model =====
    WT::glBindVertexArray(surfaceVAO);
    glm::mat4 model;
    model = glm::mat4(1.0f);
    model = glm::scale(model, glm::vec3(scale));    // Scale the surface
    model = glm::translate(model,glm::vec3(this->x,this->y,this->z));

    WT::glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));

    glDrawElements(GL_TRIANGLES, indexSize, GL_UNSIGNED_INT, 0);
    WT::glBindVertexArray(0);


    // ===== Draw Lamp =====

    // Also draw the lamp object, again binding the appropriate shader
    lampShader->Use();
    // Get location objects for the matrices on the lamp shader (these could be different on a different shader)
    modelLoc = WT::glGetUniformLocation(lampShader->Program, "model");
    viewLoc = WT::glGetUniformLocation(lampShader->Program, "view");
    projLoc = WT::glGetUniformLocation(lampShader->Program, "projection");
    // Set matrices
    WT::glUniformMatrix4fv(viewLoc, 1, GL_FALSE, view.data());
    WT::glUniformMatrix4fv(projLoc, 1, GL_FALSE, projection.data());
    model = glm::mat4(1.0f);
    model = glm::translate(model, vec3(lampPos->x, lampPos->y, lampPos->z));
    model = glm::scale(model, glm::vec3(1.0f)); // Make it a smaller cube
    WT::glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));
    // Draw the light object (using light's vertex attributes)
    WT::glBindVertexArray(lightVAO);
    WT::glDrawArrays(GL_TRIANGLES, 0, 36);
    WT::glBindVertexArray(0);

}

void Lib::setView(const vector<float> &view) {
    this->view = view;
}

void Lib::setPcamera(float x, float y, float z) {
    this->pcamera = new vec3(x, y, z);
}

void Lib::setProjection(const vector<float> &projection) {
    this->projection = projection;
}

void Lib::setProjection(const float *projection) {
    const std::vector<float> vf{projection, projection + 16};
    this->projection = vf;
}

void Lib::setView(const float *view) {
    std::vector<float> vf{view, view + 16};
    Lib::view = vf;
}

void Lib::setWidth(int width, int height) {
    WIDTH = width;
    HEIGHT = height;
}

void Lib::setViewport(int x, int y, int width, int height) {
    glViewport(x,y,width,height);

}


bool Waater::InitializeAsOpenGL(OpenGLDeviceType deviceType) {
    return WT::Initialize(deviceType, true);

}
