package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ContainerResolver {

    private ContainerResolver() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static int solve(List<Container> containers, int capacity) {
        int n = containers.size();
        int[] bestValue = new int[capacity + 1];

        for (int c = 0; c <= capacity; c++) {
            for (Container container : containers) {
                int w = container.weight();
                int v = container.value();

                if (w <= c) {
                    bestValue[c] = Math.max(bestValue[c], v + bestValue[c - w]);
                }
            }
        }

        int optimalValue = bestValue[capacity];
        log.info("Optimal value = {}", optimalValue);

        List<int[]> solutions = new ArrayList<>();
        dfsCombinations(
                containers, capacity, optimalValue,
                0,
                new int[n],
                solutions
        );

        for (int[] sol : solutions) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < sol.length; i++) {
                sb.append("x").append(i + 1).append(" = ").append(sol[i]).append(" ");
            }

            log.info(sb.toString());
        }

        return optimalValue;
    }

    private static void dfsCombinations(
            List<Container> containers,
            int remainingWeight,
            int remainingValue,
            int startIndex,
            int[] count,
            List<int[]> out
    ) {
        if (remainingWeight == 0 && remainingValue == 0) {
            out.add(count.clone());
            return;
        }

        if (remainingWeight < 0 || remainingValue < 0)
            return;

        for (int i = startIndex; i < containers.size(); i++) {
            int w = containers.get(i).weight();
            int v = containers.get(i).value();

            count[i]++;
            dfsCombinations(
                    containers,
                    remainingWeight - w,
                    remainingValue - v,
                    i,
                    count,
                    out
            );
            count[i]--;
        }
    }
}
