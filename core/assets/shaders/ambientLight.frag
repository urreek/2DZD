#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 vTexCoord0;
varying vec4 vColor;

//texture samplers
uniform sampler2D u_texture; //diffuse map

//additional parameters for the shader
uniform vec4 ambientColor;

void main() {
    vec4 diffuseColor = texture2D(u_texture, vTexCoord0);
    vec3 ambient = ambientColor.rgb * ambientColor.a;
    vec3 intensity = ambient * diffuseColor.rgb;
    gl_FragColor = vec4(intensity, diffuseColor.a);
}