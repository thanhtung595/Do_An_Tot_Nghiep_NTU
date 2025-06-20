package nguyenthanhtung.datn.dto.feedback.request;

import lombok.Data;

@Data
public class CreateFeedbackDTO {
    int idUser;
    String comment;
    String doctor;
    double rating;
    String title;
}
