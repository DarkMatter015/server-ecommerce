package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment;


import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PaymentRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.FilterManager;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPayment.IPaymentResponseService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudResponseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PaymentResponseServiceImpl extends CrudResponseServiceImpl<Payment, Long> implements IPaymentResponseService {

    public PaymentResponseServiceImpl(PaymentRepository paymentRepository, FilterManager filterManager, AuthService authService) {
        super(paymentRepository, filterManager, authService);
    }

}
