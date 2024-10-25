#version 150

#moj_import <unearth:raymarch/sdf.glsl>

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

uniform mat4 invViewMat;
uniform vec2 InSize;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

float distance_from_sphere(in vec3 p, in vec3 c, float r)
{
    return length(p - c) - r;
}

float map_the_world(in vec3 p)
{
    float displacement = sin(5.0 * p.x) * sin(5.0 * p.y) * sin(5.0 * p.z) * 0.25;
    float sphere_0 = distance_from_sphere(p, vec3(0.0), 1.0);

    return sphere_0 + displacement;
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

//vec3 ray_march(in vec3 ro, in vec3 rd)
//{
//    float total_distance_traveled = 0.0;
//    const int NUMBER_OF_STEPS = 64;
//    const float MINIMUM_HIT_DISTANCE = 0.001;
//    const float MAXIMUM_TRACE_DISTANCE = 1000.0;
//
//    for (int i = 0; i < NUMBER_OF_STEPS; ++i)
//    {
//        vec3 current_position = ro + total_distance_traveled * rd;
//
//        float distance_to_closest = map_the_world(current_position);
//
//        if (distance_to_closest < MINIMUM_HIT_DISTANCE)
//        {
//            vec3 normal = calculate_normal(current_position);
//            vec3 light_position = vec3(2.0, -5.0, 3.0);
//            vec3 direction_to_light = normalize(current_position - light_position);
//
//            float diffuse_intensity = max(0.0, dot(normal, direction_to_light));
//
//            return vec3(1.0, 0.0, 0.0) * diffuse_intensity;
//        }
//
//        if (total_distance_traveled > MAXIMUM_TRACE_DISTANCE)
//        {
//            break;
//        }
//        total_distance_traveled += distance_to_closest;
//    }
//    return vec3(0.0);
//}

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
    vec3 material_properties; // For example: (reflectivity, shininess, refractivity)
};

RayMarchResult ray_march(in vec3 ro, in vec3 rd)
{
    float total_distance_traveled = 0.0;
    const int NUMBER_OF_STEPS = 128;
    const float MINIMUM_HIT_DISTANCE = 0.001;
    const float MAXIMUM_TRACE_DISTANCE = 1000.0;

    RayMarchResult result;
    result.hit = false;

    for (int i = 0; i < NUMBER_OF_STEPS; ++i)
    {
        vec3 current_position = ro + total_distance_traveled * rd;

        float distance_to_closest = map_the_world(current_position);

        if (distance_to_closest < MINIMUM_HIT_DISTANCE)
        {
            result.position = current_position;
            result.normal = calculate_normal(current_position);
            result.distance = total_distance_traveled;
            result.hit = true;

            // Calculate material properties
            result.material_properties = get_material_properties(current_position);

            // Calculate color (could be a texture lookup or a function)
            result.color = get_color(current_position);

            // Lighting calculations
            vec3 light_position = vec3(2.0, -5.0, 3.0);
            vec3 light_direction = normalize(light_position - current_position);

            result.diffuse_intensity = max(0.0, dot(result.normal, light_direction));

            // Specular intensity (Phong reflection model)
            vec3 view_direction = normalize(-rd);
            vec3 reflect_direction = reflect(-light_direction, result.normal);
            float shininess = result.material_properties.y; // Assuming shininess is stored here
            result.specular_intensity = pow(max(dot(view_direction, reflect_direction), 0.0), shininess);

            return result;
        }

        if (total_distance_traveled > MAXIMUM_TRACE_DISTANCE)
        {
            break;
        }

        total_distance_traveled += distance_to_closest;
    }

    // If no hit, you can set default values
    result.position = ro + total_distance_traveled * rd;
    result.normal = vec3(0.0);
    result.distance = total_distance_traveled;
    result.hit = false;
    result.color = vec3(0.0);
    result.diffuse_intensity = 0.0;
    result.specular_intensity = 0.0;
    result.material_properties = vec3(0.0);

    return result;
}

void main() {
    vec2 uv = texCoord * 2.0 - 1.0;
    uv *= vec2(InSize.x / InSize.y, 1.0);
    fragColor = texture(DiffuseSampler, texCoord);

    vec3 rayOrigin = vec3(0.0, 0.0, -5.0);
    vec3 rayDirection = vec3(uv, 1.0);
    //vec3 ray = ray_march(rayOrigin, rayDirection);
    RayMarchResult ray = ray_march(rayOrigin, rayDirection);
    if (ray.hit) {
        float diffuseIntensity = ray.diffuse_intensity;
        float specularIntensity = ray.specular_intensity;
        vec3 color = ray.color;
        vec3 finalColor = color * (1-diffuseIntensity);
        fragColor = vec4(finalColor, 1.0);
    }

}