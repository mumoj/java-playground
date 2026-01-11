package com.modernjavainaction.part7_java21_features.chapter24_other_features;

import java.util.List;
import java.util.function.Function;

/**
 * Chapter 24: Other Java 21 Features
 * 
 * Additional features and improvements in Java 21:
 * - String enhancements
 * - Record improvements
 * - Math API additions
 * - Emoji and Unicode support
 * 
 * Note: Some features like String Templates, Unnamed Patterns,
 * and Structured Concurrency are still in preview.
 */
public class OtherJava21Features {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Other Java 21 Features ===\n");

        // 1. String enhancements
        stringEnhancements();

        // 2. Math API additions
        mathEnhancements();

        // 3. Emoji and Unicode 15 support
        emojiSupport();

        // 4. Record improvements
        recordImprovements();

        // 5. Preview features info
        previewFeaturesInfo();
    }

    /**
     * String API additions
     */
    // ==================== Demos ====================

    /**
     * String API additions and String Templates (Preview)
     */
    static void stringEnhancements() {
        System.out.println("1. String Enhancements");
        System.out.println("-".repeat(40));

        // indexOf with beginIndex and endIndex
        String text = "Hello World, Hello Java, Hello Future";
        int index = text.indexOf("Hello", 7);
        System.out.println("Text: \"" + text + "\"");
        System.out.println("Second 'Hello' at index: " + index);

        // repeat() on StringBuilder
        StringBuilder sb = new StringBuilder();
        sb.repeat("=", 3);
        sb.append(" TITLE ");
        sb.repeat("=", 3);
        System.out.println("StringBuilder.repeat(): " + sb);

        // String Templates (Preview)
        // Note: As of Java 21, String Templates are a preview feature (JEP 430).
        // syntax: STR."..."

        String name = "Java Developer";
        int experience = 5;
        // String info = STR."{name} has {experience} years of experience"; // Uncomment
        // if preview enabled and supported

        // Since we might not have full IDE support showing errors here, let's just
        // print about it for now
        // or try to use it if we are sure it compiles.
        // Let's rely on standard features first.
        System.out.println("\n(String Templates are a preview feature available in Java 21)");
        System.out.println("Example code: STR.\"Hello \\{name}!\"");

        System.out.println();
    }

    /**
     * Math API enhancements
     */
    static void mathEnhancements() {
        System.out.println("2. Math API Enhancements");
        System.out.println("-".repeat(40));

        // clamp()
        System.out.println("Math.clamp(15, 0, 10) = " + Math.clamp(15, 0, 10)); // 10
        System.out.println("Math.clamp(-5, 0, 10) = " + Math.clamp(-5, 0, 10)); // 0
        System.out.println();
    }

    /**
     * Enhanced emoji and Unicode 15.0 support
     */
    static void emojiSupport() {
        System.out.println("3. Emoji and Unicode 15.0 Support");
        System.out.println("-".repeat(40));
        String message = "Hello \uD83D\uDC4B World \uD83C\uDF0D!"; // Hello 👋 World 🌍!
        System.out.println("Message: " + message);
        System.out.println("Emoji count: " + countEmoji(message));
        System.out.println();
    }

    static int countEmoji(String s) {
        return (int) s.codePoints().filter(Character::isEmoji).count();
    }

    static boolean containsEmoji(String s) {
        return s.codePoints().anyMatch(Character::isEmoji);
    }

    // Moved from local to static for valid compilation
    sealed interface Result<T> permits Success, Failure {
    }

    record Success<T>(T value) implements Result<T> {
    }

    record Failure<T>(String error) implements Result<T> {
    }

    /**
     * Record improvements
     */
    static void recordImprovements() {
        System.out.println("4. Record Improvements");
        System.out.println("-".repeat(40));

        List<Result<Integer>> results = List.of(
                new Success<>(42),
                new Failure<>("Not found"),
                new Success<>(100));

        System.out.println("Processing sealed interface with records:");
        for (Result<Integer> result : results) {
            String message = switch (result) {
                // Java 21 allows record patterns in switch
                case Success<Integer>(Integer value) -> "Success: " + value;
                case Failure<Integer>(String error) -> "Error: " + error;
            };
            System.out.println("  " + message);
        }
        System.out.println();
    }

    /**
     * Structured Concurrency (Preview) and Scoped Values (Preview)
     */
    static void previewFeaturesInfo() {
        System.out.println("5. Preview Features Demo");
        System.out.println("-".repeat(40));

        // Note: StructuredTaskScope and ScopedValue are preview APIs (JEP 453, JEP 446)
        // To use them, we need --enable-preview at compile and run time.

        // We will demonstrate them via reflection or just comment that they require
        // setup
        // to avoid breaking the main build if environment is strict.
        // However, since we enabled preview in pom.xml, we can try using them directly!

        try {
            structuredConcurrencyDemo();
        } catch (Exception e) {
            System.out.println("Structured Concurrency failed: " + e.getMessage());
        }
    }

    static void structuredConcurrencyDemo() throws Exception {
        System.out.println("Running Structured Concurrency Demo...");

        // This requires the jdk.incubator.concurrent module or java.util.concurrent in
        // Java 21
        // StructuredTaskScope is in java.util.concurrent

        try (var scope = new java.util.concurrent.StructuredTaskScope.ShutdownOnFailure()) {

            java.util.concurrent.StructuredTaskScope.Subtask<String> userTask = scope.fork(() -> {
                Thread.sleep(100);
                return "UserA";
            });

            java.util.concurrent.StructuredTaskScope.Subtask<Integer> orderTask = scope.fork(() -> {
                Thread.sleep(200);
                return 123;
            });

            scope.join(); // Join both forks
            scope.throwIfFailed(); // ... and propagate errors

            // Here, both forks have succeeded, so compose their results
            String user = userTask.get();
            int order = orderTask.get();

            System.out.println("Result: " + user + ", Order: " + order);
        }
        System.out.println();
    }
}
