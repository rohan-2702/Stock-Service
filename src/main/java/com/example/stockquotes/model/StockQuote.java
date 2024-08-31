package com.example.stockquotes.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockQuote {
    private String symbol;
    private double price;
    private long volume;
    private LocalDate latestTradingDay;
    private double previousClose;
    private double change;
    private String changePercent;
}
