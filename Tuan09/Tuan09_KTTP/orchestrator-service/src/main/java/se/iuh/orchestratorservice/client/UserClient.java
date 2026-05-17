package se.iuh.orchestratorservice.client;

// client/UserClient.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.iuh.orchestratorservice.dto.User;

@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    public User getUser(Long userId) {
        String url = "http://172.16.32.93:8081/users/" + userId;

        System.out.println("CALL USER: " + url);

        return restTemplate.getForObject(url, User.class);
    }
}