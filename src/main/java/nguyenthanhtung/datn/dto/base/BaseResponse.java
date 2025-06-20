package nguyenthanhtung.datn.dto.base;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseResponse {
    private String subjectFunction;
    private int httpStatusCode;
    private String status;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public void addData(String key, Object value) {
        data.put(key, value);
    }
}
