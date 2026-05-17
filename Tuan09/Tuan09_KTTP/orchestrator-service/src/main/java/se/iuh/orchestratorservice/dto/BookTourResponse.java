package se.iuh.orchestratorservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
public class BookTourResponse {
    private String status;
    private String message;
    private Long bookingId;

    public BookTourResponse(String status, String message, Long bookingId) {
        this.status = status;
        this.message = message;
        this.bookingId = bookingId;
    }
}