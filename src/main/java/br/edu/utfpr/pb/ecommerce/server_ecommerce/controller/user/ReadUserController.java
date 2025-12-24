package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.BaseSoftDeleteReadController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.IUser.IUserResponseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("users")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "User Read", description = "Endpoints for reading users")
public class ReadUserController extends BaseSoftDeleteReadController<User, UserResponseDTO> {

    public ReadUserController(IUserResponseService service, ModelMapper modelMapper) {
        super(UserResponseDTO.class, service, modelMapper);
    }
}
