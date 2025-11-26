package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.payment;


import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.payment.PaymentUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound.PaymentNotFoundException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PaymentRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPayment.IPaymentRequestService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.CrudRequestServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ValidationUtils.validateStringNullOrBlank;

@Service
public class PaymentRequestServiceImpl extends CrudRequestServiceImpl<Payment, PaymentUpdateDTO, Long> implements IPaymentRequestService {

    private final PaymentRepository paymentRepository;

    public PaymentRequestServiceImpl(PaymentRepository paymentRepository) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment update(Long id, PaymentUpdateDTO updateDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found."));

        if (updateDTO.getName() != null) {
            validateStringNullOrBlank(updateDTO.getName());
            existingPayment.setName(updateDTO.getName());
        }

        return paymentRepository.save(existingPayment);
    }

}
