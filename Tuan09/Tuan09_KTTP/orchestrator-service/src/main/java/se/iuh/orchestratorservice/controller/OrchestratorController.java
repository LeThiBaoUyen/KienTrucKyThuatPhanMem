package se.iuh.orchestratorservice.controller;

// controller/OrchestratorController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.iuh.orchestratorservice.client.TourClient;
import se.iuh.orchestratorservice.dto.BookTourRequest;
import se.iuh.orchestratorservice.dto.BookTourResponse;
import se.iuh.orchestratorservice.dto.Tour;
import se.iuh.orchestratorservice.service.BookingOrchestratorService;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class OrchestratorController {

    @Autowired
    private BookingOrchestratorService  service;

    @Autowired
    private TourClient tourClient;

    // booking
    @PostMapping("/book-tour")
    public BookTourResponse book(@RequestBody BookTourRequest req) {
        return service.bookTour(req);
    }

    // lấy tour thật từ service
    @GetMapping("/tours")
    public List<Tour> getTours() {
        return tourClient.getAllTours();
    }
}