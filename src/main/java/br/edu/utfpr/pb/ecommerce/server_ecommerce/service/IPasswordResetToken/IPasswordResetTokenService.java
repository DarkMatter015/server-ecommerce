package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPasswordResetToken;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ForgetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ResetPasswordDTO;

public interface IPasswordResetTokenService {
    void createPasswordResetTokenForEmail(ForgetPasswordDTO dto);
    void validatePasswordResetToken(String token);
    void changeUserPassword(ResetPasswordDTO dto);
}
