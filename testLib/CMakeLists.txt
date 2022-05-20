cmake_minimum_required(VERSION 3.11)
project(testLib)

set(CMAKE_CXX_STANDARD 11)





find_package(OpenGL REQUIRED)
find_package(glfw3 REQUIRED)

include_directories(../cpp)

add_executable(testLib main.cpp camera.h)






target_link_libraries(testLib OpenGL::GL  wt glfw )

add_dependencies(testLib wt)


