package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ContainerResolver {

	private ContainerResolver() {
		throw new UnsupportedOperationException("Cannot instantiate");
	}

	public static void solve(List<Container> containers, int shipCapacity) {
		int[] maxAchievableValue = computeMaxValue(containers, shipCapacity);

		int optimalValue = maxAchievableValue[shipCapacity];
		log.info("Maximum value: {}", optimalValue);

		List<Map<Container, Integer>> optimalSolution = new ArrayList<>();

		Map<Container, Integer> currentSelection = new LinkedHashMap<>();

		for (Container container : containers) {
			currentSelection.put(container, 0);
		}

		findAllSolutions(containers, shipCapacity, optimalValue, 0, currentSelection, optimalSolution);

		log.info("All optimal solutions found: {}", optimalSolution.size());
		for (Map<Container, Integer> solution : optimalSolution) {
			logSolution(containers, solution);
		}
	}

	private static int[] computeMaxValue(List<Container> containers, int shipCapacity) {
		int[] bestValueAtWeight = new int[shipCapacity + 1];

		for (int i = 0; i <= shipCapacity; i++) {
			for (Container container : containers) {
				int weight = container.weight();
				int value = container.value();

				if (weight <= i) {
					bestValueAtWeight[i] = Math.max(bestValueAtWeight[i], value + bestValueAtWeight[i - weight]);
				}
			}
		}

		return bestValueAtWeight;
	}

	private static void findAllSolutions(
			List<Container> containers,
			int remainingWeight,
			int remainingValue,
			int containerIndex,
			Map<Container, Integer> currentSelection,
			List<Map<Container, Integer>> allSolutions
	) {
		log.debug("remainingWeight = {}, remainingValue = {}, containerIndex = {}", remainingWeight, remainingValue, containerIndex);

		if (remainingWeight < 0 || remainingValue < 0) {
			log.debug("Impossible: weight or value is less than 0");
			return;
		}

		if (remainingWeight == 0 && remainingValue == 0) {
			allSolutions.add(new HashMap<>(currentSelection));
			return;
		}

		for (int i = containerIndex; i < containers.size(); i++) {
			Container container = containers.get(i);
			currentSelection.put(container, currentSelection.get(container) + 1);

			int weight = container.weight();
			int value = container.value();

			findAllSolutions(
					containers,
					remainingWeight - weight,
					remainingValue - value,
					i,
					currentSelection,
					allSolutions
			);

			currentSelection.put(container, currentSelection.get(container) - 1);
		}
	}

	private static void logSolution(List<Container> containers, Map<Container, Integer> solution) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < containers.size(); i++) {
			Container c = containers.get(i);
			stringBuilder.append("x").append(i + 1).append(" = ").append(solution.get(c)).append(" ");
		}

		log.info(stringBuilder.toString());
	}
}
