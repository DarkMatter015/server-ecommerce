package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.PasswordResetToken;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password.IncorrectPasswordException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password.InvalidTokenException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PasswordResetTokenRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ForgetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto.password.ResetPasswordDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.PasswordResetToken.IPasswordResetToken.IPasswordResetTokenService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetTokenServiceImpl implements IPasswordResetTokenService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.reset.token.expiration.minutes:15}")
    private int tokenExpirationMinutes;

    @Override
    @Transactional
    public void createPasswordResetTokenForEmail(ForgetPasswordDTO dto) {
        log.info("Processing password reset request for anonymized email.");
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

        if (userOptional.isEmpty()) {
            log.warn("Password reset attempt for non-existent email: {}", dto.getEmail());
            return;
        }

        User user = userOptional.get();

        PasswordResetToken myToken = tokenRepository.findByUser(user)
                .orElse(new PasswordResetToken());

        myToken.setToken(UUID.randomUUID().toString());
        myToken.setUser(user);
        myToken.setExpiryDate(tokenExpirationMinutes);

        tokenRepository.save(myToken);

        emailService.sendResetPasswordEmail(user, myToken);
        log.info("Recovery token generated and sent for user ID: {}", user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public void validatePasswordResetToken(String token) {
        getValidTokenOrThrow(token);
    }

    @Override
    @Transactional
    public void changeUserPassword(ResetPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IncorrectPasswordException(ErrorCode.PASSWORD_MISMATCH);
        }

        PasswordResetToken passToken = getValidTokenOrThrow(dto.getToken());
        User user = passToken.getUser();

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IncorrectPasswordException(ErrorCode.PASSWORD_SAME);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(passToken);
        log.info("Password changed successfully for user ID: {}", user.getId());
    }

    private PasswordResetToken getValidTokenOrThrow(String token) {
        if (token == null || token.isBlank())
            throw new InvalidTokenException(ErrorCode.TOKEN_INVALID);

        PasswordResetToken passToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Attempted to use a non-existent token: {}", token);
                    return new InvalidTokenException(ErrorCode.TOKEN_INVALID);
                });

        if (passToken.isTokenExpired()) {
            log.warn("Attempted to use an expired token. User ID: {}", passToken.getUser().getId());
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED);
        }

        return passToken;
    }
}
