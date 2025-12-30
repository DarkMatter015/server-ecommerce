package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.softDeleteController.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.user.iUserController.IReadUserController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.IUser.IUserResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("users")
public class ReadUserController extends BaseSoftDeleteReadController<User, UserResponseDTO> implements IReadUserController {

    public ReadUserController(IUserResponseService service, ModelMapper modelMapper) {
        super(UserResponseDTO.class, service, modelMapper);
    }
}
