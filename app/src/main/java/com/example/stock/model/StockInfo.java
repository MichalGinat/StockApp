package com.example.stock.model;

public class StockInfo {
    private String symbol;
    private String name;

    public StockInfo(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
