//
// Created by Antonio Johnathan on 15/04/2022.
//

#ifndef GDXWATER_PREGL_H
#define GDXWATER_PREGL_H

#include <vector>
#include <iostream>
#include <glm/gtx/string_cast.hpp>



#if defined(_WIN32)
#include <windows.h>
#endif

#if defined(__RENDERER_GLES2__)

#if defined(__APPLE__)
#include <OpenGLES/ES2/gl.h>
#include <OpenGLES/ES2/glext.h>
#else
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#endif

#elif defined(__RENDERER_GLES3__)

#if defined(__APPLE__)
#include <OpenGLES/ES3/gl.h>
#else
#define GL_GLEXT_PROTOTYPES
#include <GLES3/gl3.h>
#endif

#elif defined(__RENDERER_GL2__)

#if _WIN32
#include <GL/gl.h>
#elif defined(__APPLE__)
#define GL_SILENCE_DEPRECATION
#include <OpenGL/gl.h>
#else
#include <GL/gl.h>
#endif

#else

#if defined(_WIN32)
#include <GL/gl.h>
#elif defined(__APPLE__)
#define GL_SILENCE_DEPRECATION
#include <OpenGL/gl3.h>
#else
#define GL_GLEXT_PROTOTYPES
#include <GL/gl.h>
#endif

#endif

#if _WIN32
#pragma comment(lib, "gdiplus.lib")
#pragma comment(lib, "opengl32.lib")
#endif

#ifdef _WIN32
#define WT_STDCALL __stdcall
#else
#define WT_STDCALL
#endif


#endif //GDXWATER_PREGL_H
