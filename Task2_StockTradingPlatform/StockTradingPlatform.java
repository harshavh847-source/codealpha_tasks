package stocktradingplatform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Random;

public class StockTradingPlatform extends JFrame {

    private final StockMarket market = new StockMarket();
    private final Portfolio portfolio = new Portfolio(100000);
    private final Random random = new Random();

    private final JLabel balanceLabel = new JLabel();
    private final JLabel portfolioLabel = new JLabel();
    private final JLabel profitLabel = new JLabel();

    private final JComboBox<String> stockBox = new JComboBox<>();
    private final JTextField quantityField = new JTextField();

    private final DefaultTableModel marketModel =
            new DefaultTableModel(
                    new String[]{"Symbol", "Company", "Price"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final DefaultTableModel portfolioModel =
            new DefaultTableModel(
                    new String[]{"Symbol", "Quantity"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final DefaultTableModel transactionModel =
            new DefaultTableModel(
                    new String[]{"Transaction"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public StockTradingPlatform() {

        setTitle("Stock Trading Platform");
        setSize(1150, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        loadMarket();
        updatePortfolio();
    }

    private void createGUI() {

        JLabel title =
                new JLabel("STOCK TRADING PLATFORM");

        title.setFont(
                new Font("Arial", Font.BOLD, 26));

        balanceLabel.setFont(
                new Font("Arial", Font.BOLD, 15));

        portfolioLabel.setFont(
                new Font("Arial", Font.BOLD, 15));

        profitLabel.setFont(
                new Font("Arial", Font.BOLD, 15));

        JPanel header =
                new JPanel(new GridLayout(4, 1));

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20));

        header.add(title);
        header.add(balanceLabel);
        header.add(portfolioLabel);
        header.add(profitLabel);

        JPanel tradePanel =
                new JPanel(new GridLayout(2, 5, 10, 10));

        tradePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Trade Stocks"));

        tradePanel.add(new JLabel("Select Stock"));
        tradePanel.add(stockBox);

        tradePanel.add(new JLabel("Quantity"));
        tradePanel.add(quantityField);

        JButton buyButton =
                new JButton("BUY");

        JButton sellButton =
                new JButton("SELL");

        JButton marketButton =
                new JButton("Update Market");

        JButton summaryButton =
                new JButton("Portfolio Summary");

        JButton clearButton =
                new JButton("Clear");

        tradePanel.add(buyButton);
        tradePanel.add(sellButton);
        tradePanel.add(marketButton);
        tradePanel.add(summaryButton);
        tradePanel.add(clearButton);

        JTable marketTable =
                new JTable(marketModel);

        JTable portfolioTable =
                new JTable(portfolioModel);

        JTable transactionTable =
                new JTable(transactionModel);

        marketTable.setRowHeight(25);
        portfolioTable.setRowHeight(25);
        transactionTable.setRowHeight(25);

        JPanel tables =
                new JPanel(
                        new GridLayout(1, 3, 10, 10));

        tables.add(createPanel(
                "Market", marketTable));

        tables.add(createPanel(
                "My Portfolio", portfolioTable));

        tables.add(createPanel(
                "Transaction History",
                transactionTable));

        JPanel center =
                new JPanel(new BorderLayout(10, 10));

        center.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 15, 15));

        center.add(tradePanel, BorderLayout.NORTH);
        center.add(tables, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        buyButton.addActionListener(
                e -> buyStock());

        sellButton.addActionListener(
                e -> sellStock());

        marketButton.addActionListener(
                e -> updateMarket());

        summaryButton.addActionListener(
                e -> showPortfolioSummary());

        clearButton.addActionListener(
                e -> quantityField.setText(""));
    }

    private JPanel createPanel(
            String title, JTable table) {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createTitledBorder(title));

        panel.add(new JScrollPane(table));

        return panel;
    }

    private void loadMarket() {

        marketModel.setRowCount(0);
        stockBox.removeAllItems();

        for (Stock stock :
                market.getStocks().values()) {

            marketModel.addRow(new Object[]{
                    stock.getSymbol(),
                    stock.getCompanyName(),
                    String.format(
                            "₹%.2f",
                            stock.getPrice())
            });

            stockBox.addItem(
                    stock.getSymbol());
        }
    }

    private void buyStock() {

        Stock stock = getSelectedStock();
        int quantity = getQuantity();

        if (stock == null || quantity <= 0) {
            showError(
                    "Select a stock and enter a valid quantity.");
            return;
        }

        if (portfolio.buyStock(
                stock, quantity)) {

            addTransaction(
                    "BUY", stock, quantity);

            updatePortfolio();

            showMessage(
                    "Stock purchased successfully!");

        } else {

            showError(
                    "Insufficient balance.");
        }
    }

    private void sellStock() {

        Stock stock = getSelectedStock();
        int quantity = getQuantity();

        if (stock == null || quantity <= 0) {
            showError(
                    "Select a stock and enter a valid quantity.");
            return;
        }

        if (portfolio.sellStock(
                stock, quantity)) {

            addTransaction(
                    "SELL", stock, quantity);

            updatePortfolio();

            showMessage(
                    "Stock sold successfully!");

        } else {

            showError(
                    "You don't own enough shares.");
        }
    }

    private void updateMarket() {

        for (Stock stock :
                market.getStocks().values()) {

            double currentPrice =
                    stock.getPrice();

            double change =
                    0.95 + random.nextDouble() * 0.10;

            stock.updatePrice(
                    currentPrice * change);
        }

        loadMarket();
        updatePortfolio();

        showMessage(
                "Market prices updated!");
    }

    private void showPortfolioSummary() {

        double portfolioValue =
                portfolio.getPortfolioValue(
                        market.getStocks());

        double profitLoss =
                portfolio.getProfitLoss(
                        market.getStocks());

        double percentage =
                portfolio.getProfitLossPercentage(
                        market.getStocks());

        int stockTypes =
                portfolio.getHoldings().size();

        String summary =
                "PORTFOLIO SUMMARY\n\n" +
                        "Initial Investment : ₹" +
                        String.format("%.2f",
                                portfolio.getInitialBalance()) +

                        "\nAvailable Balance  : ₹" +
                        String.format("%.2f",
                                portfolio.getBalance()) +

                        "\nPortfolio Value    : ₹" +
                        String.format("%.2f",
                                portfolioValue) +

                        "\nProfit / Loss      : ₹" +
                        String.format("%.2f",
                                profitLoss) +

                        "\nReturn             : " +
                        String.format("%.2f%%",
                                percentage) +

                        "\nDifferent Stocks   : " +
                        stockTypes;

        JOptionPane.showMessageDialog(
                this,
                summary,
                "Portfolio Summary",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private Stock getSelectedStock() {

        String symbol =
                (String) stockBox.getSelectedItem();

        if (symbol == null) {
            return null;
        }

        return market.getStock(symbol);
    }

    private int getQuantity() {

        try {

            return Integer.parseInt(
                    quantityField.getText().trim());

        } catch (NumberFormatException e) {

            return -1;
        }
    }

    private void addTransaction(
            String type,
            Stock stock,
            int quantity) {

        Transaction transaction =
                new Transaction(
                        type,
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice());

        transactionModel.addRow(
                new Object[]{
                        transaction.toString()
                });
    }

    private void updatePortfolio() {

        portfolioModel.setRowCount(0);

        for (var entry :
                portfolio.getHoldings().entrySet()) {

            portfolioModel.addRow(
                    new Object[]{
                            entry.getKey(),
                            entry.getValue()
                    });
        }

        balanceLabel.setText(
                String.format(
                        "Available Balance: ₹%.2f",
                        portfolio.getBalance()));

        portfolioLabel.setText(
                String.format(
                        "Portfolio Value: ₹%.2f",
                        portfolio.getPortfolioValue(
                                market.getStocks())));

        profitLabel.setText(
                String.format(
                        "Profit / Loss: ₹%.2f (%.2f%%)",
                        portfolio.getProfitLoss(
                                market.getStocks()),
                        portfolio.getProfitLossPercentage(
                                market.getStocks())));
    }

    private void showMessage(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Trading Error",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> new StockTradingPlatform()
                        .setVisible(true));
    }
}