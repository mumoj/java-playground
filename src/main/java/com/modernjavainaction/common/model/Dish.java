package com.modernjavainaction.common.model;

import java.util.Arrays;
import java.util.List;

/**
 * Dish class used for Stream exercises throughout the book.
 * Featured prominently in Chapters 4-6 for menu filtering, grouping, and collecting.
 */
public class Dish {

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public enum Type {
        MEAT, FISH, OTHER
    }

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Sample menu used throughout the book's examples.
     */
    public static List<Dish> getMenu() {
        return Arrays.asList(
            new Dish("pork", false, 800, Type.MEAT),
            new Dish("beef", false, 700, Type.MEAT),
            new Dish("chicken", false, 400, Type.MEAT),
            new Dish("french fries", true, 530, Type.OTHER),
            new Dish("rice", true, 350, Type.OTHER),
            new Dish("season fruit", true, 120, Type.OTHER),
            new Dish("pizza", true, 550, Type.OTHER),
            new Dish("prawns", false, 300, Type.FISH),
            new Dish("salmon", false, 450, Type.FISH)
        );
    }
}
