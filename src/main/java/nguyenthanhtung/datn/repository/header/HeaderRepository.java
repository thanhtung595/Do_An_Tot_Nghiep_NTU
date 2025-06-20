package nguyenthanhtung.datn.repository.header;

import java.util.List;
import java.util.Map;

public interface HeaderRepository {
    List<Map<String, Object>> getHeader();
    List<Map<String, Object>> getHeaderAdmin();

}
