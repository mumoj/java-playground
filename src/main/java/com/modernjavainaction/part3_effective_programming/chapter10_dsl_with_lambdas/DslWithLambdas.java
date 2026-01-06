package com.modernjavainaction.part3_effective_programming.chapter10_dsl_with_lambdas;

import java.util.function.Consumer;

/**
 * Chapter 10: Domain-Specific Languages using Lambdas
 * 
 * Key concepts:
 * - Creating fluent APIs
 * - Method chaining
 * - Nested function style
 * - Lambda sequencing
 * - Real-world DSL examples
 */
public class DslWithLambdas {

    public static void main(String[] args) {
        System.out.println("=== Chapter 10: Domain-Specific Languages with Lambdas ===\n");

        // 1. Method Chaining DSL
        System.out.println("1. Method Chaining DSL (Builder pattern):");
        Order order1 = OrderBuilder.builder()
                .forCustomer("John")
                .buy(100).stock("IBM").at(125.00)
                .sell(50).stock("GOOGLE").at(375.00)
                .build();
        System.out.println("  " + order1);

        // 2. Nested Function DSL
        System.out.println("\n2. Nested Function DSL:");
        Order order2 = order("John",
                buy(100, stock("IBM"), at(125.00)),
                sell(50, stock("GOOGLE"), at(375.00)));
        System.out.println("  " + order2);

        // 3. Lambda Sequencing DSL
        System.out.println("\n3. Lambda Sequencing DSL:");
        Order order3 = LambdaOrderBuilder.order(o -> {
            o.forCustomer("John");
            o.buy(t -> {
                t.quantity(100);
                t.price(125.00);
                t.stock("IBM");
            });
            o.sell(t -> {
                t.quantity(50);
                t.price(375.00);
                t.stock("GOOGLE");
            });
        });
        System.out.println("  " + order3);

        // 4. Mixed approach
        System.out.println("\n4. Mixed DSL Approaches:");
        System.out.println("  See source code for combining multiple DSL styles");

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 10.1: Create a DSL for building SQL queries");
        System.out.println("Exercise 10.2: Create a DSL for building HTML elements");
        System.out.println("Exercise 10.3: Extend the Order DSL to include tax calculation");
    }

    // Nested function style helpers
    public static Order order(String customer, Trade... trades) {
        Order order = new Order();
        order.setCustomer(customer);
        for (Trade trade : trades) {
            order.addTrade(trade);
        }
        return order;
    }

    public static Trade buy(int quantity, Stock stock, double price) {
        return buildTrade(quantity, stock, price, Trade.Type.BUY);
    }

    public static Trade sell(int quantity, Stock stock, double price) {
        return buildTrade(quantity, stock, price, Trade.Type.SELL);
    }

    private static Trade buildTrade(int quantity, Stock stock, double price, Trade.Type type) {
        Trade trade = new Trade();
        trade.setType(type);
        trade.setQuantity(quantity);
        trade.setStock(stock);
        trade.setPrice(price);
        return trade;
    }

    public static Stock stock(String symbol) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        return stock;
    }

    public static double at(double price) {
        return price;
    }
}

// Domain classes
class Stock {
    private String symbol;
    private String market;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    @Override
    public String toString() {
        return symbol;
    }
}

class Trade {
    public enum Type {
        BUY, SELL
    }

    private Type type;
    private Stock stock;
    private int quantity;
    private double price;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getValue() {
        return quantity * price;
    }

    @Override
    public String toString() {
        return type + " " + quantity + " " + stock + " @ " + price;
    }
}

class Order {
    private String customer;
    private java.util.List<Trade> trades = new java.util.ArrayList<>();

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void addTrade(Trade trade) {
        trades.add(trade);
    }

    public double getValue() {
        return trades.stream().mapToDouble(Trade::getValue).sum();
    }

    @Override
    public String toString() {
        return "Order[customer=" + customer + ", trades=" + trades + ", value=" + getValue() + "]";
    }
}

// Method Chaining Builder
class OrderBuilder {
    private final Order order = new Order();

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public OrderBuilder forCustomer(String customer) {
        order.setCustomer(customer);
        return this;
    }

    public TradeBuilder buy(int quantity) {
        return new TradeBuilder(this, Trade.Type.BUY, quantity);
    }

    public TradeBuilder sell(int quantity) {
        return new TradeBuilder(this, Trade.Type.SELL, quantity);
    }

    public OrderBuilder addTrade(Trade trade) {
        order.addTrade(trade);
        return this;
    }

    public Order build() {
        return order;
    }
}

class TradeBuilder {
    private final OrderBuilder builder;
    private final Trade trade = new Trade();

    public TradeBuilder(OrderBuilder builder, Trade.Type type, int quantity) {
        this.builder = builder;
        trade.setType(type);
        trade.setQuantity(quantity);
    }

    public StockBuilder stock(String symbol) {
        return new StockBuilder(builder, trade, symbol);
    }
}

class StockBuilder {
    private final OrderBuilder builder;
    private final Trade trade;
    private final Stock stock = new Stock();

    public StockBuilder(OrderBuilder builder, Trade trade, String symbol) {
        this.builder = builder;
        this.trade = trade;
        stock.setSymbol(symbol);
    }

    public OrderBuilder at(double price) {
        trade.setPrice(price);
        trade.setStock(stock);
        return builder.addTrade(trade);
    }
}

// Lambda Sequencing Builder
class LambdaOrderBuilder {
    private final Order order = new Order();

    public static Order order(Consumer<LambdaOrderBuilder> consumer) {
        LambdaOrderBuilder builder = new LambdaOrderBuilder();
        consumer.accept(builder);
        return builder.order;
    }

    public void forCustomer(String customer) {
        order.setCustomer(customer);
    }

    public void buy(Consumer<LambdaTradeBuilder> consumer) {
        trade(consumer, Trade.Type.BUY);
    }

    public void sell(Consumer<LambdaTradeBuilder> consumer) {
        trade(consumer, Trade.Type.SELL);
    }

    private void trade(Consumer<LambdaTradeBuilder> consumer, Trade.Type type) {
        LambdaTradeBuilder builder = new LambdaTradeBuilder();
        builder.trade.setType(type);
        consumer.accept(builder);
        order.addTrade(builder.trade);
    }
}

class LambdaTradeBuilder {
    final Trade trade = new Trade();
    private final Stock stock = new Stock();

    public void quantity(int quantity) {
        trade.setQuantity(quantity);
    }

    public void price(double price) {
        trade.setPrice(price);
    }

    public void stock(String symbol) {
        stock.setSymbol(symbol);
        trade.setStock(stock);
    }
}
