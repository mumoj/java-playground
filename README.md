# Modern Java in Action - Playground 🚀

A hands-on Java 8+ playground based on the excellent book **"Modern Java in Action"** by Raoul-Gabriel Urma, Mario Fusco, and Alan Mycroft.

## 📁 Project Structure

```
java-playground/
├── pom.xml                              # Maven configuration
├── README.md                            # You are here!
├── PROGRESS.md                          # Track your progress
└── src/main/java/com/modernjavainaction/
    ├── common/model/                    # Shared domain objects
    │   ├── Apple.java
    │   ├── Dish.java
    │   ├── Trader.java
    │   └── Transaction.java
    │
    ├── part1_fundamentals/
    │   ├── chapter01_whats_happening/   # Java 8 overview
    │   ├── chapter02_behavior_parameterization/
    │   └── chapter03_lambda_expressions/
    │
    ├── part2_streams/
    │   ├── chapter04_introducing_streams/
    │   ├── chapter05_working_with_streams/
    │   ├── chapter06_collecting_data/
    │   └── chapter07_parallel_processing/
    │
    ├── part3_effective_programming/
    │   ├── chapter08_collection_api/
    │   ├── chapter09_refactoring_testing/
    │   └── chapter10_dsl_with_lambdas/
    │
    ├── part4_everyday_java/
    │   ├── chapter11_optional/
    │   ├── chapter12_date_time_api/
    │   ├── chapter13_default_methods/
    │   └── chapter14_module_system/
    │
    ├── part5_concurrency/
    │   ├── chapter15_completable_future_concepts/
    │   ├── chapter16_completable_future/
    │   └── chapter17_reactive_programming/
    │
    ├── part6_functional_programming/
    │   ├── chapter18_thinking_functionally/
    │   ├── chapter19_fp_techniques/
    │   └── chapter20_oop_and_fp/
    │
    └── sandbox/                         # Free-form playground
        └── Playground.java
```

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher (JDK 8+)
- Maven 3.6+

### Build and Run

```bash
# Compile the project
mvn clean compile

# Run a specific chapter's main class
mvn exec:java -Dexec.mainClass="com.modernjavainaction.part1_fundamentals.chapter03_lambda_expressions.LambdaExpressions"

# Run all tests
mvn test
```

### Running Individual Chapters

Each chapter has a runnable `main` method. You can run them directly from your IDE or use Maven:

```bash
# Chapter 1: What's Happening
mvn exec:java -Dexec.mainClass="com.modernjavainaction.part1_fundamentals.chapter01_whats_happening.WhatsHappening"

# Chapter 5: Working with Streams
mvn exec:java -Dexec.mainClass="com.modernjavainaction.part2_streams.chapter05_working_with_streams.WorkingWithStreams"

# Chapter 16: CompletableFuture
mvn exec:java -Dexec.mainClass="com.modernjavainaction.part5_concurrency.chapter16_completable_future.CompletableFutureExamples"
```

## 📚 Chapter Overview

| Part | Chapters | Key Topics |
|------|----------|------------|
| **1. Fundamentals** | 1-3 | Lambdas, Method References, Behavior Parameterization |
| **2. Streams** | 4-7 | Stream API, Collectors, Parallel Processing |
| **3. Effective Programming** | 8-10 | Collection API, Refactoring, DSLs |
| **4. Everyday Java** | 11-14 | Optional, Date/Time API, Default Methods, Modules |
| **5. Concurrency** | 15-17 | CompletableFuture, Reactive Programming |
| **6. Functional Programming** | 18-20 | FP Principles, Advanced Techniques, Java vs Scala |

## ✅ Exercises

Each chapter contains `// TODO:` comments marking exercises. Look for:

```java
// TODO: Exercise X.Y - Description
public static void exerciseMethod() {
    // Implement here
}
```

## 🎯 Suggested Learning Path

1. **Week 1-2**: Part 1 (Chapters 1-3) - Master lambdas and method references
2. **Week 3-4**: Part 2 (Chapters 4-7) - Become a Stream ninja
3. **Week 5**: Part 3 (Chapters 8-10) - Refactoring and DSLs
4. **Week 6**: Part 4 (Chapters 11-14) - Everyday Java 8+ features
5. **Week 7-8**: Part 5 (Chapters 15-17) - Async and reactive programming
6. **Week 9**: Part 6 (Chapters 18-20) - Functional programming deep dive

## 📝 Tips

- **Run the examples first** - See the output before modifying code
- **Complete the TODO exercises** - Active practice beats passive reading
- **Use the sandbox** - `sandbox.Playground` is your experimentation zone
- **Break things!** - You learn more from errors than success

## 🔗 Resources

- [Modern Java in Action - Manning](https://www.manning.com/books/modern-java-in-action)
- [Java 8 API Documentation](https://docs.oracle.com/javase/8/docs/api/)
- [Stream API Guide](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

## 📊 Track Your Progress

Use the `PROGRESS.md` file to track which chapters and exercises you've completed!

---

Happy coding! 🎉
