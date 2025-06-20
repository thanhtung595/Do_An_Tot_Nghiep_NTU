package nguyenthanhtung.datn.constants.ConstantQuery.header;

public class ConstantQueryHeader {

    public static final String GET_ALL_HEADER = """
        SELECT * FROM HeaderLayout
        WHERE police = 'patient'
            AND (IsToken = '0'
            OR IsDefault = '1')
        ORDER BY levesort;
    """;

    public static final String GET_ALL_HEADER_ADMIN = """
        SELECT * FROM HeaderLayout
        WHERE police = 'admin'
        AND istoken = '0'
        ORDER BY levesort;
    """;

    public static final String GET_ALL_PAGE_HEADER_ADMIN = """
        SELECT * FROM HeaderLayout
        WHERE police = 'admin'
        AND istoken = '1'
        ORDER BY levesort;
    """;

    public static final String GET_ALL_HEADER_DOCTOR = """
        SELECT * FROM HeaderLayout
        WHERE police = 'doctor'
            AND (IsToken = '1'
            OR IsDefault = '1')
        ORDER BY levesort;
    """;

    public static final String GET_ALL_HEADER_PATIENT = """
        SELECT * FROM HeaderLayout
        WHERE police = 'patient'
            AND (IsToken = '1'
            OR IsDefault = '1')
        ORDER BY levesort;
    """;
}
