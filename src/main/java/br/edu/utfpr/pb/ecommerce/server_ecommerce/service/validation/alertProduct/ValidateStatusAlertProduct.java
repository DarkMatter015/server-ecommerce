package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.enums.AlertProductStatus;
import org.springframework.stereotype.Component;

@Component
public class ValidateStatusAlertProduct implements IValidateAlertProduct {
    @Override
    public void validate(AlertProduct alertProduct) {
        if (alertProduct.getStatus().equals(AlertProductStatus.SENT)) throw new BusinessException(ErrorCode.ALERT_PRODUCT_ALREADY_SENT);
        if (alertProduct.getStatus().equals(AlertProductStatus.FAILED)) throw new BusinessException(ErrorCode.ALERT_PRODUCT_FAILED);
    }
}
