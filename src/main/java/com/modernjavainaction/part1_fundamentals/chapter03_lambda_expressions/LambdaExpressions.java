package com.modernjavainaction.part1_fundamentals.chapter03_lambda_expressions;

import com.modernjavainaction.common.model.Apple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

import static com.modernjavainaction.common.model.Apple.Color.*;

/**
 * Chapter 3: Lambda Expressions
 * 
 * Key concepts:
 * - Lambda syntax
 * - Functional interfaces
 * - Common functional interfaces (Predicate, Consumer, Function, Supplier)
 * - Type inference
 * - Method references
 * - Composing lambdas
 */
public class LambdaExpressions {

    public static void main(String[] args) {
        System.out.println("=== Chapter 3: Lambda Expressions ===\n");

        // 1. Lambda syntax variations
        System.out.println("1. Lambda Syntax Examples:");
        demonstrateLambdaSyntax();

        // 2. Functional interfaces
        System.out.println("\n2. Built-in Functional Interfaces:");
        demonstrateFunctionalInterfaces();

        // 3. Method references
        System.out.println("\n3. Method References:");
        demonstrateMethodReferences();

        // 4. Composing lambdas
        System.out.println("\n4. Composing Lambdas:");
        demonstrateComposition();

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 3.1: Create a Comparator for Apple using lambda");
        System.out.println("Exercise 3.2: Implement the execute around pattern");
        System.out.println("Exercise 3.3: Use Function composition to build a processing pipeline");
        System.out.println("Exercise 3.4: Convert all anonymous classes to lambdas and method references");
    }

    private static void demonstrateLambdaSyntax() {
        // No parameters
        Runnable r1 = () -> System.out.println("Hello World!");

        // Single parameter (parentheses optional)
        Consumer<String> c1 = s -> System.out.println(s);
        Consumer<String> c2 = (String s) -> System.out.println(s);

        // Multiple parameters
        Comparator<Apple> comp = (Apple a1, Apple a2) -> Integer.compare(a1.getWeight(), a2.getWeight());

        // Type inference - compiler infers types
        Comparator<Apple> compInferred = (a1, a2) -> Integer.compare(a1.getWeight(), a2.getWeight());

        // Block body with explicit return
        Comparator<Apple> compBlock = (a1, a2) -> {
            System.out.println("Comparing apples...");
            return Integer.compare(a1.getWeight(), a2.getWeight());
        };

        r1.run();
        c1.accept("Lambda with single parameter");
    }

    private static void demonstrateFunctionalInterfaces() {
        // Predicate<T> - T -> boolean
        Predicate<Apple> heavyApple = apple -> apple.getWeight() > 150;
        System.out.println("  Predicate: Is 200g apple heavy? " + heavyApple.test(new Apple(200, RED)));

        // Consumer<T> - T -> void
        Consumer<Apple> printApple = apple -> System.out.println("  Consumer: " + apple);
        printApple.accept(new Apple(120, GREEN));

        // Function<T, R> - T -> R
        Function<Apple, Integer> getWeight = Apple::getWeight;
        System.out.println("  Function: Apple weight = " + getWeight.apply(new Apple(180, RED)));

        // Supplier<T> - () -> T
        Supplier<Apple> appleFactory = () -> new Apple(100, GREEN);
        System.out.println("  Supplier: " + appleFactory.get());

        // UnaryOperator<T> - T -> T (special case of Function)
        UnaryOperator<Integer> doubler = x -> x * 2;
        System.out.println("  UnaryOperator: 5 doubled = " + doubler.apply(5));

        // BinaryOperator<T> - (T, T) -> T
        BinaryOperator<Integer> adder = (a, b) -> a + b;
        System.out.println("  BinaryOperator: 3 + 4 = " + adder.apply(3, 4));

        // BiPredicate, BiConsumer, BiFunction
        BiPredicate<Apple, Integer> isHeavier = (apple, weight) -> apple.getWeight() > weight;
        System.out.println("  BiPredicate: Is 200g apple heavier than 150? " +
                isHeavier.test(new Apple(200, RED), 150));
    }

    private static void demonstrateMethodReferences() {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(100, GREEN));
        inventory.add(new Apple(180, RED));
        inventory.add(new Apple(120, GREEN));

        // Static method reference: ClassName::staticMethod
        // Lambda: (args) -> ClassName.staticMethod(args)
        Function<String, Integer> stringToInt = Integer::parseInt;
        System.out.println("  Static method ref: " + stringToInt.apply("123"));

        // Instance method on arbitrary object: ClassName::instanceMethod
        // Lambda: (obj, args) -> obj.instanceMethod(args)
        Function<Apple, Integer> getWeight = Apple::getWeight;
        System.out.println("  Instance method ref (arbitrary): " + getWeight.apply(new Apple(150, RED)));

        // Instance method on specific object: obj::instanceMethod
        // Lambda: (args) -> obj.instanceMethod(args)
        Apple myApple = new Apple(200, GREEN);
        Supplier<Integer> getMyAppleWeight = myApple::getWeight;
        System.out.println("  Instance method ref (specific): " + getMyAppleWeight.get());

        // Constructor reference: ClassName::new
        // For no-arg constructor
        Supplier<List<Apple>> listSupplier = ArrayList::new;
        System.out.println("  Constructor ref (no-arg): " + listSupplier.get());

        // Sorting with method reference
        inventory.sort(Comparator.comparing(Apple::getWeight));
        System.out.println("  Sorted by weight: " + inventory);
    }

    private static void demonstrateComposition() {
        // Comparator composition
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(100, GREEN));
        inventory.add(new Apple(180, RED));
        inventory.add(new Apple(100, RED));

        // Sort by weight, then by color for equal weights
        Comparator<Apple> byWeight = Comparator.comparing(Apple::getWeight);
        Comparator<Apple> byColor = Comparator.comparing(Apple::getColor);

        inventory.sort(byWeight.thenComparing(byColor));
        System.out.println("  Sorted by weight then color: " + inventory);

        // Reverse order
        inventory.sort(byWeight.reversed());
        System.out.println("  Sorted by weight (reversed): " + inventory);

        // Predicate composition
        Predicate<Apple> isGreen = apple -> GREEN.equals(apple.getColor());
        Predicate<Apple> isHeavy = apple -> apple.getWeight() > 150;

        Predicate<Apple> isGreenAndHeavy = isGreen.and(isHeavy);
        Predicate<Apple> isGreenOrHeavy = isGreen.or(isHeavy);
        Predicate<Apple> isNotGreen = isGreen.negate();

        Apple testApple = new Apple(180, GREEN);
        System.out.println("  Test apple: " + testApple);
        System.out.println("  isGreenAndHeavy: " + isGreenAndHeavy.test(testApple));
        System.out.println("  isGreenOrHeavy: " + isGreenOrHeavy.test(testApple));
        System.out.println("  isNotGreen: " + isNotGreen.test(testApple));

        // Function composition
        Function<Integer, Integer> addOne = x -> x + 1;
        Function<Integer, Integer> multiplyByTwo = x -> x * 2;

        // g.andThen(f) means: first apply g, then f => f(g(x))
        Function<Integer, Integer> addThenMultiply = addOne.andThen(multiplyByTwo);
        System.out.println("  (5 + 1) * 2 = " + addThenMultiply.apply(5));

        // g.compose(f) means: first apply f, then g => g(f(x))
        Function<Integer, Integer> multiplyThenAdd = addOne.compose(multiplyByTwo);
        System.out.println("  (5 * 2) + 1 = " + multiplyThenAdd.apply(5));
    }
}
