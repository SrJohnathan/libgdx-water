#ifndef SHADER_H
#define SHADER_H


#include <string>
#include <fstream>
#include <sstream>
#include <iostream>
#include "Extension.h"

#define _USE_MATH_DEFINES

#include <cmath>

using namespace std;


inline std::string readFile(const char *filePath);

inline bool reportShaderErrors(GLuint shader);


std::string readFile(const char *filePath) {
    std::string content;
    std::ifstream fileStream(filePath, std::ios::in);

    if (!fileStream.is_open()) {
        std::cerr << "Could not read file " << filePath << ". File does not exist." << std::endl;
        return "";
    }

    std::string line = "";
    while (!fileStream.eof()) {
        std::getline(fileStream, line);
        cout << line;
        content.append(line + "\n");
    }

    fileStream.close();
    return content;
}


bool reportShaderErrors(GLuint shader) {
    int infologLength = 0;
    WT::glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infologLength);
    if (infologLength > 1) {
        char *infoLog = new char[infologLength];
        WT::glGetShaderInfoLog(shader, infologLength, NULL, infoLog);

        GLint shadertype;
        WT::glGetShaderiv(shader, GL_SHADER_TYPE, &shadertype);
        std::cerr << "Error compiling ";
        switch (shadertype) {
            case GL_VERTEX_SHADER:
                std::cerr << "vertex";
                break;
            case GL_FRAGMENT_SHADER:
                std::cerr << "fragment";
                break;
            default:
                std::cerr << "unknown type";
        }
        std::cerr << " shader:\n";
        std::cerr << infoLog << std::endl;

        delete[] infoLog;
        return true;
    }
    return false;
}


class Shader {
public:
    GLuint Program;

    // Constructor generates the shader on the fly
    Shader(std::string vertexPath, std::string fragmentPath, std::string geometryPath, int n) {

        std::string vertShaderStr = readFile(vertexPath.c_str());
        std::string fragShaderStr = readFile(fragmentPath.c_str());
        const char *vShaderCode = vertShaderStr.c_str();
        const char *fShaderCode = fragShaderStr.c_str();


        // 2. Compile shaders
        GLuint vertex, fragment;
        GLint success;
        GLchar infoLog[512];
        // Vertex Shader
        vertex = WT::glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, 1, &vShaderCode, NULL);
        WT::glCompileShader(vertex);
        reportShaderErrors(vertex);
        // checkCompileErrors(vertex, "VERTEX");
        // Fragment Shader
        fragment = WT::glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, 1, &fShaderCode, NULL);
        WT::glCompileShader(fragment);
        reportShaderErrors(fragment);
        // checkCompileErrors(fragment, "FRAGMENT");
        // If geometry shader is given, compile geometry shader
        GLuint geometry;
        if (geometryPath.length() > 0) {
            const GLchar *gShaderCode = readFile(geometryPath.c_str()).c_str();
            //  geometry = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(geometry, 1, &gShaderCode, NULL);
            WT::glCompileShader(geometry);
            checkCompileErrors(geometry, "GEOMETRY");
        }
        // Shader Program
        this->Program = WT::glCreateProgram();
        WT::glAttachShader(this->Program, vertex);
        WT::glAttachShader(this->Program, fragment);
        if (geometryPath.length() > 0)
            WT::glAttachShader(this->Program, geometry);
        WT::glLinkProgram(this->Program);
        // checkCompileErrors(this->Program, "PROGRAM");
        // Delete the shaders as they're linked into our program now and no longer necessery
        WT::glDeleteShader(vertex);
        WT::glDeleteShader(fragment);
        if (geometryPath.length() > 0)
            WT::glDeleteShader(geometry);
    }

    Shader(std::string vertexString, std::string fragmentString, std::string geometryPath) {


        const char *vShaderCode = vertexString.c_str();
        const char *fShaderCode = fragmentString.c_str();


        // 2. Compile shaders



        GLuint vertex, fragment;
        GLint success;
        GLchar infoLog[1024];
        // Vertex Shader


        vertex = WT::glCreateShader(GL_VERTEX_SHADER);

        WT::glShaderSource(vertex, 1, &vShaderCode, NULL);
        WT::glCompileShader(vertex);
        reportShaderErrors(vertex);
        // checkCompileErrors(vertex, "VERTEX");
        // Fragment Shader
        fragment = WT::glCreateShader(GL_FRAGMENT_SHADER);
        WT::glShaderSource(fragment, 1, &fShaderCode, NULL);
        WT::glCompileShader(fragment);
        reportShaderErrors(fragment);
        // checkCompileErrors(fragment, "FRAGMENT");
        // If geometry shader is given, compile geometry shader
        GLuint geometry;
        if (geometryPath.length() > 0) {

            const GLchar *gShaderCode = readFile(geometryPath.c_str()).c_str();
            //  geometry = glCreateShader(GL_GEOMETRY_SHADER);
            WT::glShaderSource(geometry, 1, &gShaderCode, NULL);
            WT::glCompileShader(geometry);
            checkCompileErrors(geometry, "GEOMETRY");
        }
        // Shader Program
        this->Program = WT::glCreateProgram();
        WT::glAttachShader(this->Program, vertex);
        WT::glAttachShader(this->Program, fragment);
        if (geometryPath.length() > 0)
            WT::glAttachShader(this->Program, geometry);
        WT::glLinkProgram(this->Program);
        // checkCompileErrors(this->Program, "PROGRAM");
        // Delete the shaders as they're linked into our program now and no longer necessery
        WT::glDeleteShader(vertex);
        WT::glDeleteShader(fragment);
        if (geometryPath.length() > 0)
            WT::glDeleteShader(geometry);
    }

    Shader(const GLchar *vertexPath, const GLchar *fragmentPath, const GLchar *geometryPath = nullptr) {
        // 1. Retrieve the vertex/fragment source code from filePath
        std::string vertexCode;
        std::string fragmentCode;
        std::string geometryCode;
        std::ifstream vShaderFile;
        std::ifstream fShaderFile;
        std::ifstream gShaderFile;
        // ensures ifstream objects can throw exceptions:
        vShaderFile.exceptions(std::ifstream::failbit | std::ifstream::badbit);
        fShaderFile.exceptions(std::ifstream::failbit | std::ifstream::badbit);
        gShaderFile.exceptions(std::ifstream::failbit | std::ifstream::badbit);
        try {
            // Open files
            vShaderFile.open(vertexPath);
            fShaderFile.open(fragmentPath);
            std::stringstream vShaderStream, fShaderStream;
            // Read file's buffer contents into streams
            vShaderStream << vShaderFile.rdbuf();
            fShaderStream << fShaderFile.rdbuf();
            // close file handlers
            vShaderFile.close();
            fShaderFile.close();
            // Convert stream into string
            vertexCode = vShaderStream.str();
            fragmentCode = fShaderStream.str();
            // If geometry shader path is present, also load a geometry shader
            if (geometryPath != nullptr) {
                gShaderFile.open(geometryPath);
                std::stringstream gShaderStream;
                gShaderStream << gShaderFile.rdbuf();
                gShaderFile.close();
                geometryCode = gShaderStream.str();
            }
        }
        catch (std::ifstream::failure e) {
            std::cout << "ERROR::SHADER::FILE_NOT_SUCCESFULLY_READ" << std::endl;
        }


        const GLchar *vShaderCode = vertexCode.c_str();
        const GLchar *fShaderCode = fragmentCode.c_str();
        // 2. Compile shaders
        GLuint vertex, fragment;
        GLint success;
        GLchar infoLog[512];
        // Vertex Shader
        vertex = WT::glCreateShader(GL_VERTEX_SHADER);
       WT:: glShaderSource(vertex, 1, &vShaderCode, NULL);
        WT::glCompileShader(vertex);
        checkCompileErrors(vertex, "VERTEX");
        // Fragment Shader
        fragment = WT::glCreateShader(GL_FRAGMENT_SHADER);
        WT::  glShaderSource(fragment, 1, &fShaderCode, NULL);
        WT::glCompileShader(fragment);
        checkCompileErrors(fragment, "FRAGMENT");
        // If geometry shader is given, compile geometry shader
        GLuint geometry;
        if (geometryPath != nullptr) {
            const GLchar *gShaderCode = geometryCode.c_str();
            //geometry = glCreateShader(GL_GEOMETRY_SHADER);
            WT:: glShaderSource(geometry, 1, &gShaderCode, NULL);
            WT::glCompileShader(geometry);
            checkCompileErrors(geometry, "GEOMETRY");
        }
        // Shader Program
        this->Program = WT::glCreateProgram();
        WT::glAttachShader(this->Program, vertex);
        WT::glAttachShader(this->Program, fragment);
        if (geometryPath != nullptr)
            WT::glAttachShader(this->Program, geometry);
        WT::glLinkProgram(this->Program);
        checkCompileErrors(this->Program, "PROGRAM");
        // Delete the shaders as they're linked into our program now and no longer necessery
        WT::glDeleteShader(vertex);
        WT::glDeleteShader(fragment);
        if (geometryPath != nullptr)
            WT::glDeleteShader(geometry);

    }

    // Uses the current shader
    void Use() { WT::glUseProgram(this->Program); }

private:
    void checkCompileErrors(GLuint shader, std::string type) {


        GLint success;
        GLchar infoLog[1024];
        if (type != "PROGRAM") {
            WT::glGetShaderiv(shader, GL_COMPILE_STATUS, &success);
            if (!success) {
                WT::glGetShaderInfoLog(shader, 1024, NULL, infoLog);
                std::cout << "| ERROR::::SHADER-COMPILATION-ERROR of type: " << type << "|\n" << infoLog
                          << "\n| -- --------------------------------------------------- -- |" << std::endl;
            }
        } else {
            WT::glGetProgramiv(shader, GL_LINK_STATUS, &success);
            if (!success) {
                WT::glGetProgramInfoLog(shader, 1024, NULL, infoLog);
                std::cout << "| ERROR::::PROGRAM-LINKING-ERROR of type: " << type << "|\n" << infoLog
                          << "\n| -- --------------------------------------------------- -- |" << std::endl;
            }
        }
    }
};

#endif
