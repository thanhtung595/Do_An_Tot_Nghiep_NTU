package nguyenthanhtung.datn.repository.feedback.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.constants.ConstantQuery.feedback.QueryConstansFeedback;
import nguyenthanhtung.datn.constants.ConstantQuery.users.ConstantQueryUsers;
import nguyenthanhtung.datn.dto.feedback.request.CreateFeedbackDTO;
import nguyenthanhtung.datn.repository.feedback.FeedbackRepository;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImplFeedbackRepository implements FeedbackRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getAll(int limit) {
        return jdbcTemplate.query(QueryConstansFeedback.SELECT_LIMIT, new ColumnMapRowMapper(), limit);
    }

    @Override
    public void craeteFeedback(CreateFeedbackDTO createFeedbackDTO) {
        double roundedRating = BigDecimal.valueOf(createFeedbackDTO.getRating())
                .setScale(1, RoundingMode.DOWN)
                .doubleValue();
        jdbcTemplate.update(QueryConstansFeedback.CRREATE_FEEDBACK,
                createFeedbackDTO.getIdUser(),
                createFeedbackDTO.getTitle(),
                createFeedbackDTO.getComment(),
                roundedRating,
                createFeedbackDTO.getDoctor());
    }
}
