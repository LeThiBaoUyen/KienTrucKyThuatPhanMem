package se.iuh.orchestratorservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookTourRequest {
    private Long userId;
    private Long tourId;
    // getter/setter
}