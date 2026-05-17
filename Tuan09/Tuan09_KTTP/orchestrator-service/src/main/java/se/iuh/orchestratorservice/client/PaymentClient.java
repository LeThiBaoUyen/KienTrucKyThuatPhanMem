package se.iuh.orchestratorservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.iuh.orchestratorservice.dto.Payment;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentClient {

    @Autowired
    private RestTemplate restTemplate;

    public Payment pay(Long bookingId) {
        String url = "http://172.16.64.86:8084/payments";

        System.out.println("CALL PAYMENT: " + url);

        Map<String, Object> req = new HashMap<>();
        req.put("bookingId", bookingId);

        return restTemplate.postForObject(url, req, Payment.class);
    }
}
