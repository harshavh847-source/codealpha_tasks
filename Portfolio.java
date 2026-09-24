package stocktradingplatform;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private final double initialBalance;
    private double balance;

    private final Map<String, Integer> holdings = new HashMap<>();

    public Portfolio(double initialBalance) {

        if (initialBalance < 0) {
            throw new IllegalArgumentException(
                    "Balance cannot be negative.");
        }

        this.initialBalance = initialBalance;
        this.balance = initialBalance;
    }

    public boolean buyStock(Stock stock, int quantity) {

        if (stock == null || quantity <= 0) {
            return false;
        }

        double cost = stock.getPrice() * quantity;

        if (cost > balance) {
            return false;
        }

        balance -= cost;

        holdings.merge(
                stock.getSymbol(),
                quantity,
                Integer::sum
        );

        return true;
    }

    public boolean sellStock(Stock stock, int quantity) {

        if (stock == null || quantity <= 0) {
            return false;
        }

        int owned =
                holdings.getOrDefault(
                        stock.getSymbol(), 0);

        if (quantity > owned) {
            return false;
        }

        balance += stock.getPrice() * quantity;

        int remaining = owned - quantity;

        if (remaining == 0) {
            holdings.remove(stock.getSymbol());
        } else {
            holdings.put(
                    stock.getSymbol(),
                    remaining
            );
        }

        return true;
    }

    public double getPortfolioValue(
            Map<String, Stock> market) {

        double value = balance;

        for (Map.Entry<String, Integer> entry
                : holdings.entrySet()) {

            Stock stock =
                    market.get(entry.getKey());

            if (stock != null) {
                value += stock.getPrice()
                        * entry.getValue();
            }
        }

        return value;
    }

    public double getProfitLoss(
            Map<String, Stock> market) {

        return getPortfolioValue(market)
                - initialBalance;
    }

    public double getProfitLossPercentage(
            Map<String, Stock> market) {

        return (getProfitLoss(market)
                / initialBalance) * 100;
    }

    public double getBalance() {
        return balance;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public Map<String, Integer> getHoldings() {
        return new HashMap<>(holdings);
    }
}
