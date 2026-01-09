package com.modernjavainaction.sandbox;
import com.modernjavainaction.common.model.Dish;

/**
 * Sandbox - Your Free-Form Playground
 * 
 * Use this package for experimenting without any structure.
 * Create your own classes, try out concepts, break things!
 * 
 * Suggested experiments:
 * 1. Try all the stream operations from Chapter 5
 * 2. Build your own collectors
 * 3. Experiment with CompletableFuture chains
 * 4. Create your own functional interfaces
 * 5. Practice the design patterns from Chapter 9
 */
public class Playground {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     Welcome to the Modern Java in Action Playground!       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("This is your sandbox - experiment freely here!");
        System.out.println();
        System.out.println("Quick Reference:");
        System.out.println("  • Lambda syntax:     (params) -> expression");
        System.out.println("  • Method reference:  Class::method");
        System.out.println("  • Stream pipeline:   .stream().filter().map().collect()");
        System.out.println("  • Optional chain:    .map().flatMap().orElse()");
        System.out.println("  • Async:            CompletableFuture.supplyAsync()");
        System.out.println();
        System.out.println("Start experimenting below!");
        System.out.println("────────────────────────────────────────────────────────────");

        System.out.println(Dish.getMenu());
        // Try things out!

    }
}
