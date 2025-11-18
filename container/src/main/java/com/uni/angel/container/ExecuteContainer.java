package com.uni.angel.container;

import java.util.List;

public class ExecuteContainer {

    public void run() {
        List<Container> containers = ExampleContainers.getContainers();
        int shipCapacity = 10;

        int result = ContainerResolver.solve(containers, shipCapacity);

        System.out.println("Maximum value = " + result);
    }
}
