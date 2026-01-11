package com.modernjavainaction.part7_java21_features.chapter21_virtual_threads;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Chapter 21: Virtual Threads (Project Loom)
 * 
 * Virtual threads are lightweight threads that dramatically reduce the effort
 * of writing, maintaining, and observing high-throughput concurrent
 * applications.
 * 
 * Key concepts:
 * - Virtual threads are cheap to create (millions possible)
 * - They're managed by the JVM, not the OS
 * - Perfect for I/O-bound workloads
 * - Use the same Thread API you already know
 */
public class VirtualThreads {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Virtual Threads Demo ===\n");

        // 1. Creating a simple virtual thread
        simpleVirtualThread();

        // 2. Virtual thread executor
        virtualThreadExecutor();

        // 3. Comparing platform vs virtual threads
        compareThreadPerformance();

        // 4. Thread-per-request pattern
        threadPerRequestDemo();
    }

    /**
     * The simplest way to create a virtual thread
     */
    static void simpleVirtualThread() throws InterruptedException {
        System.out.println("1. Simple Virtual Thread Creation");
        System.out.println("-".repeat(40));

        // Using Thread.startVirtualThread()
        Thread vThread = Thread.startVirtualThread(() -> {
            System.out.println("Hello from virtual thread: " + Thread.currentThread());
            System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
        });

        vThread.join();

        // Using Thread.ofVirtual() builder
        Thread vThread2 = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> System.out.println("Named virtual thread: " + Thread.currentThread().getName()));

        vThread2.join();
        System.out.println();
    }

    /**
     * Using virtual thread executor for task submission
     */
    static void virtualThreadExecutor() throws InterruptedException {
        System.out.println("2. Virtual Thread Executor");
        System.out.println("-".repeat(40));

        // newVirtualThreadPerTaskExecutor creates a new virtual thread for each task
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 5; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    System.out.println("Task " + taskId + " running on: " + Thread.currentThread());
                    return taskId;
                });
            }
        } // Auto-closes and waits for all tasks

        System.out.println();
    }

    /**
     * Compare performance of platform threads vs virtual threads
     */
    static void compareThreadPerformance() {
        System.out.println("3. Performance Comparison");
        System.out.println("-".repeat(40));

        int numTasks = 10_000;

        // Virtual threads
        Instant start = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, numTasks).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofMillis(100));
                return i;
            }));
        }
        Duration virtualTime = Duration.between(start, Instant.now());

        // Platform threads (limited pool to avoid resource exhaustion)
        start = Instant.now();
        try (ExecutorService executor = Executors.newFixedThreadPool(100)) {
            IntStream.range(0, numTasks).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofMillis(100));
                return i;
            }));
        }
        Duration platformTime = Duration.between(start, Instant.now());

        System.out.printf("Virtual threads  (%,d tasks): %s%n", numTasks, virtualTime);
        System.out.printf("Platform threads (%,d tasks): %s%n", numTasks, platformTime);
        System.out.println("Virtual threads are ~" + (platformTime.toMillis() / Math.max(1, virtualTime.toMillis()))
                + "x faster!");
        System.out.println();
    }

    /**
     * Thread-per-request pattern - the classic blocking model made efficient
     */
    static void threadPerRequestDemo() throws InterruptedException {
        System.out.println("4. Thread-Per-Request Pattern");
        System.out.println("-".repeat(40));

        // Simulate handling many concurrent "requests"
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
                final int requestId = i;
                executor.submit(() -> handleRequest(requestId));
            }
        }

        System.out.println("All requests handled!");
    }

    static String handleRequest(int requestId) {
        try {
            // Simulate I/O operations (database, HTTP calls, etc.)
            Thread.sleep(Duration.ofMillis(50));
            if (requestId % 20 == 0) {
                System.out.println("Processed request #" + requestId);
            }
            return "Response for request " + requestId;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Interrupted";
        }
    }
}
