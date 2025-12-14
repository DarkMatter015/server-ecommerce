package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPasswordResetToken;

import java.util.Optional;

public interface IPasswordResetTokenService {
    Optional<String> createPasswordResetTokenForEmail(String email);
    boolean validatePasswordResetToken(String token);
    void changeUserPassword(String token, String newPassword);
}
