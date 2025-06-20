package nguyenthanhtung.datn.config.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class TrimmedAspec {

    // Pointcut bắt tất cả method trong package controller (và sub-package)
    @Pointcut("within(nguyenthanhtung.datn.controller..*)")
    public void controllerMethods() {}

    // Advice chạy trước khi method controller được gọi
    @Before("controllerMethods()")
    public void trimStringsInParams(JoinPoint joinPoint) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        if (args == null) return;

        for (Object arg : args) {
            if (arg == null) continue;
            trimFields(arg);
        }
    }

    private void trimFields(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Trimmed.class) && field.getType() == String.class) {
                field.setAccessible(true);
                String value = (String) field.get(obj);
                if (value != null) {
                    field.set(obj, value.trim());
                }
            }
        }
    }
}
