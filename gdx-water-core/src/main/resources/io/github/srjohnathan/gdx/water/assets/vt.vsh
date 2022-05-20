#ifdef GL_ES

precision mediump float;
// #extension GL_EXT_clip_cull_distance : enable
#endif


attribute vec2 position;

varying vec4 clipSpace;
varying vec2 textureCoords;
varying vec3 toCameraVector;
varying vec3 fromLightVector;
varying float visibility;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;

const float tiling = 4.0;
const float density = 0.003;
const float gradient = 5.0;

void main(void) {

    vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);

    vec4 positionRelativeToCam = projectionMatrix * viewMatrix * worldPosition;

    clipSpace =  positionRelativeToCam;

    gl_Position = clipSpace;

    textureCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
    toCameraVector = cameraPosition - worldPosition.xyz;
    fromLightVector = worldPosition.xyz - lightPosition;

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance*density),gradient));
    visibility = clamp(visibility,0.0,1.0);
}