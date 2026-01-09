package com.modernjavainaction.part5_concurrency.chapter15_completable_future_concepts;

import java.util.concurrent.*;
import java.util.function.IntConsumer;

/**
 * Chapter 15: Concepts behind CompletableFuture and Reactive Programming
 * 
 * Key concepts:
 * - Threads and higher-level abstractions
 * - Executors and thread pools
 * - Synchronous vs asynchronous APIs
 * - Box-and-channel model
 * - Publish-subscribe pattern basics
 */
public class FutureAndAsyncConcepts {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Chapter 15: Future and Async Concepts ===\n");

        // 1. Basic Future
        System.out.println("1. Basic Future:");
        demonstrateBasicFuture();

        // 2. Executors and Thread Pools
        System.out.println("\n2. Executors and Thread Pools:");
        demonstrateExecutors();

        // 3. Why async is better for I/O-bound tasks
        System.out.println("\n3. Async vs Sync:");
        System.out.println("   Synchronous: Thread blocked waiting for I/O");
        System.out.println("   Asynchronous: Thread released, callback invoked on completion");
        System.out.println("   For I/O-bound workloads, async scales better.\n");

        // 4. Simple async example
        System.out.println("4. Simple Async Pattern:");
        demonstrateAsyncPattern();

        // 5. Publish-Subscribe basics
        System.out.println("\n5. Publish-Subscribe Pattern:");
        demonstratePubSub();

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 15.1: Implement a simple async method using ExecutorService");
        System.out.println("Exercise 15.2: Create a Publisher/Subscriber for price updates");
        System.out.println("Exercise 15.3: Compare thread pool sizes for CPU vs I/O bound tasks");
    }

    private static void demonstrateBasicFuture() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit a callable task
        Future<String> future = executor.submit(() -> {
            Thread.sleep(500); // Simulate work
            return "Result from background task";
        });

        System.out.println("  Task submitted, doing other work...");

        // Block and get result (with timeout)
        try {
            String result = future.get(1, TimeUnit.SECONDS);
            System.out.println("  Result: " + result);
        } catch (TimeoutException e) {
            System.out.println("  Task timed out!");
        }

        executor.shutdown();
    }

    private static void demonstrateExecutors() throws Exception {
        System.out.println("  Common ExecutorService Types:");
        System.out.println("    - newSingleThreadExecutor(): 1 thread");
        System.out.println("    - newFixedThreadPool(n): n threads");
        System.out.println("    - newCachedThreadPool(): grows as needed");
        System.out.println("    - newScheduledThreadPool(n): for delayed/periodic tasks");

        // Fixed thread pool example
        int numCores = Runtime.getRuntime().availableProcessors();
        System.out.println("  \n  Available cores: " + numCores);
        System.out.println("  For CPU-bound: use cores or cores+1 threads");
        System.out.println("  For I/O-bound: use more threads (cores * ratio)");

        // Demonstrate thread pool
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("    Task " + taskId + " on " + Thread.currentThread().getName());
            });
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);
    }

    private static void demonstrateAsyncPattern() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Callback-based async (simulating what CompletableFuture does internally)
        System.out.println("  Starting async task with callback...");

        asyncOperation(executor, result -> {
            System.out.println("  [Callback] Result received: " + result);
        });

        System.out.println("  Main thread continues immediately!");
        Thread.sleep(600); // Wait for callback

        executor.shutdown();
    }

    private static void asyncOperation(ExecutorService executor, IntConsumer callback) {
        executor.submit(() -> {
            try {
                Thread.sleep(300); // Simulate async work
                callback.accept(42);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private static void demonstratePubSub() {
        SimplePublisher publisher = new SimplePublisher();

        // Subscribe
        publisher.subscribe(value -> System.out.println("    Subscriber 1 received: " + value));
        publisher.subscribe(value -> System.out.println("    Subscriber 2 received: " + value));

        // Publish
        System.out.println("  Publishing values...");
        publisher.publish(10);
        publisher.publish(20);
    }
}

// Simple publish-subscribe implementation
class SimplePublisher {
    private final java.util.List<IntConsumer> subscribers = new java.util.ArrayList<>();

    public void subscribe(IntConsumer subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(int value) {
        for (IntConsumer subscriber : subscribers) {
            subscriber.accept(value);
        }
    }
}
