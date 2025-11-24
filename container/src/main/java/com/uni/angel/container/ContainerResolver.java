package com.uni.angel.container;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ContainerResolver {

	private static final int SHIP_CAPACITY = 10;
	private static final List<Container> containers = ExampleContainers.getContainers();

	private ContainerResolver() {
		throw new UnsupportedOperationException();
	}

	public static void solve() {
		int containerSize = containers.size();

		int[][] optimalEarningPerCapacity = new int[containerSize + 2][SHIP_CAPACITY + 1];
		int[][] optimalContainerCount = new int[containerSize + 2][SHIP_CAPACITY + 1];

		computeBellmanTables(optimalEarningPerCapacity, optimalContainerCount);

		log.info("All optimal solutions:");
		int optimalValue = optimalEarningPerCapacity[1][SHIP_CAPACITY];
		List<int[]> solutions = findAllOptimalSolutions(optimalValue);

		for (int[] solution : solutions) {
			printSolution(solution);
		}

		log.info("Number of solutions = {}", solutions.size());
	}

	private static void computeBellmanTables(int[][] optimalEarningPerCapacity, int[][] optimalContainerCount) {
		int containerSize = containers.size();

		for (int i = 0; i <= SHIP_CAPACITY; i++) {
			optimalEarningPerCapacity[containerSize + 1][i] = 0;
		}

		for (int i = containerSize; i >= 1; i--) {
			int qi = containers.get(i - 1).weight();
			int ci = containers.get(i - 1).value();

			for (int S = 0; S <= SHIP_CAPACITY; S++) {
				int bestValue = 0;
				int bestX = 0;

				int maxCount = S / qi;

				for (int x = 0; x <= maxCount; x++) {
					int newValue = ci * x + optimalEarningPerCapacity[i + 1][S - qi * x];

					if (newValue > bestValue) {
						bestValue = newValue;
						bestX = x;
					}
				}

				optimalEarningPerCapacity[i][S] = bestValue;
				optimalContainerCount[i][S] = bestX;
			}

			log.info("W{}(S): {}", i, Arrays.toString(optimalEarningPerCapacity[i]));
		}
	}

	private static List<int[]> findAllOptimalSolutions(int optimalValue) {
		List<int[]> results = new ArrayList<>();
		int[] selection = new int[containers.size()];

		dfs(containers, 0, ContainerResolver.SHIP_CAPACITY, optimalValue, selection, results);

		return results;
	}

	private static void dfs(List<Container> containers, int index, int remainingW, int remainingV, int[] selection, List<int[]> results) {
		if (remainingW < 0 || remainingV < 0) {
			return;
		}

		if (remainingW == 0 && remainingV == 0) {
			results.add(selection.clone());
			return;
		}

		if (index >= containers.size()) {
			return;
		}

		Container container = containers.get(index);

		for (int count = 0; count <= remainingW / container.weight(); count++) {
			selection[index] = count;
			dfs(containers, index + 1, remainingW - count * container.weight(), remainingV - count * container.value(), selection, results);
		}

		selection[index] = 0;
	}

	private static void printSolution(int[] sol) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < sol.length; i++) {
			sb.append("x").append(i + 1).append("* = ").append(sol[i]).append(" ");
		}

		log.info(sb.toString());
	}
}
