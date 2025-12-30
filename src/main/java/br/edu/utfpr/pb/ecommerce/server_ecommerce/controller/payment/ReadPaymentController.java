package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.payment;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.payment.iPaymentController.IReadPaymentController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment.IPayment.IPaymentResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
public class ReadPaymentController extends BaseSoftDeleteReadController<Payment, PaymentResponseDTO> implements IReadPaymentController {

    public ReadPaymentController(IPaymentResponseService paymentResponseService, ModelMapper modelMapper) {
        super(PaymentResponseDTO.class, paymentResponseService, modelMapper);
    }
}
