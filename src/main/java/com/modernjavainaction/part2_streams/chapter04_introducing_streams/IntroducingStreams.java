package com.modernjavainaction.part2_streams.chapter04_introducing_streams;

import com.modernjavainaction.common.model.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Chapter 4: Introducing Streams
 * 
 * Key concepts:
 * - What is a Stream?
 * - Streams vs Collections
 * - Internal vs External iteration
 * - Stream operations: intermediate and terminal
 * - Stream pipeline
 */
public class IntroducingStreams {

    public static void main(String[] args) {
        List<Dish> menu = Dish.getMenu();

        System.out.println("=== Chapter 4: Introducing Streams ===\n");

        // 1. Before Java 8: External iteration
        System.out.println("1. Before Java 8 - External iteration (verbose!):");
        List<Dish> lowCaloriesDishesOld = new ArrayList<>();
        for (Dish dish : menu) {
            if (dish.getCalories() < 400) {
                lowCaloriesDishesOld.add(dish);
            }
        }
        Collections.sort(lowCaloriesDishesOld, new Comparator<Dish>() {
            @Override
            public int compare(Dish d1, Dish d2) {
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });
        List<String> lowCaloriesDishNamesOld = new ArrayList<>();
        for (Dish dish : lowCaloriesDishesOld) {
            lowCaloriesDishNamesOld.add(dish.getName());
        }
        System.out.println(lowCaloriesDishNamesOld);

        // 2. Java 8: Stream API - Internal iteration
        System.out.println("\n2. Java 8 - Stream API (clean and declarative!):");
        List<String> lowCaloriesDishNames = menu.stream()
                .filter(d -> d.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
        System.out.println(lowCaloriesDishNames);

        // 3. Parallel stream - same code, parallel execution!
        System.out.println("\n3. Parallel Stream:");
        List<String> parallelResult = menu.parallelStream()
                .filter(d -> d.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
        System.out.println(parallelResult);

        // 4. Streams are traversable only once!
        System.out.println("\n4. Streams can only be consumed once:");
        java.util.stream.Stream<Dish> stream = menu.stream();
        stream.forEach(System.out::println);
        // Uncomment to see IllegalStateException:
        // stream.forEach(System.out::println);

        // 5. Intermediate vs Terminal operations
        System.out.println("\n5. Lazy evaluation - intermediate operations:");
        List<String> names = menu.stream()
                .filter(dish -> {
                    System.out.println("  filtering: " + dish.getName());
                    return dish.getCalories() > 300;
                })
                .map(dish -> {
                    System.out.println("  mapping: " + dish.getName());
                    return dish.getName();
                })
                .limit(3) // Short-circuiting!
                .collect(Collectors.toList());
        System.out.println("Result: " + names);

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 4.1: Get names of vegetarian dishes");
        System.out.println("Exercise 4.2: Find the 3 most caloric dishes");
        System.out.println("Exercise 4.3: Explain why streams are lazy (write in comments)");
        System.out.println("Exercise 4.4: Convert a for loop that filters fish dishes to streams");
    }

    // TODO: Exercise 4.1 - Get vegetarian dish names
    public static List<String> getVegetarianDishNames(List<Dish> menu) {
        // Implement using streams
        return null;
    }

    // TODO: Exercise 4.2 - Get 3 highest calorie dishes
    public static List<Dish> getTopThreeHighestCalorieDishes(List<Dish> menu) {
        // Implement using streams
        return null;
    }
}
