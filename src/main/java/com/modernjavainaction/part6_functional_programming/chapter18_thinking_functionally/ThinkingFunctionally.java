package com.modernjavainaction.part6_functional_programming.chapter18_thinking_functionally;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Chapter 18: Thinking Functionally
 * 
 * Key concepts:
 * - What is functional programming?
 * - Referential transparency
 * - Pure functions (no side effects)
 * - Immutability
 * - Recursion vs iteration
 */
public class ThinkingFunctionally {

    public static void main(String[] args) {
        System.out.println("=== Chapter 18: Thinking Functionally ===\n");

        // 1. Pure functions
        System.out.println("1. Pure Functions:");
        demonstratePureFunctions();

        // 2. Referential transparency
        System.out.println("\n2. Referential Transparency:");
        demonstrateReferentialTransparency();

        // 3. Immutability
        System.out.println("\n3. Immutability:");
        demonstrateImmutability();

        // 4. Recursion
        System.out.println("\n4. Recursion vs Iteration:");
        demonstrateRecursion();

        // 5. Tail recursion
        System.out.println("\n5. Tail Recursion:");
        demonstrateTailRecursion();

        System.out.println("\n=== KEY PRINCIPLES ===");
        System.out.println("• No shared mutable data");
        System.out.println("• Functions should have no side effects");
        System.out.println("• Prefer immutable data structures");
        System.out.println("• Use recursion instead of iteration (when practical)");
        System.out.println("• Functions are first-class values");

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 18.1: Identify impure functions and make them pure");
        System.out.println("Exercise 18.2: Rewrite an iterative algorithm using recursion");
        System.out.println("Exercise 18.3: Create immutable versions of common operations");
    }

    private static void demonstratePureFunctions() {
        // PURE: Same input always gives same output, no side effects
        Function<Integer, Integer> pureDouble = x -> x * 2;
        System.out.println("  Pure function: double(5) = " + pureDouble.apply(5));
        System.out.println("  Called again: double(5) = " + pureDouble.apply(5));

        // IMPURE: Has side effects (modifies external state)
        System.out.println("\n  Impure examples (avoid these):");
        System.out.println("    • Writing to console/file");
        System.out.println("    • Modifying global/static variables");
        System.out.println("    • Changing input parameters");
        System.out.println("    • Throwing exceptions");
        System.out.println("    • Random number generation");
    }

    private static void demonstrateReferentialTransparency() {
        // Referential transparency: can replace function call with its result

        // This is referentially transparent:
        int sum = add(2, 3); // Can be replaced with 5 anywhere
        System.out.println("  add(2, 3) = " + sum + " (can always replace with 5)");

        // NOT referentially transparent:
        // - System.currentTimeMillis() - returns different value each time
        // - random.nextInt() - different value each time
        // - readFile() - file content may change

        System.out.println("\n  Benefits of referential transparency:");
        System.out.println("    • Easier to reason about code");
        System.out.println("    • Safe to parallelize (no shared state)");
        System.out.println("    • Enables memoization/caching");
        System.out.println("    • Easier to test");
    }

    private static int add(int a, int b) {
        return a + b;
    }

    private static void demonstrateImmutability() {
        // Mutable - AVOID
        List<String> mutableList = new ArrayList<>();
        mutableList.add("hello");
        mutableList.add("world");
        // Problem: Anyone with a reference can modify this list!

        // Immutable - PREFER
        List<String> immutableList = Collections.unmodifiableList(
                java.util.Arrays.asList("hello", "world"));

        System.out.println("  Immutable list: " + immutableList);

        try {
            immutableList.add("!"); // Throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("  Cannot modify immutable list! (Good!)");
        }

        // Functional update: create new collection
        List<String> updated = appendImmutably(immutableList, "!");
        System.out.println("  Original: " + immutableList);
        System.out.println("  Updated (new list): " + updated);
    }

    // Functional approach: return new list instead of modifying input
    private static List<String> appendImmutably(List<String> list, String item) {
        List<String> result = new ArrayList<>(list);
        result.add(item);
        return Collections.unmodifiableList(result);
    }

    private static void demonstrateRecursion() {
        // Iterative factorial
        System.out.println("  Iterative factorial(5) = " + factorialIterative(5));

        // Recursive factorial
        System.out.println("  Recursive factorial(5) = " + factorialRecursive(5));

        // Recursive subsets
        List<Integer> set = java.util.Arrays.asList(1, 2, 3);
        System.out.println("  Subsets of [1,2,3]: " + subsets(set));
    }

    // Iterative - uses mutable state
    public static long factorialIterative(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // Recursive - no mutable state, but not tail-recursive
    public static long factorialRecursive(int n) {
        return n <= 1 ? 1 : n * factorialRecursive(n - 1);
    }

    // Generate all subsets of a list
    public static <T> List<List<T>> subsets(List<T> list) {
        if (list.isEmpty()) {
            List<List<T>> result = new ArrayList<>();
            result.add(Collections.emptyList());
            return result;
        }

        T first = list.get(0);
        List<T> rest = list.subList(1, list.size());

        List<List<T>> subsetsOfRest = subsets(rest);
        List<List<T>> subsetsIncludingFirst = insertAll(first, subsetsOfRest);

        return concat(subsetsOfRest, subsetsIncludingFirst);
    }

    private static <T> List<List<T>> insertAll(T first, List<List<T>> lists) {
        List<List<T>> result = new ArrayList<>();
        for (List<T> list : lists) {
            List<T> newList = new ArrayList<>();
            newList.add(first);
            newList.addAll(list);
            result.add(newList);
        }
        return result;
    }

    private static <T> List<T> concat(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }

    private static void demonstrateTailRecursion() {
        // Regular recursion - stack grows with each call
        // factorialRecursive uses: n * factorialRecursive(n-1)
        // The multiplication happens AFTER the recursive call returns

        // Tail recursion - accumulator contains intermediate result
        // Last operation is the recursive call itself
        System.out.println("  Tail-recursive factorial(5) = " + factorialTailRecursive(5, 1));

        System.out.println("\n  Note: Java doesn't optimize tail recursion!");
        System.out.println("  Still risk StackOverflowError for large inputs.");
        System.out.println("  Use iteration for performance-critical code.");
        System.out.println("  Scala/Kotlin can optimize with @tailrec annotation.");
    }

    // Tail-recursive factorial with accumulator
    public static long factorialTailRecursive(int n, long accumulator) {
        return n <= 1 ? accumulator : factorialTailRecursive(n - 1, n * accumulator);
    }
}
