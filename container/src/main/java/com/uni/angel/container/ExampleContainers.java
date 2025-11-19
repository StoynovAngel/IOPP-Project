package com.uni.angel.container;

import java.util.List;

public class ExampleContainers {

    private ExampleContainers() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static List<Container> getContainers() {
        return List.of(
                new Container(2, 4),
                new Container(8, 10),
                new Container(3, 6),
                new Container(4, 8)
        );
    }
}
