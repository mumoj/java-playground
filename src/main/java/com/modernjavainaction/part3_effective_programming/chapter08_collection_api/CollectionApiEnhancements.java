package com.modernjavainaction.part3_effective_programming.chapter08_collection_api;

import java.util.*;

/**
 * Chapter 8: Collection API Enhancements
 * 
 * Key concepts:
 * - Collection factories (List.of, Set.of, Map.of)
 * - removeIf, replaceAll
 * - Map enhancements (forEach, compute, merge)
 */
public class CollectionApiEnhancements {

    public static void main(String[] args) {
        System.out.println("=== Chapter 8: Collection API Enhancements ===\n");

        // 1. Collection Factories (Java 9+)
        // Note: These require Java 9+ but we include for completeness
        System.out.println("1. Collection Factories (Java 9+):");
        // List.of - immutable list
        // List<String> friends = List.of("Alice", "Bob", "Charlie");
        // Set.of - immutable set
        // Set<String> cities = Set.of("London", "Paris", "Tokyo");
        // Map.of - immutable map
        // Map<String, Integer> ageMap = Map.of("Alice", 30, "Bob", 25);

        // Java 8 equivalent:
        List<String> friends = Collections.unmodifiableList(
                Arrays.asList("Alice", "Bob", "Charlie"));
        System.out.println("  Immutable list: " + friends);

        Set<String> cities = Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList("London", "Paris", "Tokyo")));
        System.out.println("  Immutable set: " + cities);

        Map<String, Integer> ageMap = new HashMap<>();
        ageMap.put("Alice", 30);
        ageMap.put("Bob", 25);
        Map<String, Integer> immutableAgeMap = Collections.unmodifiableMap(ageMap);
        System.out.println("  Immutable map: " + immutableAgeMap);

        // 2. removeIf
        System.out.println("\n2. removeIf:");
        List<String> names = new ArrayList<>(Arrays.asList(
                "Alice", "Bob", "Andrew", "Charlie", "Anna"));
        System.out.println("  Before: " + names);
        names.removeIf(name -> name.startsWith("A"));
        System.out.println("  After removing names starting with 'A': " + names);

        // 3. replaceAll
        System.out.println("\n3. replaceAll:");
        List<String> colors = new ArrayList<>(Arrays.asList("red", "green", "blue"));
        System.out.println("  Before: " + colors);
        colors.replaceAll(String::toUpperCase);
        System.out.println("  After replaceAll(toUpperCase): " + colors);

        // 4. Map forEach
        System.out.println("\n4. Map.forEach:");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        scores.forEach((name, score) -> System.out.println("  " + name + ": " + score));

        // 5. Map sorting
        System.out.println("\n5. Map Sorting:");
        System.out.println("  Sorted by key:");
        scores.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.println("    " + e.getKey() + ": " + e.getValue()));

        System.out.println("  Sorted by value (descending):");
        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.println("    " + e.getKey() + ": " + e.getValue()));

        // 6. getOrDefault
        System.out.println("\n6. getOrDefault:");
        String davidScore = scores.getOrDefault("David", -1).toString();
        System.out.println("  David's score (not in map): " + davidScore);

        // 7. computeIfAbsent, computeIfPresent, compute
        System.out.println("\n7. Compute methods:");
        Map<String, List<String>> friendsByLetter = new HashMap<>();

        // computeIfAbsent - useful for caching pattern
        friendsByLetter.computeIfAbsent("A", k -> new ArrayList<>()).add("Alice");
        friendsByLetter.computeIfAbsent("A", k -> new ArrayList<>()).add("Anna");
        friendsByLetter.computeIfAbsent("B", k -> new ArrayList<>()).add("Bob");
        System.out.println("  Friends by letter: " + friendsByLetter);

        // computeIfPresent - only compute if key exists
        Map<String, Integer> wordCount = new HashMap<>();
        wordCount.put("hello", 5);
        wordCount.computeIfPresent("hello", (key, val) -> val + 1);
        wordCount.computeIfPresent("world", (key, val) -> val + 1); // No effect
        System.out.println("  Word count after computeIfPresent: " + wordCount);

        // 8. merge - useful for combining values
        System.out.println("\n8. merge:");
        Map<String, Integer> movieRatings = new HashMap<>();
        movieRatings.put("Inception", 8);
        movieRatings.put("Interstellar", 9);

        // Merge new rating (take max)
        movieRatings.merge("Inception", 10, Integer::max);
        movieRatings.merge("Matrix", 9, Integer::max); // New entry
        System.out.println("  Movie ratings after merge: " + movieRatings);

        // Word frequency counting with merge
        String text = "the quick brown fox jumps over the lazy dog the dog slept";
        Map<String, Long> wordFreq = new HashMap<>();
        for (String word : text.split(" ")) {
            wordFreq.merge(word, 1L, Long::sum);
        }
        System.out.println("  Word frequency: " + wordFreq);

        // 9. replace and replaceAll for Map
        System.out.println("\n9. Map replace methods:");
        Map<String, String> favorites = new HashMap<>();
        favorites.put("Alice", "Pizza");
        favorites.put("Bob", "Burger");

        favorites.replace("Alice", "Sushi"); // Unconditional replace
        favorites.replace("Charlie", "Tacos"); // No effect - key doesn't exist
        System.out.println("  After replace: " + favorites);

        favorites.replace("Bob", "Burger", "Steak"); // Conditional replace
        System.out.println("  After conditional replace: " + favorites);

        favorites.replaceAll((name, food) -> food.toUpperCase());
        System.out.println("  After replaceAll: " + favorites);

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 8.1: Use computeIfAbsent to implement a simple cache");
        System.out.println("Exercise 8.2: Count word frequency in a file using merge");
        System.out.println("Exercise 8.3: Implement a grouping function using computeIfAbsent");
    }
}
