package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
public class Cinema {
    private int rows;
    private int columns;
    private List<Seat> seats;
    @JsonIgnore
    private List<OrderedSeat> orderedSeats;

    public Cinema(int rows, int columns, List<Seat> seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
        this.orderedSeats = new ArrayList<>();
    }
    public static Cinema getAllSeats(int rows, int columns) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column <= columns; column++) {
                seats.add(new Seat(row, column));
            }
        }
        return new Cinema(rows, columns, seats);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Seat> getSeats() {
        // Return a copy of the seats to avoid exposing the internal list directly
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        // Set the seats using a new list to avoid external modification
        this.seats = seats;
    }

    public List<OrderedSeat> getOrderedSeats() {
        return orderedSeats;
    }

    public void setOrderedSeats(List<OrderedSeat> orderedSeats) {
        this.orderedSeats = orderedSeats;
    }
}
