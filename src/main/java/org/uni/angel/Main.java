package org.uni.angel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the max capacity of the ship: ");
        int capacity = scanner.nextInt();

        String messageCapacity = "Max capacity: " + capacity;
        logger.info(messageCapacity);

        System.out.print("Enter the number of container's types: ");
        int types = scanner.nextInt();

        String messageTypes = "Container's types: " + types;
        logger.info(messageTypes);

        List<Container> containers = new ArrayList<>();

        for (int i = 0; i < types; i++) {
            System.out.println("Enter weight:");
            int weight = scanner.nextInt();

            System.out.println("Enter value: ");
            int value = scanner.nextInt();

            logger.info("Weight and value {}, {}: ", weight, value);
            containers.add(new Container(weight, value));
        }

        logger.info("Solving the actual task...");
        ProblemSolver problemSolver = new ProblemSolver();
        problemSolver.solve(containers, capacity);
    }
}