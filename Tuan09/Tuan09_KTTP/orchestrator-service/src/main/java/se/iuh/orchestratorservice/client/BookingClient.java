package se.iuh.orchestratorservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.iuh.orchestratorservice.dto.BookTourRequest;
import se.iuh.orchestratorservice.dto.Booking;

@Component
public class BookingClient {

    @Autowired
    private RestTemplate restTemplate;

    public Booking createBooking(BookTourRequest req) {
        String url = "http://172.16.64.86:8083/bookings";

        System.out.println("CALL BOOKING: " + url);

        return restTemplate.postForObject(url, req, Booking.class);
    }
}