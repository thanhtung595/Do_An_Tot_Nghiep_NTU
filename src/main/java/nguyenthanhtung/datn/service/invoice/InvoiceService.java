package nguyenthanhtung.datn.service.invoice;

import nguyenthanhtung.datn.dto.base.BaseResponse;

public interface InvoiceService {
    BaseResponse getInvoiceById(int id);
    BaseResponse getInvoiceByIdUser();
    BaseResponse getInvoiceByIdUserAndInvoice(int idInvoice);

}
