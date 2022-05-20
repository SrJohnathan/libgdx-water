cmake_minimum_required(VERSION 3.15)

project(wt)


set(BUILD_EXAMPLES OFF)
set(BUILD_SHARED_LIBS OFF)
set(ENABLE_FLOAT ON)
set(ENABLE_SSE2 ON)
set(ENABLE_AVX2 ON)
set(DISABLE_FORTRAN OFF)
set(ENABLE_THREADS ON)
set(ENABLE_LONG_DOUBLE OFF)
set(ENABLE_QUAD_PRECISION OFF)


add_subdirectory(fftw3)
add_subdirectory(glm)
find_package(OpenGL REQUIRED)


set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--start-group")


if (WIN32)
    message(windows)
endif ()
find_package(JNI REQUIRED)
SET(CMAKE_EXE_LINKER_FLAGS "-ljvm")


link_directories(${CMAKE_CURRENT_BINARY_DIR}/fftw3)
#include_directories(${FFTW3_DIR}/api/)

if (USE_INTERNAL_LOADER)
    add_definitions(-D__RENDERER_INTERNAL_LOADER__)
endif ()

if (USE_OPENGLES2)
    add_definitions(-D__RENDERER_GLES2__)
endif ()

if (USE_OPENGLES3)
    add_definitions(-D__RENDERER_GLES3__)
endif ()

if (USE_OPENGL3)
    add_definitions(-D__RENDERER_GL3__)
endif ()


add_library(
        wt SHARED


        dll.cpp
        shader.h
        model.h
        mesh.h
        wave.h
        wave.cpp
        shaders-str.h
        Lib.cpp
        Lib.h

        PreGL.h
        Extension.cpp
        Extension.h
        OpenGLDeviceType.h)


include_directories(${CMAKE_CURRENT_SOURCE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/fftw3/api)

target_include_directories(
        wt
        PUBLIC
        ${JNI_INCLUDE_DIRS}
)


target_link_libraries(wt

        PRIVATE

        glm
        OpenGL::GL
        fftw3f
        fftw3f_threads
        )


add_dependencies(wt  fftw3f)
add_dependencies(wt  fftw3f_threads)

