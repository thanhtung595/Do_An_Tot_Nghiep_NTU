package nguyenthanhtung.datn.util;

import java.sql.Time;
import java.time.LocalTime;

public class TextUtils {

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static Time convertStringToTime(String str){
        return Time.valueOf(LocalTime.parse(str));
    }
}
