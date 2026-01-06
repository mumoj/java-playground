package com.modernjavainaction.part3_effective_programming.chapter09_refactoring_testing;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Chapter 9: Refactoring, Testing, and Debugging
 * 
 * Key concepts:
 * - Refactoring for readability (anonymous classes -> lambdas)
 * - From lambda to method reference
 * - Design patterns with lambdas (Strategy, Template Method, Observer, Chain of
 * Responsibility, Factory)
 * - Testing lambdas
 * - Debugging streams
 */
public class RefactoringTestingDebugging {

    public static void main(String[] args) {
        System.out.println("=== Chapter 9: Refactoring, Testing, and Debugging ===\n");

        // 1. Anonymous class to lambda
        System.out.println("1. Anonymous class -> Lambda:");
        demonstrateAnonymousToLambda();

        // 2. Lambda to method reference
        System.out.println("\n2. Lambda -> Method reference:");
        demonstrateLambdaToMethodRef();

        // 3. Design patterns with lambdas
        System.out.println("\n3. Design Patterns with Lambdas:");
        demonstrateDesignPatterns();

        // 4. Debugging with peek
        System.out.println("\n4. Debugging Streams with peek:");
        demonstrateDebugging();

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 9.1: Refactor the old-style Runnable to lambda");
        System.out.println("Exercise 9.2: Implement Template Method pattern with lambdas");
        System.out.println("Exercise 9.3: Add logging to a stream pipeline using peek");
    }

    private static void demonstrateAnonymousToLambda() {
        // Before: Anonymous class
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("  Hello from anonymous class!");
            }
        };
        r1.run();

        // After: Lambda
        Runnable r2 = () -> System.out.println("  Hello from lambda!");
        r2.run();
    }

    private static void demonstrateLambdaToMethodRef() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // Lambda
        names.forEach(s -> System.out.println("  " + s));

        // Method reference (if you have a helper method)
        names.stream()
                .map(String::toUpperCase) // Instead of s -> s.toUpperCase()
                .forEach(s -> System.out.println("  " + s));
    }

    private static void demonstrateDesignPatterns() {
        // Strategy Pattern
        System.out.println("\n  Strategy Pattern:");
        Validator numericValidator = new Validator(s -> s.matches("\\d+"));
        System.out.println("    Is '123' numeric? " + numericValidator.validate("123"));
        System.out.println("    Is 'abc' numeric? " + numericValidator.validate("abc"));

        Validator lowerCaseValidator = new Validator(s -> s.matches("[a-z]+"));
        System.out.println("    Is 'abc' lowercase? " + lowerCaseValidator.validate("abc"));

        // Template Method Pattern
        System.out.println("\n  Template Method Pattern:");
        OnlineBanking banking = new OnlineBanking();
        banking.processCustomer(1234, customer -> System.out.println("    Processing: " + customer));

        // Observer Pattern
        System.out.println("\n  Observer Pattern:");
        Feed feed = new Feed();
        feed.registerObserver(tweet -> {
            if (tweet.contains("money")) {
                System.out.println("    Breaking news about money! " + tweet);
            }
        });
        feed.registerObserver(tweet -> {
            if (tweet.contains("queen")) {
                System.out.println("    Yet another news about the queen! " + tweet);
            }
        });
        feed.notifyObservers("The queen said money is important!");

        // Chain of Responsibility
        System.out.println("\n  Chain of Responsibility:");
        Function<String, String> addHeader = text -> "From: example@email.com\n" + text;
        Function<String, String> spellCheck = text -> text.replace("labda", "lambda");
        Function<String, String> addFooter = text -> text + "\nBest regards";

        Function<String, String> pipeline = addHeader.andThen(spellCheck).andThen(addFooter);

        String result = pipeline.apply("I love labda expressions!");
        System.out.println("    Processed email:\n" + result);

        // Factory Pattern
        System.out.println("\n  Factory Pattern:");
        Product loan = ProductFactory.createProduct("loan");
        Product stock = ProductFactory.createProduct("stock");
        System.out.println("    Created: " + loan + ", " + stock);
    }

    private static void demonstrateDebugging() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Use peek to see intermediate values
        int sum = numbers.stream()
                .peek(n -> System.out.println("    Original: " + n))
                .map(n -> n * 2)
                .peek(n -> System.out.println("    Doubled: " + n))
                .filter(n -> n > 5)
                .peek(n -> System.out.println("    Filtered: " + n))
                .reduce(0, Integer::sum);

        System.out.println("  Final sum: " + sum);
    }
}

// Strategy Pattern classes
class Validator {
    private final Predicate<String> validationStrategy;

    public Validator(Predicate<String> validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    public boolean validate(String s) {
        return validationStrategy.test(s);
    }
}

// Template Method Pattern
class OnlineBanking {
    public void processCustomer(int id, Consumer<String> makeCustomerHappy) {
        String customer = "Customer-" + id;
        makeCustomerHappy.accept(customer);
    }
}

// Observer Pattern
interface Observer {
    void notify(String tweet);
}

class Feed {
    private final List<Observer> observers = new java.util.ArrayList<>();

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void notifyObservers(String tweet) {
        observers.forEach(o -> o.notify(tweet));
    }
}

// Factory Pattern
interface Product {
}

class Loan implements Product {
    @Override
    public String toString() {
        return "Loan";
    }
}

class Stock implements Product {
    @Override
    public String toString() {
        return "Stock";
    }
}

class Bond implements Product {
    @Override
    public String toString() {
        return "Bond";
    }
}

class ProductFactory {
    private static final java.util.Map<String, java.util.function.Supplier<Product>> map = new java.util.HashMap<>();

    static {
        map.put("loan", Loan::new);
        map.put("stock", Stock::new);
        map.put("bond", Bond::new);
    }

    public static Product createProduct(String name) {
        java.util.function.Supplier<Product> p = map.get(name.toLowerCase());
        if (p != null)
            return p.get();
        throw new IllegalArgumentException("Unknown product: " + name);
    }
}
