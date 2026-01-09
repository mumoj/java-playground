package com.modernjavainaction.part1_fundamentals.chapter02_behavior_parameterization;

import com.modernjavainaction.common.model.Apple;

import java.util.ArrayList;
import java.util.List;

import static com.modernjavainaction.common.model.Apple.Color.*;

/**
 * Chapter 2: Passing Code with Behavior Parameterization
 * 
 * Key concepts:
 * - Coping with changing requirements
 * - Behavior parameterization
 * - Anonymous classes
 * - Preview of lambdas
 */
public class BehaviorParameterization {

    public static void main(String[] args) {
        List<Apple> inventory = createInventory();

        System.out.println("=== Chapter 2: Behavior Parameterization ===\n");

        // Step 1: Hardcoded filtering (inflexible)
        System.out.println("1. Hardcoded green apple filter:");
        List<Apple> greenApples = filterGreenApples(inventory);
        greenApples.forEach(System.out::println);

        // Step 2: Parameterizing color (slightly better)
        System.out.println("\n2. Parameterized color filter:");
        List<Apple> redApples = filterApplesByColor(inventory, RED);
        redApples.forEach(System.out::println);

        // Step 3: Using strategy pattern with ApplePredicate interface
        System.out.println("\n3. Using ApplePredicate (Strategy Pattern):");
        List<Apple> heavyApples = filterApples(inventory, new AppleHeavyWeightPredicate());
        heavyApples.forEach(System.out::println);

        // Step 4: Anonymous class (verbose but flexible)
        System.out.println("\n4. Using anonymous class:");
        List<Apple> redHeavyApples = filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return RED.equals(apple.getColor()) && apple.getWeight() > 100;
            }
        });
        redHeavyApples.forEach(System.out::println);

        // Step 5: Lambda expression (most concise)
        System.out.println("\n5. Using lambda expression:");
        List<Apple> heavyGreenApples = filterApples(inventory,
                apple -> GREEN.equals(apple.getColor()) && apple.getWeight() > 100);
        heavyGreenApples.forEach(System.out::println);

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 2.1: Create ApplePredicate for red apples weighing more than 150g");
        System.out.println("Exercise 2.2: Implement prettyPrintApple that can print in different formats");
        System.out.println("Exercise 2.3: Use anonymous class to sort apples by weight descending");
        System.out.println("Exercise 2.4: Refactor the anonymous class sorting to use a lambda");
    }

    // Step 1: Hardcoded filter - inflexible
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (GREEN.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    // Step 2: Parameterized color
    public static List<Apple> filterApplesByColor(List<Apple> inventory, Apple.Color color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (color.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    // Step 3: Behavior parameterization with predicate
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate predicate) {
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

/**
 * Functional interface for apple predicates.
 * This is the Strategy pattern applied to filtering.
 */
@FunctionalInterface
interface ApplePredicate {
    boolean test(Apple apple);
}

/**
 * Concrete strategy: select heavy apples (weight > 150g)
 */
class AppleHeavyWeightPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getWeight() > 150;
    }
}

/**
 * Concrete strategy: select green apples
 */
class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return Apple.Color.GREEN.equals(apple.getColor());
    }
}

// TODO: Exercise 2.1 - Create AppleRedAndHeavyPredicate
