package hotelreservationsystem;

import java.util.ArrayList;
import java.util.List;

public class HotelManager {

    private final List<Room> rooms = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public HotelManager() {
        rooms.add(new Room(101, "Single", 1500));
        rooms.add(new Room(102, "Single", 1500));
        rooms.add(new Room(201, "Double", 2500));
        rooms.add(new Room(202, "Double", 2500));
        rooms.add(new Room(301, "Suite", 4000));
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public boolean bookRoom(Reservation reservation) {

        if (!reservation.getRoom().isAvailable()) {
            return false;
        }

        reservation.getRoom().setAvailable(false);
        reservations.add(reservation);

        return true;
    }

    public boolean cancelBooking(String bookingId) {

        for (Reservation reservation : reservations) {

            if (reservation.getBookingId()
                    .equalsIgnoreCase(bookingId)) {

                reservation.getRoom().setAvailable(true);
                reservations.remove(reservation);

                return true;
            }
        }

        return false;
    }

    public int availableRooms() {

        int count = 0;

        for (Room room : rooms) {
            if (room.isAvailable()) {
                count++;
            }
        }

        return count;
    }
}
