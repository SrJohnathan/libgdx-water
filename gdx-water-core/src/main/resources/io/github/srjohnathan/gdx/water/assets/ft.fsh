#ifdef GL_ES

precision mediump float;
#endif


varying vec4 clipSpace;
varying vec2 textureCoords;
varying vec3 toCameraVector;
varying vec3 fromLightVector;
varying float visibility;

// out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;


uniform vec3 lightColour;
uniform vec3 skyColour;

uniform float moveFactor;

const float waveStrength = 0.05;
const float shineDamper = 10.0;
const float reflectivity = 0.5;
uniform vec3 waterColour;

void main(void) {

    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

    float near = 1.0;
    float far = 300.0;
    float depth = texture2D(depthMap, refractTexCoords).r;
    float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));

    depth = gl_FragCoord.z;
    float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    float waterDepth = floorDistance - waterDistance;

    vec2 distortedTexCoords = texture2D(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
    distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
    vec2 totalDistortion = (texture2D(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / 20.0, 0.0, 1.0);

    refractTexCoords += totalDistortion;
    refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);

    reflectTexCoords += totalDistortion;
    reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
    reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

    vec4 reflectColour = texture2D(reflectionTexture, reflectTexCoords);
    vec4 refractColour = texture2D(refractionTexture, refractTexCoords);
  //  refractColour = mix(refractColour, vec4(waterColour, 1.0), clamp(waterDepth/70.0, 0.0, 1.0));

    vec4 normalMapColour = texture2D(normalMap, distortedTexCoords);
    vec3 normal = vec3(normalMapColour.r * 2.0 - 1.0, normalMapColour.b * 3.0, normalMapColour.g * 2.0 - 1.0);

    normal = normalize(normal);

    vec3 viewVector = normalize(toCameraVector);

    float refractiveFactor = dot(viewVector, normal);
    refractiveFactor = pow(refractiveFactor, 0.5);
    refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);

    vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColour * specular * reflectivity * clamp(waterDepth / 5.0, 0.0, 1.0);

    vec4  out_Color = mix(reflectColour, refractColour, refractiveFactor);
    out_Color = mix(out_Color, vec4(waterColour, 1.0), 0.3) + vec4(specularHighlights, 0.0);
    out_Color = mix(vec4(skyColour,1.0),out_Color, visibility);
  //  out_Color.a = clamp(waterDepth / 5.0, 0.0, 1.0);


    gl_FragColor = out_Color;
}
