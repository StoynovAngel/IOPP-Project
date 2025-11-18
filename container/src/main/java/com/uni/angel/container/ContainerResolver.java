package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ContainerResolver {

    public static int solve(List<Container> containers, int weightCapacity) {

        int[] bestValue = new int[weightCapacity + 1];

        for (int i = 0; i <= weightCapacity; i++) {

            for (Container container : containers) {
                int weightContainer = container.weight();
                int valueContainer = container.value();

                if (weightContainer > i) {
                    log.debug("Container (weightContainer={}, valueContainer={}) does NOT fit into capacity {}", weightContainer, valueContainer, weightCapacity);
                    continue;
                }

                int previous = bestValue[weightCapacity];
                int candidate = valueContainer + bestValue[weightCapacity - weightContainer];

                bestValue[i] = Math.max(bestValue[i], valueContainer + bestValue[i - weightContainer]);

                if (candidate > previous) {
                    String message = "Update: bestValue[{}] changed from {} → {} using container (w={}, v={})";
                    log.info(message, weightCapacity, previous, candidate, weightContainer, valueContainer);
                    bestValue[weightCapacity] = candidate;
                }
            }
        }

        return bestValue[weightCapacity];
    }
}
