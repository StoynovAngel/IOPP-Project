package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ContainerResolver {

    public static int solve(List<Container> containers, int weightCapacity) {
        int[] bestValue = new int[weightCapacity + 1];
        int[] choice = new int[weightCapacity + 1];

        for (int capacity = 0; capacity <= weightCapacity; capacity++) {
            for (int i = 0; i < containers.size(); i++) {
                Container container = containers.get(i);

                int w = container.weight();
                int v = container.value();

                if (w > capacity) {
                    log.debug(
                            "Container (w={}, v={}) does NOT fit into capacity {}",
                            w, v, capacity
                    );
                    continue;
                }

                int previous = bestValue[capacity];
                int candidate = v + bestValue[capacity - w];

                log.debug(
                        "Try container {} (w={}, v={}): candidate = {} + bestValue[{}] = {}",
                        i, w, v, v, (capacity - w), candidate
                );

                if (candidate > previous) {
                    bestValue[capacity] = candidate;
                    choice[capacity] = i;

                    log.info(
                            "UPDATE: bestValue[{}] changed {} → {} using container i={}, (w={}, v={})",
                            capacity, previous, candidate, i, w, v
                    );
                }
            }
        }

        int cap = weightCapacity;
        int[] count = new int[containers.size()];

        while (cap > 0) {
            int chosenIndex = choice[cap];
            count[chosenIndex]++;

            int w = containers.get(chosenIndex).weight();
            cap -= w;
        }

        for (int i = 0; i < containers.size(); i++) {
            String message = "x{}*={}";
            log.info(message, i, count[i]);
        }

        return bestValue[weightCapacity];
    }
}
