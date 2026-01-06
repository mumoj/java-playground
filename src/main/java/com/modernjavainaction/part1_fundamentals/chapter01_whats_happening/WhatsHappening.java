package com.modernjavainaction.part1_fundamentals.chapter01_whats_happening;

import com.modernjavainaction.common.model.Apple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static com.modernjavainaction.common.model.Apple.Color.*;

/**
 * Chapter 1: Java 8, 9, 10, and 11: What's Happening?
 * 
 * Key concepts:
 * - Why Java needed to evolve
 * - Stream API introduction
 * - Passing code to methods (behavior parameterization preview)
 * - Default methods in interfaces
 * - Functional programming concepts
 */
public class WhatsHappening {

    public static void main(String[] args) {
        List<Apple> inventory = createInventory();

        System.out.println("=== Chapter 1: What's Happening in Java 8? ===\n");

        // Old way: imperative style with external iteration
        System.out.println("1. Old way - filtering green apples (imperative):");
        List<Apple> greenApplesOldWay = filterGreenApplesOldWay(inventory);
        greenApplesOldWay.forEach(System.out::println);

        System.out.println("\n2. New way - using lambdas and streams:");
        // TODO: Rewrite using Stream API and lambda expressions
        // Hint: inventory.stream().filter(...).collect(...)

        System.out.println("\n3. Sorting apples by weight:");
        // Old way
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return Integer.compare(a1.getWeight(), a2.getWeight());
            }
        });

        // TODO: Rewrite using lambda expression
        // Hint: inventory.sort((a1, a2) -> ...)
        // Even better: inventory.sort(Comparator.comparing(...))

        System.out.println("\n4. Method references - the most concise form:");
        // TODO: Use method reference to print all apples
        // Hint: inventory.forEach(System.out::println)

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 1.1: Filter apples heavier than 150g using streams");
        System.out.println("Exercise 1.2: Sort apples by color, then by weight");
        System.out.println("Exercise 1.3: Count the number of green apples using streams");
    }

    /**
     * Old-style filtering - lots of boilerplate!
     */
    public static List<Apple> filterGreenApplesOldWay(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (GREEN.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * TODO: Implement using streams
     */
    public static List<Apple> filterGreenApplesNewWay(List<Apple> inventory) {
        // TODO: Use Stream API
        // return inventory.stream()
        // .filter(apple -> GREEN.equals(apple.getColor()))
        // .collect(Collectors.toList());
        return new ArrayList<>();
    }

    /**
     * Generic filter method with predicate - preview of Chapter 2
     */
    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    private static List<Apple> createInventory() {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(80, GREEN));
        inventory.add(new Apple(155, GREEN));
        inventory.add(new Apple(120, RED));
        inventory.add(new Apple(180, RED));
        inventory.add(new Apple(100, GREEN));
        return inventory;
    }
}
