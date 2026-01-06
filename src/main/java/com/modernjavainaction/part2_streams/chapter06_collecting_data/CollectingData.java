package com.modernjavainaction.part2_streams.chapter06_collecting_data;

import com.modernjavainaction.common.model.Dish;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Chapter 6: Collecting Data with Streams
 * 
 * Key concepts:
 * - Collectors factory methods
 * - Reducing and summarizing
 * - Grouping
 * - Partitioning
 * - Custom collectors
 */
public class CollectingData {

    public static void main(String[] args) {
        List<Dish> menu = Dish.getMenu();

        System.out.println("=== Chapter 6: Collecting Data with Streams ===\n");

        // 1. Basic collectors
        System.out.println("1. Basic Collectors:");

        long count = menu.stream().collect(counting());
        System.out.println("  Count: " + count);

        Optional<Dish> maxCalorie = menu.stream()
                .collect(maxBy(Comparator.comparing(Dish::getCalories)));
        maxCalorie.ifPresent(d -> System.out.println("  Max calories: " + d));

        int totalCalories = menu.stream()
                .collect(summingInt(Dish::getCalories));
        System.out.println("  Total calories: " + totalCalories);

        double avgCalories = menu.stream()
                .collect(averagingInt(Dish::getCalories));
        System.out.println("  Average calories: " + avgCalories);

        // All stats at once
        IntSummaryStatistics stats = menu.stream()
                .collect(summarizingInt(Dish::getCalories));
        System.out.println("  Statistics: " + stats);

        // 2. Joining strings
        System.out.println("\n2. Joining Strings:");
        String shortMenu = menu.stream()
                .map(Dish::getName)
                .collect(joining(", "));
        System.out.println("  Menu: " + shortMenu);

        // 3. Reducing
        System.out.println("\n3. Reducing with Collectors:");
        int total = menu.stream()
                .collect(reducing(0, Dish::getCalories, Integer::sum));
        System.out.println("  Total (reducing): " + total);

        Optional<Dish> mostCaloric = menu.stream()
                .collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
        mostCaloric.ifPresent(d -> System.out.println("  Most caloric: " + d));

        // 4. Grouping
        System.out.println("\n4. Grouping:");
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream()
                .collect(groupingBy(Dish::getType));
        System.out.println("  By type: " + dishesByType);

        // Custom grouping
        Map<String, List<Dish>> dishesByCalorieLevel = menu.stream()
                .collect(groupingBy(dish -> {
                    if (dish.getCalories() <= 400)
                        return "DIET";
                    else if (dish.getCalories() <= 700)
                        return "NORMAL";
                    else
                        return "FAT";
                }));
        System.out.println("  By calorie level: " + dishesByCalorieLevel);

        // 5. Multi-level grouping
        System.out.println("\n5. Multi-level Grouping:");
        Map<Dish.Type, Map<String, List<Dish>>> dishesByTypeAndLevel = menu.stream()
                .collect(groupingBy(Dish::getType,
                        groupingBy(dish -> {
                            if (dish.getCalories() <= 400)
                                return "DIET";
                            else if (dish.getCalories() <= 700)
                                return "NORMAL";
                            else
                                return "FAT";
                        })));
        System.out.println("  By type and level: " + dishesByTypeAndLevel);

        // 6. Collecting in subgroups
        System.out.println("\n6. Collecting in Subgroups:");
        Map<Dish.Type, Long> countByType = menu.stream()
                .collect(groupingBy(Dish::getType, counting()));
        System.out.println("  Count by type: " + countByType);

        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        maxBy(Comparator.comparing(Dish::getCalories))));
        System.out.println("  Most caloric by type: " + mostCaloricByType);

        // Unwrap optional with collectingAndThen
        Map<Dish.Type, Dish> mostCaloricByTypeUnwrapped = menu.stream()
                .collect(groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(Comparator.comparing(Dish::getCalories)),
                                Optional::get)));
        System.out.println("  Most caloric by type (unwrapped): " + mostCaloricByTypeUnwrapped);

        // Sum calories by type
        Map<Dish.Type, Integer> totalCaloriesByType = menu.stream()
                .collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
        System.out.println("  Total calories by type: " + totalCaloriesByType);

        // 7. Partitioning
        System.out.println("\n7. Partitioning:");
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));
        System.out.println("  Vegetarian partition: " + partitionedMenu);

        // Multi-level partitioning
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianByType = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian,
                        groupingBy(Dish::getType)));
        System.out.println("  Vegetarian by type: " + vegetarianByType);

        // Most caloric in each partition
        Map<Boolean, Dish> mostCaloricPartitioned = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian,
                        collectingAndThen(
                                maxBy(Comparator.comparing(Dish::getCalories)),
                                Optional::get)));
        System.out.println("  Most caloric per partition: " + mostCaloricPartitioned);

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 6.1: Group dishes by calorie level (LOW < 400, MEDIUM 400-700, HIGH > 700)");
        System.out.println("Exercise 6.2: Find the most caloric dish for each type");
        System.out.println("Exercise 6.3: Partition prime numbers from 2 to 100");
        System.out.println("Exercise 6.4: Create a custom ToListCollector (advanced)");
    }

    // TODO: Exercise 6.3 - Prime numbers partitioning
    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        // Hint: Use IntStream.rangeClosed and partitioningBy
        return null;
    }

    public static boolean isPrime(int candidate) {
        // TODO: Implement prime check
        return false;
    }
}
