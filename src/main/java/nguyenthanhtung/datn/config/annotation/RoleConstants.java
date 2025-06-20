package nguyenthanhtung.datn.config.annotation;

import nguyenthanhtung.datn.constants.ContantApplication;
import org.springframework.stereotype.Component;

@Component("roleConst")
public class RoleConstants {

    public String getPatientRole() {
        return ContantApplication.HasRolePatient;
    }

    public String getDoctorRole() {
        return ContantApplication.HasRoleDoctor;
    }

    public String getAdminRole() {
        return ContantApplication.HasRoleAdmin;
    }
}
