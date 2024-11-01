package dev.cryptics.unearth.registry.client;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.shader.post.RayMarcher;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import java.util.ArrayList;
import java.util.List;

public class UnearthPostProcessors {
    private static final List<PostProcessor> POST_PROCESSORS = new ArrayList<>();

    public static final RayMarcher RAY_MARCHER = register(new RayMarcher());


    private static <T extends PostProcessor> T register(T postProcessor) {
        POST_PROCESSORS.add(postProcessor);
        return postProcessor;
    }

    public static void init() {
        POST_PROCESSORS.forEach(postProcessor -> postProcessor.setActive(false));
        //POST_PROCESSORS.forEach(PostProcessHandler::addInstance);

        int totalPostProcessors = POST_PROCESSORS.size();
        //Unearth.LOGGER.info("Registered " + totalPostProcessors + " post processors");
    }
}
