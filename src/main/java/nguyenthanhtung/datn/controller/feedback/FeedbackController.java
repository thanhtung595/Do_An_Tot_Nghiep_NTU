package nguyenthanhtung.datn.controller.feedback;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.dto.feedback.request.CreateFeedbackDTO;
import nguyenthanhtung.datn.service.feedback.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/{limit}")
    public ResponseEntity<BaseResponse> getAllLimit(@PathVariable("limit") int limit) {
        String subject = "@Get All Limit";

        BaseResponse response = feedbackService.getAll(limit);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createFeedback(@RequestBody CreateFeedbackDTO createFeedbackDTO) {
        String subject = "@Post Create Feedback";

        BaseResponse response = feedbackService.craeteFeedback(createFeedbackDTO);
        response.setSubjectFunction(subject);

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
