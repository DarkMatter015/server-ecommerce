package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.AlertProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.BaseResponseServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductResponseService;
import org.springframework.stereotype.Service;

@Service
public class AlertProductResponseService extends BaseResponseServiceImpl<AlertProduct, Long> implements IAlertProductResponseService {

    public AlertProductResponseService(AlertProductRepository repository, AuthService authService) {
        super(repository, authService);
    }
}
