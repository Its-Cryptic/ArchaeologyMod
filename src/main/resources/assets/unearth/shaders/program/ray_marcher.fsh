#version 150

#moj_import <unearth:raymarch/sdf.glsl>

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

uniform mat4 invViewMat;
uniform vec2 InSize;
uniform float u_time;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

float smoothUnionSDF(float distA, float distB, float k ) {
    float h = clamp(0.5 + 0.5*(distA-distB)/k, 0., 1.);
    return mix(distA, distB, h) - k*h*(1.-h);
}

float map_the_world(in vec3 p) {
    float displacement = sin(5.0 * p.x) * sin(5.0 * p.y) * sin(5.0 * p.z) * 0.25;
    // Matrix to rotate around the y-axis
    mat3 rotation = mat3(
        vec3(cos(u_time), 0.0, sin(u_time)),
        vec3(0.0, 1.0, 0.0),
        vec3(-sin(u_time), 0.0, cos(u_time))
    );
    float sphere_0 = sdf_sphere(rotation * p, vec3(0.0), 1.0) + displacement;
    float sphere_1 = sdf_box(rotation * p, vec3(0.75), vec3(1.0));


    return smoothUnionSDF(sphere_0, sphere_1, 1.0);
}

vec3 calculate_color(in vec3 p) {
    float displacement = sin(5.0 * p.x) * sin(5.0 * p.y) * sin(5.0 * p.z) * 0.25;
    // Matrix to rotate around the y-axis
    mat3 rotation = mat3(
    vec3(cos(u_time), 0.0, sin(u_time)),
    vec3(0.0, 1.0, 0.0),
    vec3(-sin(u_time), 0.0, cos(u_time))
    );
    float sphere_0 = sdf_sphere(rotation * p, vec3(0.0), 1.0) + displacement;
    float sphere_1 = sdf_box(rotation * p, vec3(0.75), vec3(1.0));

    return smoothstep(0.0, 0.2, sphere_0) * vec3(1.0, 1.0, 0.0) + smoothstep(0.0, 0.2, sphere_1) * vec3(0.6, 0.0, 0.8);
}

vec3 calculate_normal(in vec3 p)
{
    const vec3 small_step = vec3(0.001, 0.0, 0.0);

    float gradient_x = map_the_world(p + small_step.xyy) - map_the_world(p - small_step.xyy);
    float gradient_y = map_the_world(p + small_step.yxy) - map_the_world(p - small_step.yxy);
    float gradient_z = map_the_world(p + small_step.yyx) - map_the_world(p - small_step.yyx);

    vec3 normal = vec3(gradient_x, gradient_y, gradient_z);

    return normalize(normal);
}

vec3 ray_march_simple(in vec3 ro, in vec3 rd)
{
    float total_distance_traveled = 0.0;
    const int NUMBER_OF_STEPS = 64;
    const float MINIMUM_HIT_DISTANCE = 0.0025;
    const float MAXIMUM_TRACE_DISTANCE = 1000.0;

    /**
        Main ray marching loop
    **/
    for (int i = 0; i < NUMBER_OF_STEPS; ++i) {
        vec3 current_position = ro + total_distance_traveled * rd;

        float distance_to_closest = map_the_world(current_position);
        /**
            Case 1: As we approach a surface, the sdf distance will get increasingly smaller,
            if we are close enough to the surface, we can consider it a hit.
        **/
        if (distance_to_closest < MINIMUM_HIT_DISTANCE) {
            vec3 normal = calculate_normal(current_position);
            vec3 light_position = vec3(2.0, -5.0, 3.0);
            vec3 direction_to_light = normalize(current_position - light_position);

            float diffuse_intensity = max(0.0, dot(normal, direction_to_light));

            return vec3(0.5)+normal;
        }

        if (total_distance_traveled > MAXIMUM_TRACE_DISTANCE) {
            break;
        }
        total_distance_traveled += distance_to_closest;
    }
    return texture(DiffuseSampler, texCoord).rgb;
}

struct RayMarchSettings {
    int stepCount;
    float minHitDistance;
    float maxTraceDistance;
};

struct RayMarchData {
    vec4 color;
    vec3 normal;
    float distance;
    float smallestDistance;
    bool hit;
};

vec3 ray_march_simple2(in vec3 cameraPos, float nearPlane, in vec3 rd, in RayMarchSettings settings) {
    vec3 rayOrigin = cameraPos + vec3(0.0, 0.0, -nearPlane);

    float total_distance_traveled = 0.0;
    float smallest_distance = 100000.0;

    /**
        Main ray marching loop
    **/
    for (int i = 0; i < settings.stepCount; ++i) {
        vec3 current_position = rayOrigin + total_distance_traveled * rd;

        float distance_to_closest = map_the_world(current_position);
        if (distance_to_closest < smallest_distance) {
            smallest_distance = distance_to_closest;
        }
        /**
            Case 1: As we approach a surface, the sdf distance will get increasingly smaller,
            if we are close enough to the surface, we can consider it a hit.
        **/
        if (distance_to_closest < settings.minHitDistance) {
            vec3 normal = calculate_normal(current_position);
            vec3 light_position = vec3(2.0, -5.0, 3.0);
            vec3 direction_to_light = normalize(current_position - light_position);

            float diffuse_intensity = max(0.0, dot(normal, direction_to_light));

            return vec3(0.5)+normal;
        }

        if (total_distance_traveled > settings.maxTraceDistance) {
            break;
        }
        total_distance_traveled += distance_to_closest;
    }
    return texture(DiffuseSampler, texCoord).rgb;
}

vec3 get_color(in vec3 p) {
    return vec3(1.0, 0.0, 0.0);
}

vec3 get_material_properties(in vec3 p) {
    return vec3(0.0, 32.0, 0.0);
}

struct RayMarchResult {
    vec3 position;
    vec3 normal;
    float distance;
    bool hit;
    vec3 color;
    float diffuse_intensity;
    float specular_intensity;
    vec3 material_properties;
};

void main() {
    vec2 uv = texCoord * 2.0 - 1.0;
    uv *= vec2(InSize.x / InSize.y, 1.0);
    fragColor = texture(DiffuseSampler, texCoord);

    vec3 rayOrigin = vec3(0.0, 0.0, -5.0);
    vec3 rayDirection = vec3(uv, 1.0);
    RayMarchSettings settings = RayMarchSettings(64, 0.0025, 1000.0);
    //vec3 ray = ray_march_simple(rayOrigin, rayDirection);
    vec3 cameraPos = vec3(0.0, 0.0, 0.0);
    float nearPlane = 5.0;
    vec3 ray = ray_march_simple2(cameraPos, nearPlane, rayDirection, settings);

    fragColor = vec4(ray, 1.0);

}