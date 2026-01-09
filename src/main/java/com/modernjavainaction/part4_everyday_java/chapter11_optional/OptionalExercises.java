package com.modernjavainaction.part4_everyday_java.chapter11_optional;

import java.util.Optional;

/**
 * Chapter 11: Using Optional as a Better Alternative to Null
 * 
 * Key concepts:
 * - Problems with null
 * - Optional class introduction
 * - Patterns for applying Optional
 * - Chaining and flatMap with Optional
 * - Combining Optionals
 * - Default values and actions
 */
public class OptionalExercises {

    public static void main(String[] args) {
        System.out.println("=== Chapter 11: Optional ===\n");

        // 1. Creating Optionals
        System.out.println("1. Creating Optionals:");
        Optional<String> empty = Optional.empty();
        Optional<String> name = Optional.of("John");
        Optional<String> nullableName = Optional.ofNullable(null);

        System.out.println("  empty.isPresent(): " + empty.isPresent());
        System.out.println("  name.isPresent(): " + name.isPresent());
        System.out.println("  nullableName.isPresent(): " + nullableName.isPresent());

        // 2. Extracting values
        System.out.println("\n2. Extracting values safely:");

        // get() - throws NoSuchElementException if empty!
        System.out.println("  name.get(): " + name.get());

        // orElse - provide default
        System.out.println("  empty.orElse(\"Unknown\"): " + empty.orElse("Unknown"));

        // orElseGet - lazy default (computed only if needed)
        System.out.println("  empty.orElseGet(() -> computeDefault()): " +
                empty.orElseGet(() -> "Computed Default"));

        // orElseThrow - throw custom exception if empty
        try {
            empty.orElseThrow(() -> new IllegalStateException("Value not found!"));
        } catch (IllegalStateException e) {
            System.out.println("  empty.orElseThrow(): " + e.getMessage());
        }

        // 3. Transforming Optionals with map
        System.out.println("\n3. Transforming with map:");
        Optional<Integer> nameLength = name.map(String::length);
        System.out.println("  name.map(String::length): " + nameLength.orElse(0));

        // 4. Chaining with flatMap
        System.out.println("\n4. Chaining with flatMap:");
        Person person = new Person("John",
                new Car("Tesla", new Insurance("AllState")));

        // Old way - nested null checks (dangerous!)
        String insuranceNameOld = getInsuranceNameUnsafe(person);
        System.out.println("  Old way (unsafe): " + insuranceNameOld);

        // Safe way with Optional
        String insuranceName = getInsuranceNameSafe(person);
        System.out.println("  Safe way with Optional: " + insuranceName);

        // Person without car
        Person personNoCar = new Person("Jane", null);
        System.out.println("  Person without car: " + getInsuranceNameSafe(personNoCar));

        // 5. Filtering with Optional
        System.out.println("\n5. Filtering:");
        Optional<Insurance> insuranceOpt = Optional.of(new Insurance("AllState"));
        insuranceOpt.filter(ins -> ins.getName().startsWith("All"))
                .ifPresent(ins -> System.out.println("  Found matching insurance: " + ins.getName()));

        // 6. ifPresent and ifPresentOrElse
        System.out.println("\n6. ifPresent and actions:");
        name.ifPresent(n -> System.out.println("  Found name: " + n));

        // Java 9+: ifPresentOrElse
        // empty.ifPresentOrElse(
        // n -> System.out.println(" Found: " + n),
        // () -> System.out.println(" No value present")
        // );

        // 7. Combining two Optionals
        System.out.println("\n7. Combining Optionals:");
        Optional<Person> optPerson = Optional.of(person);
        Optional<Car> optCar = Optional.of(new Car("BMW", null));

        Optional<String> combined = optPerson.flatMap(p -> optCar.map(c -> p.getName() + " drives " + c.getBrand()));
        combined.ifPresent(s -> System.out.println("  Combined: " + s));

        // 8. Optional with streams
        System.out.println("\n8. Optional with Streams:");
        // Java 9+ has Optional.stream()
        // For Java 8, use: optional.map(Stream::of).orElseGet(Stream::empty)

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 11.1: Rewrite null-checking code to use Optional");
        System.out.println("Exercise 11.2: Implement getCarInsuranceName using Optional chain");
        System.out.println("Exercise 11.3: Combine two Optionals with a BiFunction");
        System.out.println("Exercise 11.4: Find cheapest insurance using Optional");
    }

    // Unsafe - uses null checks
    private static String getInsuranceNameUnsafe(Person person) {
        if (person != null) {
            Car car = person.getCar();
            if (car != null) {
                Insurance insurance = car.getInsurance();
                if (insurance != null) {
                    return insurance.getName();
                }
            }
        }
        return "Unknown";
    }

    // Safe - uses Optional
    private static String getInsuranceNameSafe(Person person) {
        return Optional.ofNullable(person)
                .map(Person::getCar)
                .map(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }
}

// Domain classes for Optional examples
class Person {
    private String name;
    private Car car;

    public Person(String name, Car car) {
        this.name = name;
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public Car getCar() {
        return car;
    }

    // Better design: return Optional<Car>
    public Optional<Car> getCarOptional() {
        return Optional.ofNullable(car);
    }
}

class Car {
    private String brand;
    private Insurance insurance;

    public Car(String brand, Insurance insurance) {
        this.brand = brand;
        this.insurance = insurance;
    }

    public String getBrand() {
        return brand;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    // Better design: return Optional<Insurance>
    public Optional<Insurance> getInsuranceOptional() {
        return Optional.ofNullable(insurance);
    }
}

class Insurance {
    private String name;

    public Insurance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
