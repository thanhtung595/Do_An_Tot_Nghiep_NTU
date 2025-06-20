package nguyenthanhtung.datn.constants.ConstantQuery.medicines;

public class ConstantQueryMedicines {

    public static final String SELECT_ALL = """
        SELECT
          id as id,
          id as idmedicines,
          name as name,
          type as unti
        FROM Medicines;
    """;

    public static final String GET_MEDICINES_INVOICE_BY_ID_PATIENT = """
        SELECT
          mi.id as id,
          m.id as idmedicines,
          m.name as name,
          m.type as unit
        FROM MedicinesInvoice mi
        INNER JOIN Invoice i
        ON mi.invoiceid = i.id
        INNER JOIN Medicines m
        ON mi.idmedicines = m.id
        WHERE i.patientid = ?;
    """;

    public static final String GET_MEDICINES_INVOICE_BY_ID_PATIENT_1 = """
        SELECT
          m.id as id,
          m.id as idmedicines,
          m.name as name,
          m.type as unit
        FROM MedicinesInvoice mi
        INNER JOIN Invoice i
        ON mi.invoiceid = i.id
        INNER JOIN Medicines m
        ON mi.idmedicines = m.id
        WHERE i.patientid = ?;
    """;

    public static final String GET_LIST_NAME_MEDICINES_INVOICE_BY_ID_PATIENT = """
        SELECT med.name
            FROM medicinesinvoice medin
            INNER JOIN medicines med
            ON medin.idmedicines = med.id
            WHERE medin.invoiceid = ?;
    """;
}
