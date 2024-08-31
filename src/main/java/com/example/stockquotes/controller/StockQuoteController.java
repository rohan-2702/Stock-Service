package com.example.stockquotes.controller;

import com.example.stockquotes.model.StockQuote;
import com.example.stockquotes.service.StockQuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockQuoteController {

    private final StockQuoteService stockQuoteService;

    public StockQuoteController(StockQuoteService stockQuoteService) {
        this.stockQuoteService = stockQuoteService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockQuote> getQuoteBySymbol(@PathVariable String symbol) {
        StockQuote quote = stockQuoteService.getStockQuote(symbol);
        return ResponseEntity.ok(quote);
    }

    @GetMapping("/batch")
    public ResponseEntity<Map<String, StockQuote>> getBatchQuotesBySymbols(@RequestParam List<String> symbols) {
        Map<String, StockQuote> quotes = stockQuoteService.getStockQuotes(symbols);
        return ResponseEntity.ok(quotes);
    }
}
