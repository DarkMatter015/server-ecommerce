package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.alertProduct;


import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.BaseReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.alertProduct.AlertProductResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.AlertProduct;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.alertProduct.IAlertProduct.IAlertProductResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("alerts")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Alerts Read", description = "Endpoints for reading alerts of available products")
public class ReadAlertProductController extends BaseReadController<AlertProduct, AlertProductResponseDTO, Long> {

    public ReadAlertProductController(IAlertProductResponseService service,
                                      ModelMapper modelMapper) {
        super(AlertProductResponseDTO.class, service, modelMapper);
    }
}
