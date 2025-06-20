package nguyenthanhtung.datn.repository.departments;

import java.util.List;
import java.util.Map;

public interface DepartmentsRepository {
    List<Map<String, Object>> getDepartments();
}
