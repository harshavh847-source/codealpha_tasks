package hotelreservationsystem;

public class Reservation {

    private String bookingId;
    private Guest guest;
    private Room room;
    private int nights;
    private double totalAmount;

    public Reservation(String bookingId, Guest guest,
                       Room room, int nights) {

        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.nights = nights;
        this.totalAmount = room.getPricePerNight() * nights;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
