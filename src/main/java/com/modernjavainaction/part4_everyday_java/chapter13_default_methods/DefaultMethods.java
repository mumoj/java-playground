package com.modernjavainaction.part4_everyday_java.chapter13_default_methods;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Chapter 13: Default Methods
 * 
 * Key concepts:
 * - Default methods in interfaces
 * - Static methods in interfaces
 * - Resolution rules for multiple inheritance
 * - Abstract classes vs interfaces with default methods
 */
public class DefaultMethods {

    public static void main(String[] args) {
        System.out.println("=== Chapter 13: Default Methods ===\n");

        // 1. Basic default method
        System.out.println("1. Default Methods:");
        Resizable rect = new Rectangle(10, 20);
        System.out.println("  Original size: " + rect.getWidth() + "x" + rect.getHeight());

        // Using default method
        rect.setRelativeSize(2, 2); // Scale by 2x
        System.out.println("  After setRelativeSize(2, 2): " + rect.getWidth() + "x" + rect.getHeight());

        // 2. Static methods in interfaces
        System.out.println("\n2. Static Methods in Interfaces:");
        // Comparator.comparing is a static method
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        names.sort(Comparator.comparing(String::length));
        System.out.println("  Sorted by length: " + names);

        // Custom static method example
        System.out.println("  Is 'hello' valid? " + Validator.isValid("hello"));
        System.out.println("  Is '' valid? " + Validator.isValid(""));

        // 3. Multiple inheritance resolution
        System.out.println("\n3. Multiple Inheritance Resolution:");
        MultiInheritanceDemo demo = new MultiInheritanceDemo();
        demo.hello(); // Which interface's default method is called?

        // 4. Diamond problem
        System.out.println("\n4. Diamond Problem:");
        DiamondDemo diamond = new DiamondDemo();
        diamond.hello();

        // 5. Real-world example: Comparator
        System.out.println("\n5. Comparator Default Methods:");
        List<Apple> apples = Arrays.asList(
                new Apple("Green", 150),
                new Apple("Red", 120),
                new Apple("Green", 100));

        // Using default methods: thenComparing, reversed
        Comparator<Apple> byColor = Comparator.comparing(Apple::getColor);
        Comparator<Apple> byWeight = Comparator.comparing(Apple::getWeight);

        // Chain comparators with thenComparing
        apples.sort(byColor.thenComparing(byWeight));
        System.out.println("  By color then weight: " + apples);

        // Reversed
        apples.sort(byWeight.reversed());
        System.out.println("  By weight (reversed): " + apples);

        // Null handling
        List<String> namesWithNull = Arrays.asList("Charlie", null, "Alice", null, "Bob");
        namesWithNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println("  With nulls first: " + namesWithNull);

        System.out.println("\n=== Resolution Rules (from the book) ===");
        System.out.println("  1. Classes always win over interfaces");
        System.out.println("  2. Sub-interfaces win over super-interfaces");
        System.out.println("  3. If ambiguous, must explicitly override");

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 13.1: Create an interface with multiple default methods");
        System.out.println("Exercise 13.2: Demonstrate all three resolution rules");
        System.out.println("Exercise 13.3: Refactor utility class to use static methods in interface");
    }
}

// Simple Apple class for this chapter
class Apple {
    private String color;
    private int weight;

    public Apple(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return color + "(" + weight + "g)";
    }
}

// Interface with default method
interface Resizable {
    int getWidth();

    int getHeight();

    void setWidth(int width);

    void setHeight(int height);

    // Default method - can be overridden
    default void setRelativeSize(int widthFactor, int heightFactor) {
        setWidth(getWidth() * widthFactor);
        setHeight(getHeight() * heightFactor);
    }
}

class Rectangle implements Resizable {
    private int width;
    private int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }
}

// Interface with static method
interface Validator {
    static boolean isValid(String s) {
        return s != null && !s.isEmpty();
    }
}

// Multiple inheritance scenario
interface A {
    default void hello() {
        System.out.println("  Hello from A");
    }
}

interface B extends A {
    @Override
    default void hello() {
        System.out.println("  Hello from B (extends A)");
    }
}

// Resolution Rule 2: B wins because it's more specific (extends A)
class MultiInheritanceDemo implements A, B {
    // Uses B's hello() because B is more specific than A
}

// Diamond problem
interface C extends A {
    // Inherits hello() from A
}

interface D extends A {
    // Inherits hello() from A
}

// Both C and D inherit from A, but there's no conflict because
// they inherit the same method
class DiamondDemo implements C, D {
    // Uses A's hello() - no ambiguity because C and D don't override it
}

// When there IS ambiguity, you must resolve it:
interface E {
    default void greet() {
        System.out.println("  Greet from E");
    }
}

interface F {
    default void greet() {
        System.out.println("  Greet from F");
    }
}

class AmbiguousDemo implements E, F {
    // Must override and explicitly choose
    @Override
    public void greet() {
        E.super.greet(); // Choose E's implementation
        // or F.super.greet();
    }
}
