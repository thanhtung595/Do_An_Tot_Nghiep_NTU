package nguyenthanhtung.datn.constants.ConstantQuery.feedback;

public class QueryConstansFeedback {

    public static final String CRREATE_FEEDBACK = """
        INSERT INTO feedbacks (userid, title, comment, rating, doctor, timecreate) VALUES
        (?, ?, ?, ?, ?, NOW());
    """;

    public static final String SELECT_LIMIT = """
        SELECT
          userid,
          title,
          comment,
          rating,
          doctor,
          TO_CHAR(timecreate, 'yyyy-MM-dd') AS date
        FROM feedbacks
        ORDER BY timecreate DESC, rating DESC
        LIMIT ?
    """;
}
