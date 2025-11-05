package org.uni.angel;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ProblemSolver {

    private static final Logger logger = LogManager.getLogger(ProblemSolver.class);

    public int solve(List<Container> containers, int capacity) {
        if (containers.isEmpty()) {
            logger.warn("Container size is empty. Returning 0.");
            return 0;
        }

        if (capacity <= 0) {
            logger.warn("Capacity is less than 0. Returning 0");
            return 0;
        }

        int types = containers.size();

        int[][] maxValueTable = new int[types + 1][capacity + 1];
        int[][] containerCountTable = new int[types + 1][capacity + 1];

        for (int type = types - 1; type >= 0; type--) {
            int weight = containers.get(type).getWeight();
            int value = containers.get(type).getValue();

            for (int remainingCapacity = 0; remainingCapacity <= capacity; remainingCapacity++) {
                int optimalValue = maxValueTable[type + 1][remainingCapacity];
                int optimalCount = 0;

                int maxPossibleCount = remainingCapacity / weight;

                for (int count = 1; count <= maxPossibleCount; count++) {
                    int currentValue = value * count
                            + maxValueTable[type + 1][remainingCapacity - weight * count];
                    if (currentValue > optimalValue) {
                        optimalValue = currentValue;
                        optimalCount = count;
                    }
                }

                maxValueTable[type][remainingCapacity] = optimalValue;
                containerCountTable[type][remainingCapacity] = optimalCount;
            }
        }

        String message = "Max value: " + maxValueTable[0][capacity];
        logger.debug(message);

        reconstructSolution(containers, capacity, containerCountTable);

        return maxValueTable[0][capacity];
    }

    private void reconstructSolution(List<Container> containers, int capacity, int[][] containerCountTable) {
        String message = "Optimal number of containers: " + capacity;
        logger.debug(message);

        for (int type = 0; type < containers.size(); type++) {
            int count = containerCountTable[type][capacity];

            logger.debug("Type {}: {} quantity.", type + 1, count);
            capacity -= containers.get(type).getWeight() * count;
        }
    }
}
