package se.iuh.orchestratorservice.service;

// service/BookingOrchestratorService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.iuh.orchestratorservice.client.BookingClient;
import se.iuh.orchestratorservice.client.PaymentClient;
import se.iuh.orchestratorservice.client.TourClient;
import se.iuh.orchestratorservice.client.UserClient;
import se.iuh.orchestratorservice.dto.*;

@Service
public class BookingOrchestratorService  {

    @Autowired
    private UserClient userClient;
    @Autowired private TourClient tourClient;
    @Autowired private BookingClient bookingClient;
    @Autowired private PaymentClient paymentClient;

    public BookTourResponse bookTour(BookTourRequest req) {

        try {
            // 1. validate user
            User user = userClient.getUser(req.getUserId());
            if (user == null) {
                return new BookTourResponse("FAIL", "User not found", null);
            }

            // 2. get tour
            Tour tour = tourClient.getTour(req.getTourId());
            if (tour == null) {
                return new BookTourResponse("FAIL", "Tour not found", null);
            }

            // 3. create booking
            Booking booking = bookingClient.createBooking(req);

            // 4. payment
            Payment payment = paymentClient.pay(booking.id);

            if ("SUCCESS".equals(payment.status)) {
                return new BookTourResponse("SUCCESS", "Booking success", booking.id);
            } else {
                return new BookTourResponse("FAIL", "Payment failed", booking.id);
            }

        } catch (Exception e) {
            return new BookTourResponse("ERROR", e.getMessage(), null);
        }
    }
}