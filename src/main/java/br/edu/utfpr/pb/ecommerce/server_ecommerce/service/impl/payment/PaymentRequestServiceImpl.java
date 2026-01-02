package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment;


import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PaymentRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment.IPayment.IPaymentRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseSoftDeleteRequestServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestServiceImpl extends BaseSoftDeleteRequestServiceImpl<Payment, PaymentUpdateDTO> implements IPaymentRequestService {

    public PaymentRequestServiceImpl(PaymentRepository paymentRepository,
                                     PaymentResponseServiceImpl paymentResponseService) {
        super(paymentRepository, paymentResponseService);
    }

}
