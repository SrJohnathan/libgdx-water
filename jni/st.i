%module GDX
%{
#include "wave.h"
#include "Lib.h"

%}
%include "arrays_java.i"
%include "std_vector.i"
%include "std_array.i"




namespace std {
    %template(VectorFloat) vector<float>;
};



%include "../cpp/wave.h"
%include "../cpp/Lib.h"
%include "../cpp/OpenGLDeviceType.h"
%include "../cpp/glm/glm/glm.hpp"