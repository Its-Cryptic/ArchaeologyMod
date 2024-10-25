float sdf_sphere(in vec3 p, in vec3 c, in float r) {
    return length(p - c) - r;
}

float sdf_box(in vec3 p, in vec3 c, in vec3 s) {
    vec3 d = abs(p - c) - s;
    return min(max(d.x, max(d.y, d.z)), 0.0) + length(max(d, 0.0));
}