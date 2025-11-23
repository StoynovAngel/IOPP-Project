package com.uni.angel.container;

import java.util.List;

public class ExecuteContainer {

	private static final int SHIP_CAPACITY = 10;

	public void run() {
		List<Container> containers = ExampleContainers.getContainers();
		ContainerResolver.solve(containers, SHIP_CAPACITY);
	}
}
