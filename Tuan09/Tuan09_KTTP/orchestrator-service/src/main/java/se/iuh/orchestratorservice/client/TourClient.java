package se.iuh.orchestratorservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.iuh.orchestratorservice.dto.Tour;

import java.util.List;

@Component
public class TourClient {

    @Autowired
    private RestTemplate restTemplate;

    // lấy list tour
    public List<Tour> getAllTours() {
        String url = "http://172.16.64.36:8082/tours";

        ResponseEntity<List<Tour>> res = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tour>>() {}
        );

        return res.getBody();
    }

    // lấy 1 tour
    public Tour getTour(Long id) {
        String url = "http://172.16.64.36:8082/tours/" + id;

        System.out.println("CALL TOUR: " + url);

        return restTemplate.getForObject(url, Tour.class);
    }
}