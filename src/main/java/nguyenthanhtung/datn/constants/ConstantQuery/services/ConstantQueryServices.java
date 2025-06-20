package nguyenthanhtung.datn.constants.ConstantQuery.services;

public class ConstantQueryServices {

    public static final String SELECT_ALL_SERVICE = """
        SELECT
          id,
          id as servicesid,
          name,
          description,
          image,
          price
        FROM Services;
    """;

    public static final String GET_USE_SERVICES_BY_ID_PATIENT = """
        SELECT
          us.id as id,
          us.servicesid as servicesid,
          s.name as name,
          s.description as description,
          s.price as price
        FROM UseServices us
        INNER JOIN Services s
        ON us.servicesid = s.id
        WHERE patientid = ?;
    """;

    public static final String GET_USE_SERVICES_BY_ID_PATIENT_1 = """
        SELECT
          s.id as id,
          us.servicesid as servicesid,
          s.name as name,
          s.description as description,
          s.price as price
        FROM UseServices us
        INNER JOIN Services s
        ON us.servicesid = s.id
        WHERE patientid = ?;
    """;

    public static final String GET_LIST_NAME_SERVICE_INVOICE_BY_ID_PATIENT = """
        SELECT sv.name
            FROM useservices usrc
            INNER JOIN services sv
            ON usrc.servicesid = sv.id
            WHERE usrc.patientid = ?;
    """;

    public static final String GET_LIST_SERVICE_INVOICE_BY_ID_PATIENT = """
        SELECT sv.name, sv.price , TO_CHAR(usrc.CreatedAt, 'YYYY-MM-DD') as date
            FROM useservices usrc
            INNER JOIN services sv
            ON usrc.servicesid = sv.id
            WHERE usrc.patientid = ?;
    """;

    public static final String CREATE_SERVICES = """
        INSERT INTO UseServices(PatientID, ServicesID, Price, CreatedAt) VALUES
        (?, ?, ?, NOW());
    """;

    public static final String UPDATE = """
        UPDATE services
        SET name = ?, description = ?, price = ?
        WHERE id = ?;
    """;
}
