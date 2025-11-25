package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ContainerResolver {

	private static final int SHIP_CAPACITY = 10;
	private static final List<Container> containers = ExampleContainers.getContainers();

	private ContainerResolver() {
		throw new UnsupportedOperationException();
	}

	public static void solve() {
		int[][] optimalEarningPerCapacity = computeBellmanTables();

		log.info("All optimal solutions:");
		int optimalValue = optimalEarningPerCapacity[1][SHIP_CAPACITY];
		List<Map<Container, Integer>> solutions = findAllOptimalSolutions(optimalValue);

		for (Map<Container, Integer> solution : solutions) {
			printSolution(solution);
		}

		log.info("Number of solutions = {}", solutions.size());
	}

	private static int[][] computeBellmanTables() {
		int containerSize = containers.size();
		int[][] optimalEarningPerCapacity = new int[containerSize + 2][SHIP_CAPACITY + 1];

		for (int i = 0; i <= SHIP_CAPACITY; i++) {
			optimalEarningPerCapacity[containerSize + 1][i] = 0;
		}

		for (int i = containerSize; i >= 1; i--) {
			int qi = containers.get(i - 1).weight();
			int ci = containers.get(i - 1).value();

			for (int s = 0; s <= SHIP_CAPACITY; s++) {
				int bestValue = 0;
				int maxCount = s / qi;

				for (int x = 0; x <= maxCount; x++) {
					int newValue = ci * x + optimalEarningPerCapacity[i + 1][s - qi * x];

					if (newValue > bestValue) {
						bestValue = newValue;
					}
				}

				optimalEarningPerCapacity[i][s] = bestValue;
			}

			log.info("W{}(S): {}", i, Arrays.toString(optimalEarningPerCapacity[i]));
		}

		return optimalEarningPerCapacity;
	}

	private static List<Map<Container, Integer>> findAllOptimalSolutions(int optimalValue) {
		List<Map<Container, Integer>> results = new ArrayList<>();
		Map<Container, Integer> selection = new LinkedHashMap<>();

		containers.forEach(container -> selection.put(container, 0));
		dfs(0, SHIP_CAPACITY, optimalValue, selection, results);

		return results;
	}

	private static void dfs(int index, int remainingWeight, int remainingValue, Map<Container, Integer> selection, List<Map<Container, Integer>> results) {
		if (remainingWeight < 0 || remainingValue < 0) {
			log.debug("It cannot be less than 0");
			return;
		}

		if (remainingWeight == 0 && remainingValue == 0) {
			results.add(new LinkedHashMap<>(selection));
			log.debug("Add new solution: \n{}", selection);
			return;
		}

		if (index >= containers.size()) {
			return;
		}

		Container container = containers.get(index);
		int maxCount = remainingWeight / container.weight();

		for (int count = 0; count <= maxCount; count++) {
			selection.put(container, count);

			int updatedValue = remainingValue - count * container.value();
			int updatedWeight = remainingWeight - count * container.weight();

			log.debug("UpdatedValue: {}, UpdatedWeight: {}", updatedValue, updatedWeight);

			dfs(index + 1, updatedWeight, updatedValue, selection, results);
		}

		selection.put(container, 0);
	}

	private static void printSolution(Map<Container, Integer> solution) {
		StringBuilder sb = new StringBuilder();
		int index = 1;

		for (Map.Entry<Container, Integer> entry : solution.entrySet()) {
			sb.append("x").append(index).append("* = ").append(entry.getValue()).append(" ");
			index++;
		}

		log.info(sb.toString());
	}
}
