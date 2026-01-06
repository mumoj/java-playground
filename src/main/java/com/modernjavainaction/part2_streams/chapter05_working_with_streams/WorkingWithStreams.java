package com.modernjavainaction.part2_streams.chapter05_working_with_streams;

import com.modernjavainaction.common.model.Dish;
import com.modernjavainaction.common.model.Trader;
import com.modernjavainaction.common.model.Transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Chapter 5: Working with Streams
 * 
 * Key concepts:
 * - Filtering: filter, distinct
 * - Slicing: takeWhile, dropWhile, limit, skip
 * - Mapping: map, flatMap
 * - Finding and matching: anyMatch, allMatch, noneMatch, findFirst, findAny
 * - Reducing: reduce, count, sum
 * - Numeric streams: IntStream, LongStream, DoubleStream
 * - Building streams: Stream.of, Arrays.stream, Files.lines, iterate, generate
 */
public class WorkingWithStreams {

    public static void main(String[] args) {
        List<Dish> menu = Dish.getMenu();

        System.out.println("=== Chapter 5: Working with Streams ===\n");

        // 1. Filtering
        System.out.println("1. Filtering:");
        List<Dish> vegetarianDishes = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());
        System.out.println("  Vegetarian dishes: " + vegetarianDishes);

        // Distinct
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        List<Integer> uniqueNumbers = numbers.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("  Unique numbers: " + uniqueNumbers);

        // 2. Slicing (Java 9+)
        System.out.println("\n2. Slicing with limit and skip:");
        List<Dish> firstThree = menu.stream()
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("  First 3 dishes: " + firstThree);

        List<Dish> skipTwo = menu.stream()
                .skip(2)
                .collect(Collectors.toList());
        System.out.println("  Skip first 2: " + skipTwo);

        // 3. Mapping
        System.out.println("\n3. Mapping:");
        List<String> dishNames = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
        System.out.println("  Dish names: " + dishNames);

        // Map to length
        List<Integer> nameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println("  Name lengths: " + nameLengths);

        // 4. flatMap - Flattening streams
        System.out.println("\n4. flatMap:");
        List<String> words = Arrays.asList("Hello", "World");
        List<String> uniqueChars = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("  Unique characters: " + uniqueChars);

        // 5. Finding and matching
        System.out.println("\n5. Finding and Matching:");
        boolean hasVegetarian = menu.stream()
                .anyMatch(Dish::isVegetarian);
        System.out.println("  Any vegetarian? " + hasVegetarian);

        boolean allLowCalorie = menu.stream()
                .allMatch(d -> d.getCalories() < 1000);
        System.out.println("  All under 1000 calories? " + allLowCalorie);

        boolean noHighCalorie = menu.stream()
                .noneMatch(d -> d.getCalories() >= 1000);
        System.out.println("  None >= 1000 calories? " + noHighCalorie);

        Optional<Dish> anyDish = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();
        anyDish.ifPresent(d -> System.out.println("  Found vegetarian: " + d));

        // 6. Reducing
        System.out.println("\n6. Reducing:");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("  Sum (with identity): " + sum);

        Optional<Integer> max = nums.stream().reduce(Integer::max);
        max.ifPresent(m -> System.out.println("  Max: " + m));

        int totalCalories = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);
        System.out.println("  Total calories: " + totalCalories);

        // 7. Numeric streams
        System.out.println("\n7. Numeric Streams (avoiding boxing):");
        int totalCaloriesEfficient = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();
        System.out.println("  Total calories (IntStream): " + totalCaloriesEfficient);

        // Ranges
        long evenCount = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0)
                .count();
        System.out.println("  Even numbers 1-100: " + evenCount);

        // Pythagorean triples
        System.out.println("  First 5 Pythagorean triples:");
        IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .mapToObj(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) }))
                .limit(5)
                .forEach(t -> System.out.println("    " + Arrays.toString(t)));

        // 8. Building streams
        System.out.println("\n8. Building Streams:");
        Stream<String> streamOf = Stream.of("Java", "8", "Streams");
        System.out.println("  Stream.of: " + streamOf.collect(Collectors.toList()));

        int[] numArray = { 1, 2, 3, 4, 5 };
        System.out.println("  Arrays.stream sum: " + Arrays.stream(numArray).sum());

        // Infinite streams
        System.out.println("  First 10 even numbers (iterate): " +
                Stream.iterate(0, n -> n + 2)
                        .limit(10)
                        .collect(Collectors.toList()));

        System.out.println("  5 random numbers (generate): " +
                Stream.generate(Math::random)
                        .limit(5)
                        .collect(Collectors.toList()));

        System.out.println("\n=== TRADER EXERCISES (from the book) ===");
        traderExercises();
    }

    /**
     * Classic exercises from the book using Trader and Transaction
     */
    private static void traderExercises() {
        List<Transaction> transactions = Transaction.getTransactions();

        System.out.println("Transactions: " + transactions);

        // TODO: Exercise 5.1 - Find all transactions in 2011 and sort by value (small
        // to high)
        System.out.println("\nExercise 5.1: All transactions in 2011, sorted by value");
        // Your solution here

        // TODO: Exercise 5.2 - What are all the unique cities where traders work?
        System.out.println("\nExercise 5.2: Unique cities");
        // Your solution here

        // TODO: Exercise 5.3 - Find all traders from Cambridge and sort by name
        System.out.println("\nExercise 5.3: Traders from Cambridge, sorted by name");
        // Your solution here

        // TODO: Exercise 5.4 - Return a string of all traders' names sorted
        // alphabetically
        System.out.println("\nExercise 5.4: All trader names (sorted, comma-separated)");
        // Your solution here

        // TODO: Exercise 5.5 - Are any traders based in Milan?
        System.out.println("\nExercise 5.5: Any traders in Milan?");
        // Your solution here

        // TODO: Exercise 5.6 - Print all transaction values from traders in Cambridge
        System.out.println("\nExercise 5.6: Transaction values from Cambridge traders");
        // Your solution here

        // TODO: Exercise 5.7 - What's the highest value of all transactions?
        System.out.println("\nExercise 5.7: Highest transaction value");
        // Your solution here

        // TODO: Exercise 5.8 - Find the transaction with the smallest value
        System.out.println("\nExercise 5.8: Smallest transaction");
        // Your solution here
    }
}
