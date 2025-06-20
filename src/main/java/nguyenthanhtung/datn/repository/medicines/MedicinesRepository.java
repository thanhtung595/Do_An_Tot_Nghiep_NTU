package nguyenthanhtung.datn.repository.medicines;

import java.util.List;
import java.util.Map;

public interface MedicinesRepository {
    List<Map<String, Object>> getMedicines();
    List<Map<String, Object>> getMedicinesInvoiceByIdPatient(int id);
    List<String> getListNameMedicinesInvoiceByIdPatient(int id);
}
