package com.modernjavainaction.common.model;

/**
 * Apple class used throughout the book for filtering and sorting examples.
 * Featured in Chapters 1-3 for behavior parameterization and lambda expressions.
 */
public class Apple {
    
    private int weight;
    private Color color;

    public enum Color {
        RED, GREEN
    }

    public Apple(int weight, Color color) {
        this.weight = weight;
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("Apple{weight=%d, color=%s}", weight, color);
    }
}
