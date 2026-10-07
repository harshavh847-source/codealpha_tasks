package hotelreservationsystem;

import javax.swing.*;
import java.awt.*;

public class HotelReservationSystem extends JFrame {

    private final HotelManager manager = new HotelManager();

    private final JComboBox<Room> roomBox = new JComboBox<>();
    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField nightsField = new JTextField();

    private final JLabel availableLabel = new JLabel();

    private final JTextArea bookingArea = new JTextArea();

    public HotelReservationSystem() {

        setTitle("GrandStay Hotel - Reservation System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        refreshRooms();
    }

    private void createGUI() {

        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(45, 35, 60));
        header.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel title = new JLabel("GRANDSTAY HOTEL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitle = new JLabel("  |  Reservation Management");
        subtitle.setForeground(new Color(220, 200, 170));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        header.add(title);
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        form.add(new JLabel("Guest Name"));
        form.add(nameField);

        form.add(new JLabel("Phone"));
        form.add(phoneField);

        form.add(new JLabel("Email"));
        form.add(emailField);

        form.add(new JLabel("Room"));
        form.add(roomBox);

        form.add(new JLabel("Number of Nights"));
        form.add(nightsField);

        JButton bookButton = new JButton("BOOK ROOM");
        JButton cancelButton = new JButton("CANCEL BOOKING");

        bookButton.setBackground(new Color(45, 140, 110));
        bookButton.setForeground(Color.WHITE);

        cancelButton.setBackground(new Color(170, 70, 70));
        cancelButton.setForeground(Color.WHITE);

        form.add(bookButton);
        form.add(cancelButton);

        JPanel left = new JPanel(new BorderLayout());

        JLabel formTitle = new JLabel("  Guest & Room Details");
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));

        left.add(formTitle, BorderLayout.NORTH);
        left.add(form, BorderLayout.CENTER);

        add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        availableLabel.setFont(new Font("Arial", Font.BOLD, 16));

        bookingArea.setEditable(false);
        bookingArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        right.add(availableLabel, BorderLayout.NORTH);
        right.add(new JScrollPane(bookingArea), BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);

        bookButton.addActionListener(e -> bookRoom());

        cancelButton.addActionListener(e -> cancelBooking());
    }

    private void refreshRooms() {

        roomBox.removeAllItems();

        for (Room room : manager.getRooms()) {
            if (room.isAvailable()) {
                roomBox.addItem(room);
            }
        }

        availableLabel.setText(
                "Available Rooms: " + manager.availableRooms());

        showBookings();
    }

    private void bookRoom() {

        try {

            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || phone.isEmpty()
                    || email.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter all guest details.");

                return;
            }

            int nights = Integer.parseInt(
                    nightsField.getText().trim());

            if (nights <= 0) {
                throw new NumberFormatException();
            }

            Room room = (Room) roomBox.getSelectedItem();

            if (room == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No room is available.");

                return;
            }

            Guest guest =
                    new Guest(name, phone, email);

            String bookingId =
                    "BK" + (manager.getReservations().size() + 1001);

            Reservation reservation =
                    new Reservation(
                            bookingId,
                            guest,
                            room,
                            nights);

            if (manager.bookRoom(reservation)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Booking Successful!\n\n" +
                                "Booking ID: " + bookingId +
                                "\nTotal Bill: ₹" +
                                reservation.getTotalAmount());

                clearFields();
                refreshRooms();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid number of nights.");
        }
    }

    private void cancelBooking() {

        String bookingId = JOptionPane.showInputDialog(
                this,
                "Enter Booking ID:");

        if (bookingId == null ||
                bookingId.trim().isEmpty()) {
            return;
        }

        if (manager.cancelBooking(
                bookingId.trim())) {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking cancelled successfully.");

            refreshRooms();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking ID not found.");
        }
    }

    private void showBookings() {

        bookingArea.setText("");

        for (Reservation reservation :
                manager.getReservations()) {

            bookingArea.append(
                    "Booking ID : " +
                            reservation.getBookingId() +
                            "\nGuest      : " +
                            reservation.getGuest().getName() +
                            "\nRoom       : " +
                            reservation.getRoom().getRoomNumber() +
                            " (" +
                            reservation.getRoom().getRoomType() +
                            ")" +
                            "\nNights     : " +
                            reservation.getNights() +
                            "\nTotal Bill : ₹" +
                            reservation.getTotalAmount() +
                            "\n-----------------------------\n");
        }

        if (manager.getReservations().isEmpty()) {
            bookingArea.setText(
                    "No active reservations.");
        }
    }

    private void clearFields() {

        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        nightsField.setText("");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new HotelReservationSystem().setVisible(true));
    }
}