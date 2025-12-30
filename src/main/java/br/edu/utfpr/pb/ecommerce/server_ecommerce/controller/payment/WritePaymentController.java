package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.payment;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteWriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.payment.iPaymentController.IWritePaymentController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment.IPayment.IPaymentRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
public class WritePaymentController extends BaseSoftDeleteWriteController<Payment, PaymentRequestDTO, PaymentResponseDTO, PaymentUpdateDTO> implements IWritePaymentController {
    public WritePaymentController(IPaymentRequestService paymentRequestService, ModelMapper modelMapper) {
        super(paymentRequestService, modelMapper, Payment.class, PaymentResponseDTO.class);
    }
}
