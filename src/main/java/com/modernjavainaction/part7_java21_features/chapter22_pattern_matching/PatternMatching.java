package com.modernjavainaction.part7_java21_features.chapter22_pattern_matching;

/**
 * Chapter 22: Pattern Matching Enhancements
 * 
 * Java 21 brings powerful pattern matching capabilities:
 * - Record patterns for deconstructing records
 * - Pattern matching for switch (finalized)
 * - Nested patterns for complex data structures
 */
public class PatternMatching {

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching in Java 21 ===\n");

        // 1. Pattern matching for switch
        switchPatternMatching();

        // 2. Record patterns
        recordPatterns();

        // 3. Nested record patterns
        nestedPatterns();

        // 4. Guarded patterns
        guardedPatterns();
    }

    // ==================== Records for demos ====================

    record Point(int x, int y) {
    }

    record Circle(Point center, int radius) {
    }

    record Rectangle(Point topLeft, Point bottomRight) {
    }

    sealed interface Shape permits CircleShape, RectangleShape, Triangle {
    }

    record CircleShape(Circle circle) implements Shape {
    }

    record RectangleShape(Rectangle rectangle) implements Shape {
    }

    record Triangle(Point a, Point b, Point c) implements Shape {
    }

    record Person(String name, int age) {
    }

    record Employee(Person person, String department) {
    }

    // ==================== Demos ====================

    /**
     * Pattern matching for switch - now finalized in Java 21
     */
    static void switchPatternMatching() {
        System.out.println("1. Pattern Matching for Switch");
        System.out.println("-".repeat(40));

        Object[] values = { "Hello", 42, 3.14, new Point(5, 10), null };

        for (Object obj : values) {
            String result = switch (obj) {
                case null -> "It's null!";
                case String s -> "String of length " + s.length();
                case Integer i -> "Integer: " + i * 2;
                case Double d -> "Double rounded: " + Math.round(d);
                case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
                default -> "Unknown type: " + obj.getClass().getSimpleName();
            };
            System.out.println(obj + " -> " + result);
        }
        System.out.println();
    }

    /**
     * Record patterns allow deconstructing records directly in patterns
     */
    static void recordPatterns() {
        System.out.println("2. Record Patterns");
        System.out.println("-".repeat(40));

        Point point = new Point(10, 20);

        // Old way with instanceof
        if (point instanceof Point p) {
            System.out.println("Old way: x=" + p.x() + ", y=" + p.y());
        }

        // New way with record pattern - deconstruction!
        if (point instanceof Point(int x, int y)) {
            System.out.println("New way: x=" + x + ", y=" + y);
        }

        // Works great with switch
        Person person = new Person("Alice", 30);
        String greeting = switch (person) {
            case Person(String name, int age) when age < 18 -> "Hey " + name + "!";
            case Person(String name, int age) when age < 65 -> "Hello " + name + "!";
            case Person(String name, int age) -> "Good day, " + name + "!";
        };
        System.out.println(greeting);
        System.out.println();
    }

    /**
     * Nested patterns for complex hierarchical data
     */
    static void nestedPatterns() {
        System.out.println("3. Nested Record Patterns");
        System.out.println("-".repeat(40));

        Shape[] shapes = {
                new CircleShape(new Circle(new Point(0, 0), 5)),
                new RectangleShape(new Rectangle(new Point(0, 0), new Point(10, 10))),
                new Triangle(new Point(0, 0), new Point(5, 10), new Point(10, 0))
        };

        for (Shape shape : shapes) {
            String description = switch (shape) {
                // Nested deconstruction - goes multiple levels deep!
                case CircleShape(Circle(Point(int x, int y), int r)) ->
                    "Circle centered at (" + x + "," + y + ") with radius " + r;

                case RectangleShape(Rectangle(Point(int x1, int y1), Point(int x2, int y2))) ->
                    "Rectangle from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")";

                case Triangle(Point(int ax, int ay), Point _, Point _) ->
                    "Triangle starting at (" + ax + "," + ay + ")";
            };
            System.out.println(description);
        }
        System.out.println();
    }

    /**
     * Guarded patterns with 'when' clause
     */
    static void guardedPatterns() {
        System.out.println("4. Guarded Patterns (when clause)");
        System.out.println("-".repeat(40));

        Employee[] employees = {
                new Employee(new Person("Alice", 25), "Engineering"),
                new Employee(new Person("Bob", 45), "Management"),
                new Employee(new Person("Charlie", 35), "Engineering"),
                new Employee(new Person("Diana", 55), "HR")
        };

        for (Employee emp : employees) {
            String status = switch (emp) {
                case Employee(Person(String name, int age), String dept) when dept.equals("Engineering") && age < 30 ->
                    name + " is a junior engineer";

                case Employee(Person(String name, int age), String dept) when dept.equals("Engineering") ->
                    name + " is a senior engineer";

                case Employee(Person(String name, int age2), String dept) when dept.equals("Management") ->
                    name + " is in management";

                case Employee(Person(String name, int age3), String dept) ->
                    name + " works in " + dept;
            };
            System.out.println(status);
        }
    }
}
