#moj_import <unearth:raymarch/sdf_base.glsl>

float sdf_sphere(in vec3 p, in vec3 c, in float r) {
    return sdSphere(p - c, r);
}

float sdf_box(in vec3 p, in vec3 c, in vec3 r) {
    return sdBox(p - c, r);
}