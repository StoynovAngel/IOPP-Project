package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ContainerResolver {

    private ContainerResolver() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static int solve(List<Container> containers, int weightCapacity) {
        int[] bestValue = new int[weightCapacity + 1];
        int[] choice = new int[weightCapacity + 1];

        for (int capacity = 0; capacity <= weightCapacity; capacity++) {
            for (int i = 0; i < containers.size(); i++) {
                Container container = containers.get(i);

                int weight = container.weight();
                int value = container.value();

                if (weight > capacity) {
                    log.debug("Container (weight={}, value={}) does NOT fit into capacity {}", weight, value, capacity);
                    continue;
                }

                int previous = bestValue[capacity];
                int candidate = value + bestValue[capacity - weight];

                log.debug(
                        "Try container {} (weight={}, value={}): candidate = {} + bestValue[{}] = {}",
                        i, weight, value, value, (capacity - weight), candidate
                );

                if (candidate > previous) {
                    bestValue[capacity] = candidate;
                    choice[capacity] = i;

                    log.info("UPDATE: bestValue[{}] changed {} → {} using container i={}, (weight={}, value={})",
                            capacity, previous, candidate, i, weight, value);
                }
            }
        }

        int[] count = new int[containers.size()];
        int remaining = weightCapacity;

        while (remaining > 0) {
            int idx = choice[remaining];
            count[idx]++;
            remaining -= containers.get(idx).weight();
        }

        for (int i = 0; i < containers.size(); i++) {
            log.info("x{} = {}", i + 1, count[i]);
        }

        return bestValue[weightCapacity];
    }
}
