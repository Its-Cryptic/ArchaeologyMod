#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

float remap(float value, vec2 inputRange, vec2 outputRange) {
    return outputRange.x + (value - inputRange.x) * (outputRange.y - outputRange.x) / (inputRange.y - inputRange.x);
}

vec2 remap2(vec2 value, vec2 inputRange, vec2 outputRange) {
    return vec2(
    remap(value.r, inputRange, outputRange),
    remap(value.g, inputRange, outputRange)
    );
}

vec3 remap3(vec3 value, vec2 inputRange, vec2 outputRange) {
    return vec3(
    remap(value.r, inputRange, outputRange),
    remap(value.g, inputRange, outputRange),
    remap(value.b, inputRange, outputRange)
    );
}

vec4 remap4(vec4 value, vec2 inputRange, vec2 outputRange) {
    return vec4(
        remap(value.r, inputRange, outputRange),
        remap(value.g, inputRange, outputRange),
        remap(value.b, inputRange, outputRange),
        remap(value.a, inputRange, outputRange)
    );
}

void main() {
    vec4 texture = texture(Sampler0, texCoord0);
    vec4 remapped = remap4(texture, vec2(0.184, 0.259), vec2(0.7, 1.0));

    vec4 color = remapped * vertexColor;
    fragColor = color * ColorModulator;

    if (fragColor.a < 0.5) discard;
}