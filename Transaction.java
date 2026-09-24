package stocktradingplatform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private final String type;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final LocalDateTime time;

    public Transaction(String type, String symbol,
                       int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String date = time.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        return type + " | " + symbol +
                " | Qty: " + quantity +
                " | Price: ₹" + String.format("%.2f", price) +
                " | " + date;
    }
}