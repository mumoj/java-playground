package com.modernjavainaction.part5_concurrency.chapter16_completable_future;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Chapter 16: CompletableFuture: Composable Asynchronous Programming
 * 
 * Key concepts:
 * - Creating CompletableFutures
 * - Combining and composing futures
 * - Error handling
 * - Timeouts
 * - Best practices
 */
public class CompletableFutureExamples {

    private static final Random random = new Random();

    // Custom executor for I/O bound tasks
    private static final Executor executor = Executors.newFixedThreadPool(
            Math.min(4, 100), // At least 4 threads
            r -> {
                Thread t = new Thread(r);
                t.setDaemon(true); // Don't prevent JVM shutdown
                return t;
            });

    public static void main(String[] args) throws Exception {
        System.out.println("=== Chapter 16: CompletableFuture ===\n");

        // 1. Creating CompletableFutures
        System.out.println("1. Creating CompletableFutures:");
        demonstrateCreation();

        // 2. Chaining with thenApply, thenAccept, thenRun
        System.out.println("\n2. Chaining Transformations:");
        demonstrateChaining();

        // 3. Combining multiple futures
        System.out.println("\n3. Combining Futures:");
        demonstrateCombining();

        // 4. Error handling
        System.out.println("\n4. Error Handling:");
        demonstrateErrorHandling();

        // 5. Real-world example: Best price finder
        System.out.println("\n5. Best Price Finder (Practical Example):");
        demonstrateBestPriceFinder();

        // 6. Timeouts (Java 9+)
        System.out.println("\n6. Timeouts:");
        // orTimeout, completeOnTimeout are Java 9+
        System.out.println("   orTimeout() and completeOnTimeout() require Java 9+");
        System.out.println("   For Java 8, use get(timeout, unit) or external scheduling");

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 16.1: Implement parallel price fetching from multiple shops");
        System.out.println("Exercise 16.2: Add discount service that processes prices asynchronously");
        System.out.println("Exercise 16.3: Handle timeouts for slow shops (skip them)");
    }

    private static void demonstrateCreation() throws Exception {
        // 1. supplyAsync - for tasks returning a value
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return "Hello from supplyAsync";
        });
        System.out.println("  supplyAsync: " + future1.get());

        // 2. runAsync - for tasks returning void
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            delay(50);
            System.out.println("  runAsync executed!");
        });
        future2.join();

        // 3. completedFuture - already complete
        CompletableFuture<String> future3 = CompletableFuture.completedFuture("Already done!");
        System.out.println("  completedFuture: " + future3.get());

        // 4. With custom executor (recommended for I/O-bound tasks)
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            return "Using custom executor!";
        }, executor);
        System.out.println("  With executor: " + future4.get());
    }

    private static void demonstrateChaining() throws Exception {
        // thenApply - transform result (sync, on same thread)
        CompletableFuture<Integer> chain1 = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(String::length);
        System.out.println("  thenApply (length): " + chain1.get());

        // thenApplyAsync - transform result (async, on different thread)
        CompletableFuture<Integer> chain2 = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApplyAsync(str -> {
                    System.out.println("    thenApplyAsync on: " + Thread.currentThread().getName());
                    return str.length();
                }, executor);
        System.out.println("  thenApplyAsync: " + chain2.get());

        // thenAccept - consume result (returns Void)
        CompletableFuture.supplyAsync(() -> "Consumed value")
                .thenAccept(value -> System.out.println("  thenAccept: " + value))
                .join();

        // thenRun - run action after completion (no access to result)
        CompletableFuture.supplyAsync(() -> "Ignored value")
                .thenRun(() -> System.out.println("  thenRun: Completed!"))
                .join();

        // thenCompose - chain dependent futures (flatMap equivalent)
        CompletableFuture<String> composed = CompletableFuture.supplyAsync(() -> "shop1")
                .thenCompose(shop -> getPrice(shop));
        System.out.println("  thenCompose: " + composed.get());
    }

    private static void demonstrateCombining() throws Exception {
        // thenCombine - combine two independent futures
        CompletableFuture<Double> price = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return 100.0;
        });

        CompletableFuture<Double> discount = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return 0.9; // 10% discount
        });

        CompletableFuture<Double> finalPrice = price.thenCombine(discount,
                (p, d) -> p * d);
        System.out.println("  thenCombine (price * discount): " + finalPrice.get());

        // allOf - wait for all futures
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            delay(50);
            return "Future 1";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return "Future 2";
        });

        long start = System.currentTimeMillis();
        CompletableFuture.allOf(f1, f2).join();
        System.out.println("  allOf completed in: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("  Results: " + f1.join() + ", " + f2.join());

        // anyOf - first to complete
        CompletableFuture<Object> any = CompletableFuture.anyOf(
                CompletableFuture.supplyAsync(() -> {
                    delay(200);
                    return "Slow";
                }),
                CompletableFuture.supplyAsync(() -> {
                    delay(50);
                    return "Fast";
                }));
        System.out.println("  anyOf (first to complete): " + any.get());
    }

    private static void demonstrateErrorHandling() throws Exception {
        // exceptionally - handle exceptions
        CompletableFuture<String> withError = CompletableFuture.supplyAsync(() -> {
            if (true)
                throw new RuntimeException("Oops!");
            return "Success";
        }).exceptionally(ex -> {
            System.out.println("  exceptionally caught: " + ex.getMessage());
            return "Recovered value";
        });
        System.out.println("  After exceptionally: " + withError.get());

        // handle - handle both success and failure
        CompletableFuture<String> handled = CompletableFuture.supplyAsync(() -> {
            if (random.nextBoolean()) {
                throw new RuntimeException("Random failure!");
            }
            return "Success!";
        }).handle((result, ex) -> {
            if (ex != null) {
                return "Handled error: " + ex.getMessage();
            }
            return "Result: " + result;
        });
        System.out.println("  handle: " + handled.get());

        // whenComplete - peek at result/exception without changing it
        CompletableFuture<String> peeked = CompletableFuture.supplyAsync(() -> "Peek me!")
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("  whenComplete saw: " + result);
                    }
                });
        peeked.join();
    }

    private static void demonstrateBestPriceFinder() {
        List<String> shops = Arrays.asList("BestShop", "CompareIt", "BuyNow", "SaveMore");

        // Sequential - slow!
        long start = System.currentTimeMillis();
        List<String> pricesSeq = shops.stream()
                .map(shop -> String.format("%s: $%.2f", shop, getShopPrice(shop)))
                .collect(Collectors.toList());
        System.out.println("  Sequential: " + pricesSeq);
        System.out.println("  Time: " + (System.currentTimeMillis() - start) + "ms");

        // Parallel stream
        start = System.currentTimeMillis();
        List<String> pricesPar = shops.parallelStream()
                .map(shop -> String.format("%s: $%.2f", shop, getShopPrice(shop)))
                .collect(Collectors.toList());
        System.out.println("  Parallel stream: " + pricesPar);
        System.out.println("  Time: " + (System.currentTimeMillis() - start) + "ms");

        // CompletableFuture with custom executor - best for I/O!
        start = System.currentTimeMillis();
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s: $%.2f", shop, getShopPrice(shop)),
                        executor))
                .collect(Collectors.toList());

        List<String> pricesCF = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        System.out.println("  CompletableFuture: " + pricesCF);
        System.out.println("  Time: " + (System.currentTimeMillis() - start) + "ms");
    }

    // Simulated shop price lookup
    private static double getShopPrice(String shopName) {
        delay(200); // Simulate network delay
        return random.nextDouble() * 100 + 50;
    }

    private static CompletableFuture<String> getPrice(String shop) {
        return CompletableFuture.supplyAsync(() -> {
            delay(100);
            return shop + ": $" + (random.nextDouble() * 100);
        }, executor);
    }

    private static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
