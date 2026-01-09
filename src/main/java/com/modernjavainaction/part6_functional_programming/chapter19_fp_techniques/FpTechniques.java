package com.modernjavainaction.part6_functional_programming.chapter19_fp_techniques;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Chapter 19: Functional Programming Techniques
 * 
 * Key concepts:
 * - Higher-order functions
 * - Currying
 * - Partial application
 * - Persistent data structures
 * - Lazy evaluation
 * - Pattern matching (conceptual - full support in Java 17+)
 */
public class FpTechniques {

    public static void main(String[] args) {
        System.out.println("=== Chapter 19: Functional Programming Techniques ===\n");

        // 1. Higher-order functions
        System.out.println("1. Higher-Order Functions:");
        demonstrateHigherOrderFunctions();

        // 2. Currying
        System.out.println("\n2. Currying:");
        demonstrateCurrying();

        // 3. Lazy evaluation
        System.out.println("\n3. Lazy Evaluation:");
        demonstrateLazyEvaluation();

        // 4. Infinite streams
        System.out.println("\n4. Infinite Streams:");
        demonstrateInfiniteStreams();

        // 5. Persistent data structures
        System.out.println("\n5. Persistent Data Structures:");
        demonstratePersistentDS();

        // 6. Pattern matching preview
        System.out.println("\n6. Pattern Matching (Java 17+):");
        demonstratePatternMatching();

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 19.1: Implement a curried function for calculating distance");
        System.out.println("Exercise 19.2: Create a lazy fibonacci sequence using Supplier");
        System.out.println("Exercise 19.3: Implement a simple persistent linked list");
    }

    private static void demonstrateHigherOrderFunctions() {
        // Functions that take or return other functions

        // Returning a function
        Function<Integer, Function<Integer, Integer>> adder = x -> y -> x + y;
        Function<Integer, Integer> add5 = adder.apply(5);
        System.out.println("  add5(10) = " + add5.apply(10));

        // Taking a function as argument
        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, Integer> doubleIt = x -> x * 2;

        System.out.println("  compose(square, doubleIt)(3) = " +
                compose(square, doubleIt).apply(3)); // (3*3)*2 = 18

        // Function repetition
        Function<Integer, Integer> triple = x -> x * 3;
        Function<Integer, Integer> tripleThreeTimes = repeat(3, triple);
        System.out.println("  repeat(3, triple)(2) = " + tripleThreeTimes.apply(2)); // 2*3*3*3 = 54
    }

    // Compose two functions
    private static <T> Function<T, T> compose(Function<T, T> f, Function<T, T> g) {
        return x -> g.apply(f.apply(x)); // g(f(x))
    }

    // Repeat a function n times
    private static <T> Function<T, T> repeat(int n, Function<T, T> f) {
        return n == 0 ? Function.identity() : compose(f, repeat(n - 1, f));
    }

    private static void demonstrateCurrying() {
        // Currying: transforming a function with multiple arguments
        // into a sequence of functions each with a single argument

        // Non-curried: (a, b, c) -> result
        // Curried: a -> (b -> (c -> result))

        // Regular function with 3 parameters
        TriFunction<Double, Double, Double, Double> converter = (factor, baseline, temp) -> temp * factor + baseline;

        // Usage: convert Celsius to Fahrenheit
        double celsius = 100;
        double fahrenheit = converter.apply(9.0 / 5, 32.0, celsius);
        System.out.println("  Non-curried: " + celsius + "°C = " + fahrenheit + "°F");

        // Curried version
        Function<Double, Function<Double, Function<Double, Double>>> curriedConverter = factor -> baseline -> temp -> temp
                * factor + baseline;

        // Create specialized converters
        Function<Double, Double> celsiusToFahrenheit = curriedConverter.apply(9.0 / 5).apply(32.0);
        Function<Double, Double> kilometerToMile = curriedConverter.apply(0.6214).apply(0.0);

        System.out.println("  Curried celsius->fahrenheit(100) = " + celsiusToFahrenheit.apply(100.0));
        System.out.println("  Curried km->mile(10) = " + kilometerToMile.apply(10.0));

        // The benefit: we can partially apply arguments!
    }

    private static void demonstrateLazyEvaluation() {
        // Lazy evaluation: defer computation until result is needed

        // Eager: computed immediately
        int eagerResult = expensiveComputation();
        System.out.println("  Eager result: " + eagerResult);

        // Lazy: wrap in Supplier
        Supplier<Integer> lazyResult = () -> expensiveComputation();
        System.out.println("  Lazy supplier created (no computation yet)");
        System.out.println("  Now computing: " + lazyResult.get());

        // Memoized lazy (compute once, cache result)
        Supplier<Integer> memoized = memoize(() -> {
            System.out.println("    Computing...");
            return 42;
        });

        System.out.println("  First call: " + memoized.get());
        System.out.println("  Second call (cached): " + memoized.get());
    }

    private static int expensiveComputation() {
        // Simulate expensive work
        return 42;
    }

    // Memoizing Supplier (compute once, cache result)
    private static <T> Supplier<T> memoize(Supplier<T> supplier) {
        return new Supplier<T>() {
            private T cached = null;
            private boolean computed = false;

            @Override
            public synchronized T get() {
                if (!computed) {
                    cached = supplier.get();
                    computed = true;
                }
                return cached;
            }
        };
    }

    private static void demonstrateInfiniteStreams() {
        // Streams can represent infinite sequences!

        // Fibonacci sequence (lazy, infinite) - Java 8 compatible
        System.out.println("  First 10 Fibonacci numbers:");
        // Using Stream.generate with a holder class for state
        int[] fibState = { 0, 1 };
        java.util.stream.Stream.generate(() -> {
            int result = fibState[0];
            int next = fibState[0] + fibState[1];
            fibState[0] = fibState[1];
            fibState[1] = next;
            return result;
        }).limit(10).forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Prime numbers (infinite)
        System.out.println("  First 10 primes:");
        primes().limit(10).forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    // Infinite stream of prime numbers using Sieve of Eratosthenes concept
    private static IntStream primes() {
        return IntStream.iterate(2, n -> n + 1)
                .filter(FpTechniques::isPrime);
    }

    private static boolean isPrime(int n) {
        return IntStream.rangeClosed(2, (int) Math.sqrt(n))
                .noneMatch(i -> n % i == 0);
    }

    private static void demonstratePersistentDS() {
        // Persistent data structures: immutable but efficiently share structure

        System.out.println("  Persistent data structures share structure:");
        System.out.println("    Old: [1, 2, 3]");
        System.out.println("    New: [0, 1, 2, 3]  (shares [1, 2, 3] with Old)");
        System.out.println("\n  Benefits:");
        System.out.println("    • Safe to share (immutable)");
        System.out.println("    • Undo/history is cheap");
        System.out.println("    • Thread-safe by design");
        System.out.println("\n  Libraries:");
        System.out.println("    • pcollections (Java)");
        System.out.println("    • Vavr (formerly Javaslang)");
        System.out.println("    • Eclipse Collections");

        // Simple persistent list example
        PersistentList<Integer> empty = new EmptyList<>();
        PersistentList<Integer> list1 = empty.cons(3).cons(2).cons(1);
        PersistentList<Integer> list2 = list1.cons(0); // Shares list1's structure!

        System.out.println("\n  Simple persistent list demo:");
        System.out.println("    list1: " + list1);
        System.out.println("    list2 (with 0 prepended): " + list2);
    }

    private static void demonstratePatternMatching() {
        System.out.println("  Full pattern matching requires Java 17+");
        System.out.println("  With sealed classes and switch expressions.\n");

        System.out.println("  Java 17+ example:");
        System.out.println("  ```java");
        System.out.println("  sealed interface Expr permits Num, Add, Mul {}");
        System.out.println("  record Num(int value) implements Expr {}");
        System.out.println("  record Add(Expr left, Expr right) implements Expr {}");
        System.out.println("  record Mul(Expr left, Expr right) implements Expr {}");
        System.out.println("");
        System.out.println("  int eval(Expr expr) {");
        System.out.println("      return switch (expr) {");
        System.out.println("          case Num(var value) -> value;");
        System.out.println("          case Add(var l, var r) -> eval(l) + eval(r);");
        System.out.println("          case Mul(var l, var r) -> eval(l) * eval(r);");
        System.out.println("      };");
        System.out.println("  }");
        System.out.println("  ```");

        // Simpler Java 8 approach with visitor pattern or instanceof
        System.out.println("\n  Java 8 workaround: visitor pattern or instanceof chains");
    }
}

// Helper functional interface for 3-argument function
@FunctionalInterface
interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}

// Simple persistent linked list
interface PersistentList<T> {
    T head();

    PersistentList<T> tail();

    PersistentList<T> cons(T element);

    boolean isEmpty();
}

class EmptyList<T> implements PersistentList<T> {
    @Override
    public T head() {
        throw new UnsupportedOperationException("empty");
    }

    @Override
    public PersistentList<T> tail() {
        throw new UnsupportedOperationException("empty");
    }

    @Override
    public PersistentList<T> cons(T element) {
        return new NonEmptyList<>(element, this);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "[]";
    }
}

class NonEmptyList<T> implements PersistentList<T> {
    private final T head;
    private final PersistentList<T> tail;

    NonEmptyList(T head, PersistentList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public T head() {
        return head;
    }

    @Override
    public PersistentList<T> tail() {
        return tail;
    }

    @Override
    public PersistentList<T> cons(T element) {
        return new NonEmptyList<>(element, this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        PersistentList<T> current = this;
        while (!current.isEmpty()) {
            sb.append(current.head());
            current = current.tail();
            if (!current.isEmpty())
                sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
