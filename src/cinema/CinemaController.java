package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import static cinema.Cinema.getAllSeats;

class Token {
    UUID token;

    public Token() {
    }

    public Token(UUID token) {
        this.token = token;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
@RestController
public class CinemaController {

    private final Cinema cinema;

    public CinemaController() {
        this.cinema = getAllSeats(9 ,9);
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Seat seat) {
        if (seat.getColumn() > cinema.getColumns()
                || seat.getRow() > cinema.getRows()
                || seat.getRow() < 1
                || seat.getColumn() < 1) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < cinema.getSeats().size(); i++) {
            Seat s = cinema.getSeats().get(i);
            System.out.println(s);
            if (s.equals(seat)) {
                OrderedSeat orderedSeat = new OrderedSeat(UUID.randomUUID(), s);
                cinema.getOrderedSeats().add(orderedSeat);
                cinema.getSeats().remove(i);
                return new ResponseEntity<>(orderedSeat, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        List<OrderedSeat> orderedSeats = cinema.getOrderedSeats();
        for (OrderedSeat orderedSeat : orderedSeats) {
            if (orderedSeat.getToken().equals(token.getToken())) {
                orderedSeats.remove(orderedSeat);
                cinema.getSeats().add(orderedSeat.getTicket());
                return new ResponseEntity<>(Map.of("ticket", orderedSeat.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats(@RequestParam(required = false) String password) {
        if (password != null && password.equals("super_secret")) {
            Map<String, Integer> statistic = new HashMap<>();
            int currentIncome = 0;
            for (OrderedSeat orderedSeat : cinema.getOrderedSeats()) {
                currentIncome += orderedSeat.getTicket().getPrice();
            }
            int numberOfAvailableSeats = cinema.getSeats().size();
            int numberOfPurchasedTickets = cinema.getOrderedSeats().size();
            statistic.put("income", currentIncome);
            statistic.put("available", numberOfAvailableSeats);
            statistic.put("purchased", numberOfPurchasedTickets);
            return new ResponseEntity<>(statistic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.valueOf(401));
        }
    }

}
