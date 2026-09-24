package stocktradingplatform;

import java.util.LinkedHashMap;
import java.util.Map;

public class StockMarket {

    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public StockMarket() {
        addStock(new Stock("TCS", "Tata Consultancy Services", 3850));
        addStock(new Stock("INFY", "Infosys", 1750));
        addStock(new Stock("RELIANCE", "Reliance Industries", 2950));
        addStock(new Stock("HDFCBANK", "HDFC Bank", 1680));
        addStock(new Stock("WIPRO", "Wipro", 520));
    }

    private void addStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public Map<String, Stock> getStocks() {
        return new LinkedHashMap<>(stocks);
    }
}