package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.payment;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.ReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPayment.IPaymentResponseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
@Tag(name = "Payment Read", description = "Endpoints for reading payments")
public class ReadPaymentController extends ReadController<Payment, PaymentResponseDTO, Long> {

    public ReadPaymentController(IPaymentResponseService paymentResponseService, ModelMapper modelMapper) {
        super(PaymentResponseDTO.class, paymentResponseService, modelMapper);
    }
}
