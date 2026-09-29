package stocktradingplatform;

public class Stock {

    private final String symbol;
    private final String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice(double newPrice) {
        if (newPrice > 0) {
            price = newPrice;
        }
    }

    @Override
    public String toString() {
        return symbol + " - " + companyName +
                " | ₹" + String.format("%.2f", price);
    }
}