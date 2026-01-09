package com.modernjavainaction.part6_functional_programming.chapter20_oop_and_fp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Chapter 20: Blending OOP and FP: Comparing Java and Scala
 * 
 * Key concepts:
 * - Scala basics for Java developers
 * - How Scala influences Java evolution
 * - Functional features Java adopted from Scala
 * - Where Java is heading
 */
public class OopAndFp {

    public static void main(String[] args) {
        System.out.println("=== Chapter 20: Blending OOP and FP ===\n");

        // 1. Scala influence on Java
        System.out.println("1. Scala Features Adopted by Java:");
        demonstrateScalaInfluence();

        // 2. Type inference
        System.out.println("\n2. Type Inference:");
        demonstrateTypeInference();

        // 3. First-class functions
        System.out.println("\n3. First-Class Functions:");
        demonstrateFirstClassFunctions();

        // 4. Pattern matching evolution
        System.out.println("\n4. Pattern Matching Evolution:");
        demonstratePatternMatchingEvolution();

        // 5. Mixing OOP and FP
        System.out.println("\n5. Mixing OOP and FP in Practice:");
        demonstrateMixingParadigms();

        System.out.println("\n=== JAVA EVOLUTION TIMELINE ===");
        printJavaEvolution();

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 20.1: Convert an imperative class to functional style");
        System.out.println("Exercise 20.2: Compare Java and Scala implementations of a list operation");
        System.out.println("Exercise 20.3: Identify which FP features Java still lacks");
    }

    private static void demonstrateScalaInfluence() {
        System.out.println("  Features inspired by Scala:");
        System.out.println("    ✓ Lambdas (Java 8) - Scala had from day one");
        System.out.println("    ✓ Streams (Java 8) - Similar to Scala collections");
        System.out.println("    ✓ Optional (Java 8) - Scala's Option");
        System.out.println("    ✓ CompletableFuture (Java 8) - Scala's Future");
        System.out.println("    ✓ var keyword (Java 10) - Scala's val/var");
        System.out.println("    ✓ Records (Java 14+) - Scala's case classes");
        System.out.println("    ✓ Sealed classes (Java 17) - Scala's sealed traits");
        System.out.println("    ✓ Pattern matching (Java 17+) - Scala's match");
    }

    private static void demonstrateTypeInference() {
        // Java 7 - explicit types everywhere
        List<String> java7Style = Arrays.asList("a", "b", "c");

        // Java 8 - lambda parameter types inferred
        java7Style.stream()
                .map(s -> s.toUpperCase()) // Type of 's' is inferred
                .collect(Collectors.toList());

        // Java 10+ - var keyword: local variable type inference
        // var list = Arrays.asList("a", "b", "c");
        // var result =
        // list.stream().map(String::toUpperCase).collect(Collectors.toList());

        System.out.println("  Java 7: List<String> names = Arrays.asList(...)");
        System.out.println("  Java 8: Lambda params inferred: (s) -> s.length()");
        System.out.println("  Java 10+: var names = Arrays.asList(...)");

        System.out.println("\n  Scala comparison:");
        System.out.println("    val names = List(\"a\", \"b\", \"c\")  // Always inferred");
    }

    private static void demonstrateFirstClassFunctions() {
        // Functions as values
        Function<Integer, Integer> addOne = x -> x + 1;
        Function<Integer, Integer> multiplyTwo = x -> x * 2;

        // Composing functions
        Function<Integer, Integer> addThenMultiply = addOne.andThen(multiplyTwo);

        System.out.println("  Functions as values:");
        System.out.println("    addOne(5) = " + addOne.apply(5));
        System.out.println("    multiplyTwo(5) = " + multiplyTwo.apply(5));
        System.out.println("    addThenMultiply(5) = " + addThenMultiply.apply(5));

        // Passing functions as arguments
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> doubled = applyToAll(numbers, multiplyTwo);
        System.out.println("    applyToAll([1,2,3,4,5], multiplyTwo) = " + doubled);

        System.out.println("\n  Scala comparison:");
        System.out.println("    val addOne: Int => Int = _ + 1");
        System.out.println("    val nums = List(1, 2, 3).map(_ * 2)");
    }

    private static <T, R> List<R> applyToAll(List<T> list, Function<T, R> f) {
        return list.stream().map(f).collect(Collectors.toList());
    }

    private static void demonstratePatternMatchingEvolution() {
        System.out.println("  Evolution of switch/pattern matching:\n");

        System.out.println("  Java 7 and earlier:");
        System.out.println("    switch (x) { case 1: ... break; case 2: ... }");

        System.out.println("\n  Java 12+ (switch expressions):");
        System.out.println("    var result = switch (x) {");
        System.out.println("        case 1 -> \"one\";");
        System.out.println("        case 2 -> \"two\";");
        System.out.println("        default -> \"other\";");
        System.out.println("    };");

        System.out.println("\n  Java 17+ (pattern matching for instanceof):");
        System.out.println("    if (obj instanceof String s) {");
        System.out.println("        // s is already a String here!");
        System.out.println("    }");

        System.out.println("\n  Java 21+ (pattern matching for switch):");
        System.out.println("    return switch (shape) {");
        System.out.println("        case Circle c -> Math.PI * c.radius() * c.radius();");
        System.out.println("        case Rectangle r -> r.width() * r.height();");
        System.out.println("        case null -> 0;");
        System.out.println("    };");

        System.out.println("\n  Scala comparison:");
        System.out.println("    shape match {");
        System.out.println("      case Circle(r) => Math.PI * r * r");
        System.out.println("      case Rectangle(w, h) => w * h");
        System.out.println("    }");
    }

    private static void demonstrateMixingParadigms() {
        System.out.println("  Modern Java: Best of both worlds\n");

        // OOP: encapsulation, polymorphism
        // FP: immutability, pure functions, composition

        // Example: Order processing
        List<Order> orders = Arrays.asList(
                new Order("1", 100.0, Order.Status.PENDING),
                new Order("2", 250.0, Order.Status.SHIPPED),
                new Order("3", 75.0, Order.Status.PENDING),
                new Order("4", 300.0, Order.Status.SHIPPED));

        // Functional pipeline with OOP objects
        double pendingTotal = orders.stream()
                .filter(o -> o.getStatus() == Order.Status.PENDING)
                .mapToDouble(Order::getAmount)
                .sum();

        System.out.println("  Pending orders total: $" + pendingTotal);

        // Finding an order
        Optional<Order> largestShipped = orders.stream()
                .filter(o -> o.getStatus() == Order.Status.SHIPPED)
                .max((o1, o2) -> Double.compare(o1.getAmount(), o2.getAmount()));

        largestShipped.ifPresent(
                o -> System.out.println("  Largest shipped: Order " + o.getId() + " ($" + o.getAmount() + ")"));

        System.out.println("\n  Key insight: OOP for modeling, FP for processing!");
    }

    private static void printJavaEvolution() {
        System.out.println("  Java 8 (2014):  Lambdas, Streams, Optional, default methods");
        System.out.println("  Java 9 (2017):  Modules, Flow API, improved Optional");
        System.out.println("  Java 10 (2018): var keyword");
        System.out.println("  Java 11 (2018): var in lambdas, HTTP Client");
        System.out.println("  Java 12-13:     Switch expressions (preview)");
        System.out.println("  Java 14 (2020): Records (preview), Pattern matching instanceof (preview)");
        System.out.println("  Java 15 (2020): Sealed classes (preview), Text blocks");
        System.out.println("  Java 16 (2021): Records (final), Pattern matching instanceof (final)");
        System.out.println("  Java 17 (2021): Sealed classes (final) - LTS");
        System.out.println("  Java 21 (2023): Pattern matching for switch (final) - LTS");
    }
}

// Simple Order class for demo
class Order {
    enum Status {
        PENDING, SHIPPED, DELIVERED
    }

    private final String id;
    private final double amount;
    private final Status status;

    Order(String id, double amount, Status status) {
        this.id = id;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }
}
