package com.example.stockquotes.service;

import com.example.stockquotes.exception.InvalidSymbolException;
import com.example.stockquotes.model.StockQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockQuoteService {

    @Value("${alpha.vantage.api.url}")
    private String apiUrl;

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @Value("${alpha.vantage.function}")
    private String function;

    private final RestTemplate restTemplate;

    public StockQuoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StockQuote getStockQuote(String symbol) {
        String url = String.format("%s?function=%s&symbol=%s&apikey=%s",
                apiUrl, function, symbol, apiKey);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return mapToStockQuote(response);
    }

    public Map<String, StockQuote> getStockQuotes(List<String> symbols) {
        return symbols.stream().collect(Collectors.toMap(
                symbol -> symbol,
                symbol -> {
                    try {
                        return getStockQuote(symbol);
                    } catch (InvalidSymbolException e) {
                        return null; // or handle differently
                    }
                }
        ));
    }

    private StockQuote mapToStockQuote(Map<String, Object> response) {
        Map<String, String> globalQuote = (Map<String, String>) response.get("Global Quote");
        if (globalQuote == null) {
            throw new InvalidSymbolException("Invalid stock symbol or API error");
        }

        StockQuote quote = new StockQuote();
        quote.setSymbol(globalQuote.get("01. symbol"));
        quote.setPrice(Double.parseDouble(globalQuote.get("05. price")));
        quote.setVolume(Long.parseLong(globalQuote.get("06. volume")));
        quote.setLatestTradingDay(LocalDate.parse(globalQuote.get("07. latest trading day")));
        quote.setPreviousClose(Double.parseDouble(globalQuote.get("08. previous close")));
        quote.setChange(Double.parseDouble(globalQuote.get("09. change")));
        quote.setChangePercent(globalQuote.get("10. change percent"));

        return quote;
    }
}
