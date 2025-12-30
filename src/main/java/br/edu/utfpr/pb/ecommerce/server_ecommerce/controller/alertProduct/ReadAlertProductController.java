package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct;


import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.base.BaseReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct.iAlertProductController.IReadAlertProductController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("alerts")
public class ReadAlertProductController extends BaseReadController<AlertProduct, AlertProductResponseDTO, Long> implements IReadAlertProductController {

    public ReadAlertProductController(IAlertProductResponseService service,
                                      ModelMapper modelMapper) {
        super(AlertProductResponseDTO.class, service, modelMapper);
    }
}
