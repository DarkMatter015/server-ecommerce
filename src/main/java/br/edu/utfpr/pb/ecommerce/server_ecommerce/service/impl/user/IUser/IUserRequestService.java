package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.user.IUser;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ChangePasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface IUserRequestService extends IBaseSoftDeleteRequestService<User, UserUpdateDTO, Long> {
    User createUser(UserRequestDTO userRequestDTO);
    void changePassword(ChangePasswordDTO dto);
}
