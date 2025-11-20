package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ContainerResolver {

    private ContainerResolver() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static void solve(List<Container> containers, int shipCapacity) {
        int containerSize = containers.size();
		int[] bestValue = getBestValue(containers, shipCapacity);

		int optimalValue = bestValue[shipCapacity];
        log.info("Maximum value = {}", bestValue);

        List<int[]> solutions = new ArrayList<>();
        dfsCombinations(
                containers, shipCapacity, optimalValue,
                0,
                new int[containerSize],
                solutions
        );

        for (int[] solution : solutions) {
			logResult(solution);
		}
    }

	private static int[] getBestValue(List<Container> containers, int shipCapacity) {
		int[] bestValue = new int[shipCapacity + 1];

		for (int i = 0; i <= shipCapacity; i++) {
			for (Container container : containers) {
				int weight = container.weight();
				int value = container.value();

				if (weight <= i) {
					bestValue[i] = Math.max(bestValue[i], value + bestValue[i - weight]);
				}
			}
		}

		return bestValue;
	}

	private static void dfsCombinations(
			List<Container> containers,
			int remainingWeight,
			int remainingValue,
			int startIndex,
			int[] count,
			List<int[]> solutions
	) {
		if (remainingWeight == 0 && remainingValue == 0) {
			solutions.add(count.clone());
			return;
		}

		if (remainingWeight < 0 || remainingValue < 0) {
			return;
		}

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
					solutions
			);
			count[i]--;
		}
	}

	private static void logResult(int[] solution) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < solution.length; i++) {
			stringBuilder.append("x").append(i + 1).append(" = ").append(solution[i]).append(" ");
		}

		log.info(stringBuilder.toString());
	}
}
