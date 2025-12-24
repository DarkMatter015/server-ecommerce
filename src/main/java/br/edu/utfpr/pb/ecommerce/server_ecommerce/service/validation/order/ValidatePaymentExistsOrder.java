package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.PaymentNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatePaymentExistsOrder implements IValidationOrder {

    private final PaymentRepository paymentRepository;

    @Override
    public void validate(OrderRequestDTO orderRequestDTO) {
        if (!paymentRepository.existsById(orderRequestDTO.getPaymentId())) {
            throw new PaymentNotFoundException("Payment method not found with id: " + orderRequestDTO.getPaymentId());
        }
    }
}
