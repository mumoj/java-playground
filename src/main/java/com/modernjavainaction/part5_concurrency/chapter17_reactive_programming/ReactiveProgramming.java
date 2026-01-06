package com.modernjavainaction.part5_concurrency.chapter17_reactive_programming;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Chapter 17: Reactive Programming
 * 
 * NOTE: This example uses java.util.concurrent.Flow which requires Java 9+.
 * 
 * Key concepts:
 * - Reactive Manifesto principles
 * - Publish-subscribe pattern
 * - Backpressure
 * - Flow API (Java 9+)
 */
public class ReactiveProgramming {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Chapter 17: Reactive Programming (Java 9 Flow API) ===\n");

        // 1. Reactive Manifesto
        System.out.println("1. Reactive Manifesto Principles:");
        System.out.println("   - Responsive: Rapid and consistent response times");
        System.out.println("   - Resilient: Stays responsive during failures");
        System.out.println("   - Elastic: Scales up and down as needed");
        System.out.println("   - Message Driven: Asynchronous message passing\n");

        // 2. Publisher-Subscriber (Java 9+ Flow API)
        System.out.println("2. Publisher-Subscriber Pattern with Java 9 Flow API:");
        demonstrateFlowApi();

        // 3. Backpressure
        System.out.println("\n3. Backpressure:");
        System.out.println("   The consumer requests items when it is ready to process them.");
        System.out.println("   In the example below, the subscriber requests one item at a time.\n");

        // Give async processing time to complete
        Thread.sleep(2000);
    }

    private static void demonstrateFlowApi() {
        System.out.println("   Creating Publisher (SubmissionPublisher)...");
        // Create a Publisher
        // SubmissionPublisher is a simple standard implementation of Publisher provided
        // by JDK 9
        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {

            System.out.println("   Creating Subscriber...");
            // Create a Subscriber
            TempSubscriber subscriber = new TempSubscriber();

            // Subscribe the Subscriber to the Publisher
            publisher.subscribe(subscriber);

            System.out.println("   Publishing items...");
            // Publish items
            List.of("New York", "London", "Tokyo", "Paris", "Berlin").forEach(city -> {
                System.out.println("   [Publisher] Publishing: " + city);
                publisher.submit(city);
                try {
                    Thread.sleep(100); // Simulate some delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("   [Publisher] Completed publishing.");
        }
        // Publisher is closed automatically by try-with-resources
    }

    // A simple Subscriber implementation
    static class TempSubscriber implements Flow.Subscriber<String> {

        private Flow.Subscription subscription;
        private final String name = "TempSubscriber";

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            System.out.println("     [" + name + "] Subscribed");
            // Request the first item
            subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            System.out.println("     [" + name + "] Received: " + item);
            // Simulate processing
            // Request the next item
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            System.err.println("     [" + name + "] Error: " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("     [" + name + "] Completed");
        }
    }
}
