package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment.IPayment;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface IPaymentRequestService extends IBaseSoftDeleteRequestService<Payment, PaymentUpdateDTO, Long> {
}
