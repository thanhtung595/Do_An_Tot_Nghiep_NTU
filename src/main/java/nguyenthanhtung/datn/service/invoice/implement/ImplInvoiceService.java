package nguyenthanhtung.datn.service.invoice.implement;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestThreadLocalContext;
import nguyenthanhtung.datn.config.requestThreadLocal.RequestUserThreadLocal;
import nguyenthanhtung.datn.constants.ContantApplication;
import nguyenthanhtung.datn.dto.base.BaseResponse;
import nguyenthanhtung.datn.repository.invoice.InvoiceRepository;
import nguyenthanhtung.datn.repository.services.ServicesRepository;
import nguyenthanhtung.datn.service.invoice.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImplInvoiceService implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ServicesRepository servicesRepository;

    @Override
    public BaseResponse getInvoiceById(int id) {
        BaseResponse response = new BaseResponse();
        try {
            List<Map<String, Object>> data = invoiceRepository.getInvoiceById(id);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("invoices", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse getInvoiceByIdUser() {
        BaseResponse response = new BaseResponse();

        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();

            List<Map<String, Object>> data = invoiceRepository.getInvoiceByIdUser(currentUser.getUserId());

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("invoices", data);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResponse getInvoiceByIdUserAndInvoice(int idInvoice) {
        BaseResponse response = new BaseResponse();

        try {
            RequestUserThreadLocal currentUser = RequestThreadLocalContext.get();

            Map<String, Object> data = invoiceRepository.getInvoiceByIdUserAndInvoice(currentUser.getUserId(), idInvoice);
            int patientid = (int) data.get("patientid");
            List<Map<String, Object>> getListServiceInvoiceByIdPatient = servicesRepository.getListServiceInvoiceByIdPatient(patientid);

            response.setHttpStatusCode(ContantApplication.HttpStatus_OK);
            response.setStatus(ContantApplication.StatusSuccess);
            response.setMessage(ContantApplication.MessageSuccess);
            response.addData("invoices", data);
            response.addData("service", getListServiceInvoiceByIdPatient);
        } catch (Exception e) {
            response.setHttpStatusCode(ContantApplication.HttpStatus_Internal_Server_Error);
            response.setStatus(ContantApplication.StatusErrorBugException);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
