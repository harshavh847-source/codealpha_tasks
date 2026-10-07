package aichatbot;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AIChatbot extends JFrame {

    private final ChatbotEngine chatbot = new ChatbotEngine();

    private final Color NAVY = new Color(18, 28, 48);
    private final Color NAVY_LIGHT = new Color(30, 44, 70);
    private final Color TEAL = new Color(35, 190, 170);
    private final Color BACKGROUND = new Color(242, 246, 250);
    private final Color BOT_BUBBLE = Color.WHITE;
    private final Color USER_BUBBLE = new Color(218, 247, 240);
    private final Color TEXT_DARK = new Color(35, 48, 65);
    private final Color TEXT_MUTED = new Color(145, 160, 180);

    private final JPanel messagesPanel = new JPanel();
    private final JScrollPane chatScroll =
            new JScrollPane(messagesPanel);
    private final JTextField inputField = new JTextField();

    private final JLabel statusLabel =
            new JLabel("● ONLINE  |  0 messages");

    private int messageCount = 0;

    public AIChatbot() {

        setTitle("AI Study Assistant");
        setSize(1050, 720);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        showWelcomeMessage();
    }

    private void createGUI() {

        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        add(createSidebar(), BorderLayout.WEST);
        add(createMainArea(), BorderLayout.CENTER);
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(235, 700));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 18, 20, 18));

        JLabel brandIcon = new JLabel("AI");
        brandIcon.setOpaque(true);
        brandIcon.setBackground(TEAL);
        brandIcon.setForeground(NAVY);
        brandIcon.setFont(new Font("Arial", Font.BOLD, 25));
        brandIcon.setHorizontalAlignment(SwingConstants.CENTER);
        brandIcon.setPreferredSize(new Dimension(55, 55));
        brandIcon.setMaximumSize(new Dimension(55, 55));
        brandIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brandName = new JLabel("STUDY SPACE");
        brandName.setForeground(Color.WHITE);
        brandName.setFont(new Font("Arial", Font.BOLD, 20));
        brandName.setBorder(new EmptyBorder(18, 0, 4, 0));
        brandName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Your learning companion");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brandIcon);
        sidebar.add(brandName);
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(38));

        JLabel section = new JLabel("QUICK TOPICS");
        section.setForeground(TEXT_MUTED);
        section.setFont(new Font("Arial", Font.BOLD, 11));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(section);
        sidebar.add(Box.createVerticalStrut(12));

        addTopicButton(sidebar, "Java Basics", "What is Java?");
        addTopicButton(sidebar, "OOP Concepts", "Explain OOP");
        addTopicButton(sidebar, "ArrayList", "What is ArrayList?");
        addTopicButton(sidebar, "Current Time", "What is the time?");
        addTopicButton(sidebar, "Today's Date", "What is today's date?");
        addTopicButton(sidebar, "Help", "help");

        sidebar.add(Box.createVerticalGlue());

        JPanel bottomCard = new JPanel();
        bottomCard.setLayout(new BoxLayout(bottomCard, BoxLayout.Y_AXIS));
        bottomCard.setBackground(NAVY_LIGHT);
        bottomCard.setBorder(new EmptyBorder(15, 13, 15, 13));
        bottomCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomCard.setMaximumSize(new Dimension(220, 100));

        JLabel bottomTitle = new JLabel("Keep learning.");
        bottomTitle.setForeground(Color.WHITE);
        bottomTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel bottomText = new JLabel(
                "<html>Small steps lead to<br>great skills.</html>");
        bottomText.setForeground(TEXT_MUTED);
        bottomText.setFont(new Font("Arial", Font.PLAIN, 12));

        bottomCard.add(bottomTitle);
        bottomCard.add(Box.createVerticalStrut(7));
        bottomCard.add(bottomText);

        sidebar.add(bottomCard);

        return sidebar;
    }

    private void addTopicButton(
            JPanel sidebar,
            String label,
            String prompt) {

        JButton button = new JButton(label);

        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(new Color(220, 230, 242));
        button.setBackground(NAVY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 12, 12, 8));
        button.setMaximumSize(new Dimension(220, 44));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));

        button.addActionListener(e -> sendMessage(prompt));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(NAVY_LIGHT);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(NAVY);
            }
        });

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    private JPanel createMainArea() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createChatArea(), BorderLayout.CENTER);
        main.add(createInputArea(), BorderLayout.SOUTH);

        return main;
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 25, 18, 25));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("AI Study Assistant");
        title.setForeground(TEXT_DARK);
        title.setFont(new Font("Arial", Font.BOLD, 23));

        statusLabel.setForeground(new Color(20, 160, 135));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(7, 0, 0, 0));

        titles.add(title);
        titles.add(statusLabel);

        JButton clearButton = new JButton("New Chat");
        clearButton.setBackground(new Color(235, 241, 247));
        clearButton.setForeground(TEXT_DARK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setFocusPainted(false);
        clearButton.setBorder(new EmptyBorder(10, 16, 10, 16));

        clearButton.addActionListener(e -> {

            messagesPanel.removeAll();
            messageCount = 0;

            statusLabel.setText("● ONLINE  |  0 messages");

            statusLabel.setForeground(
                    new Color(20, 160, 135));

            showWelcomeMessage();
        });

        header.add(titles, BorderLayout.WEST);
        header.add(clearButton, BorderLayout.EAST);

        return header;
    }

    private JScrollPane createChatArea() {

        messagesPanel.setLayout(
                new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));

        messagesPanel.setBackground(BACKGROUND);

        messagesPanel.setBorder(
                new EmptyBorder(22, 24, 22, 24));

        chatScroll.setBorder(null);

        chatScroll.getViewport()
                .setBackground(BACKGROUND);

        chatScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        chatScroll.getVerticalScrollBar()
                .setUnitIncrement(18);

        return chatScroll;
    }

    private JPanel createInputArea() {

        JPanel bottom =
                new JPanel(new BorderLayout(12, 0));

        bottom.setBackground(Color.WHITE);

        bottom.setBorder(
                new EmptyBorder(18, 22, 18, 22));

        inputField.setFont(
                new Font("Arial", Font.PLAIN, 15));

        inputField.setForeground(TEXT_DARK);

        inputField.setBackground(
                new Color(244, 247, 250));

        inputField.setBorder(
                new EmptyBorder(15, 15, 15, 15));

        JButton sendButton = new JButton("SEND");

        sendButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(TEAL);
        sendButton.setFocusPainted(false);

        sendButton.setBorder(
                new EmptyBorder(14, 22, 14, 22));

        sendButton.addActionListener(
                e -> sendMessage());

        inputField.addActionListener(
                e -> sendMessage());

        bottom.add(
                inputField,
                BorderLayout.CENTER);

        bottom.add(
                sendButton,
                BorderLayout.EAST);

        return bottom;
    }

    private void showWelcomeMessage() {

        addMessage(
                "Hello! Welcome to your Study Space. " +
                "I'm here to help you learn Java and programming.",
                false);

        addMessage(
                "Choose a topic from the left, or type your own question below.",
                false);
    }

    private void sendMessage() {

        sendMessage(inputField.getText().trim());
    }

    private void sendMessage(String message) {

        if (message == null ||
                message.trim().isEmpty()) {
            return;
        }

        addMessage(message, true);

        messageCount++;

        statusLabel.setText(
                "● ONLINE  |  " +
                messageCount +
                " messages");

        inputField.setText("");

        String response =
                chatbot.getResponse(message);

        addMessage(response, false);

        if (message.equalsIgnoreCase("bye") ||
                message.equalsIgnoreCase("exit")) {

            statusLabel.setText(
                    "● SESSION ENDED  |  " +
                    messageCount +
                    " messages");

            statusLabel.setForeground(
                    new Color(220, 130, 90));
        }

        inputField.requestFocus();
    }

    private void addMessage(
            String message,
            boolean isUser) {

        JPanel row =
                new JPanel(new BorderLayout());

        row.setOpaque(false);

        row.setBorder(
                new EmptyBorder(7, 0, 7, 0));

        JPanel bubble =
                createBubble(message, isUser);

        if (isUser) {
            row.add(bubble, BorderLayout.EAST);
        } else {
            row.add(bubble, BorderLayout.WEST);
        }

        messagesPanel.add(row);

        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() ->
                chatScroll.getVerticalScrollBar()
                        .setValue(
                                chatScroll
                                        .getVerticalScrollBar()
                                        .getMaximum()));
    }

    private JPanel createBubble(
            String message,
            boolean isUser) {

        JPanel bubble =
                new RoundedPanel(
                        isUser
                                ? USER_BUBBLE
                                : BOT_BUBBLE,
                        22);

        bubble.setLayout(
                new BoxLayout(
                        bubble,
                        BoxLayout.Y_AXIS));

        bubble.setBorder(
                new EmptyBorder(
                        13, 17, 13, 17));

        JLabel sender =
                new JLabel(
                        isUser
                                ? "YOU"
                                : "STUDY ASSISTANT");

        sender.setFont(
                new Font("Arial", Font.BOLD, 10));

        sender.setForeground(
                isUser
                        ? new Color(20, 135, 115)
                        : TEAL);

        JTextArea text =
                new JTextArea(message);

        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setForeground(TEXT_DARK);

        text.setFont(
                new Font("Arial", Font.PLAIN, 14));

        int rows =
                Math.max(
                        1,
                        (message.length() / 42) + 1);

        text.setRows(
                Math.min(rows, 8));

        text.setColumns(34);

        bubble.add(sender);

        bubble.add(
                Box.createVerticalStrut(7));

        bubble.add(text);

        bubble.setMaximumSize(
                new Dimension(500, 250));

        return bubble;
    }

    private static class RoundedPanel
            extends JPanel {

        private final Color fill;
        private final int radius;

        public RoundedPanel(
                Color fill,
                int radius) {

            this.fill = fill;
            this.radius = radius;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(fill);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new AIChatbot().setVisible(true));
    }
}
