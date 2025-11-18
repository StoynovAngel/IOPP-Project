package com.uni.angel.container;

import java.util.List;

public class ExampleContainers {

    public static List<Container> getContainers() {
        return List.of(
                new Container(2, 4),
                new Container(8, 10),
                new Container(4, 6),
                new Container(6, 8)
        );
    }
}
