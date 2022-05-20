cmake_minimum_required(VERSION 3.15)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

project(GdxWater LANGUAGES C CXX )




# Flags
if (WIN32)
  if(USE_MSVC_RUNTIME_LIBRARY_DLL)
    cmake_policy(SET CMP0091 NEW)
    set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>DLL")
  else()
    cmake_policy(SET CMP0091 NEW)
    set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
  endif()
  message(${CMAKE_MSVC_RUNTIME_LIBRARY})
endif()

if(UNIX AND NOT APPLE)
  set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--start-group ")
endif()

if( ANDROID )
# CMAKE_ANDROID_ARCH_ABI

  set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--start-group ")
    set(CMAKE_SYSTEM_NAME Android)
    set(CMAKE_SYSTEM_VERSION 16) # API level
    set(CMAKE_ANDROID_NDK /path/to/android-ndk)
    set(CMAKE_TOOLCHAIN_FILE=android-sdk/ndk/ndk-version/build/cmake/android.toolchain.cmake)
    set(CMAKE_ANDROID_STL_TYPE gnustl_static)
endif()

add_subdirectory(cpp)
add_subdirectory(testLib)



