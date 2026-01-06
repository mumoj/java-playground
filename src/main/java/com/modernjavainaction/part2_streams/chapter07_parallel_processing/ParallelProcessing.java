package com.modernjavainaction.part2_streams.chapter07_parallel_processing;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Chapter 7: Parallel Data Processing and Performance
 * 
 * Key concepts:
 * - Parallel streams
 * - Fork/Join framework
 * - Spliterator
 * - Performance considerations
 * - When to use parallel streams
 */
public class ParallelProcessing {

    public static void main(String[] args) {
        System.out.println("=== Chapter 7: Parallel Data Processing ===\n");

        long n = 10_000_000L;

        // 1. Sequential vs Parallel streams
        System.out.println("1. Sequential vs Parallel Streams:");
        System.out.println("   Summing numbers from 1 to " + n);

        // Sequential
        long start = System.currentTimeMillis();
        long sum1 = sequentialSum(n);
        long duration1 = System.currentTimeMillis() - start;
        System.out.println("   Sequential sum: " + sum1 + " (took " + duration1 + "ms)");

        // Parallel
        start = System.currentTimeMillis();
        long sum2 = parallelSum(n);
        long duration2 = System.currentTimeMillis() - start;
        System.out.println("   Parallel sum: " + sum2 + " (took " + duration2 + "ms)");

        // LongStream (much faster - avoids boxing!)
        start = System.currentTimeMillis();
        long sum3 = rangedSum(n);
        long duration3 = System.currentTimeMillis() - start;
        System.out.println("   LongStream.rangeClosed sum: " + sum3 + " (took " + duration3 + "ms)");

        // Parallel LongStream
        start = System.currentTimeMillis();
        long sum4 = parallelRangedSum(n);
        long duration4 = System.currentTimeMillis() - start;
        System.out.println("   Parallel LongStream sum: " + sum4 + " (took " + duration4 + "ms)");

        // 2. Fork/Join Framework
        System.out.println("\n2. Fork/Join Framework:");
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);

        start = System.currentTimeMillis();
        long forkJoinSum = new ForkJoinPool().invoke(task);
        long duration5 = System.currentTimeMillis() - start;
        System.out.println("   Fork/Join sum: " + forkJoinSum + " (took " + duration5 + "ms)");

        // 3. When N O T to use parallel streams
        System.out.println("\n3. ⚠️ WRONG: Parallel with shared mutable state:");
        demonstrateSideEffectDanger();

        // 4. Guidelines
        System.out.println("\n4. Guidelines for using parallel streams:");
        printGuidelines();

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 7.1: Benchmark iterate vs LongStream.rangeClosed for parallel sum");
        System.out.println("Exercise 7.2: Implement a parallel word counter using Spliterator");
        System.out.println("Exercise 7.3: Find cases where parallel is slower than sequential");
    }

    // Sequential sum with iterate
    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    // Parallel sum with iterate (actually slow due to iterate being hard to
    // parallelize!)
    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    // Using LongStream.rangeClosed (avoids boxing, easy to parallelize)
    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long::sum);
    }

    // Parallel LongStream (best performance)
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    // WRONG! Demonstrates the danger of shared mutable state
    private static void demonstrateSideEffectDanger() {
        Accumulator accumulator = new Accumulator();

        // This is WRONG - modifying shared state in parallel
        LongStream.rangeClosed(1, 1000)
                .parallel()
                .forEach(accumulator::add);

        System.out.println("   Expected: " + (1000 * 1001 / 2));
        System.out.println("   Got (race condition!): " + accumulator.total);
        System.out.println("   ⚠️ Results may vary due to race conditions!");
    }

    private static void printGuidelines() {
        System.out.println("   ✓ Measure, measure, measure! Don't assume parallel is faster.");
        System.out.println("   ✓ Watch out for boxing. Use primitive streams when possible.");
        System.out.println("   ✓ Some operations (limit, findFirst) are expensive in parallel.");
        System.out.println("   ✓ Consider the cost of splitting and merging vs computation.");
        System.out.println("   ✓ ArrayList/arrays parallelize well; LinkedList doesn't.");
        System.out.println("   ✗ Avoid shared mutable state!");
        System.out.println("   ✗ iterate() is hard to parallelize efficiently.");
    }
}

/**
 * Mutable accumulator - DO NOT use with parallel streams!
 */
class Accumulator {
    public long total = 0;

    public void add(long value) {
        total += value; // NOT thread-safe!
    }
}

/**
 * Fork/Join implementation for summing an array
 */
class ForkJoinSumCalculator extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;

    // Don't split below this threshold
    public static final long THRESHOLD = 10_000;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;

        // Base case: compute directly
        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        // Split into two subtasks
        int mid = start + length / 2;
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, mid);
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, mid, end);

        // Fork left task to another thread
        leftTask.fork();

        // Compute right task in this thread
        Long rightResult = rightTask.compute();

        // Wait for left result and combine
        Long leftResult = leftTask.join();

        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}
