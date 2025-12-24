package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseRequestService;

public interface IAlertProductRequestService extends IBaseRequestService<AlertProduct, AlertProductRequestDTO, Long> {
    AlertProductResponseDTO createAlert(AlertProductRequestDTO request);
    AlertProductResponseDTO cancelAlert(Long id);
    AlertProductResponseDTO activateAlert(Long id);
    void syncOrphanAlerts(User user);
}
