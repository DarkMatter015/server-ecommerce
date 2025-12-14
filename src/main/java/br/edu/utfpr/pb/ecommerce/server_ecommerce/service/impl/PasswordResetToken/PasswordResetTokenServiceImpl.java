package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.PasswordResetToken;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.PasswordResetToken;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.PasswordResetTokenRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.UserRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IPasswordResetToken.IPasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements IPasswordResetTokenService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    // Defina isso no seu application.properties: app.reset.token.expiration.minutes=60
    @Value("${app.reset.token.expiration.minutes:15}")
    private int tokenExpirationMinutes;

    /**
     * Passo 1: Solicitação.
     * Retorna Optional<String> para testes, mas em produção deve disparar um evento de e-mail.
     * @return O token gerado (apenas para seu teste no back) ou Empty se usuário não existir.
     */
    @Override
    @Transactional
    public Optional<String> createPasswordResetTokenForEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // SILENT FAILURE: Retorna empty, mas o Controller deve responder 200 OK.
            // Isso previne que atacantes descubram quais e-mails existem no banco.
            return Optional.empty();
        }

        User user = userOptional.get();

        // LIMPEZA CRÍTICA: Remove tokens antigos deste usuário para evitar múltiplos links válidos.
        tokenRepository.deleteAllByUser(user);

        // Geração e Configuração
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(tokenExpirationMinutes);

        tokenRepository.save(myToken);

        // TODO: Aqui você chamaria emailService.sendResetLink(email, token);
        return Optional.of(token);
    }

    /**
     * Passo 2: Validação (Ao clicar no link).
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passTokenOpt = tokenRepository.findByToken(token);

        if (passTokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken passToken = passTokenOpt.get();

        return !passToken.isTokenExpired();
    }

    /**
     * Passo 3: Alteração da Senha.
     */
    @Override
    @Transactional
    public void changeUserPassword(String token, String newPassword) {
        Optional<PasswordResetToken> passTokenOpt = tokenRepository.findByToken(token);

        if (passTokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Token inválido.");
        }

        PasswordResetToken passToken = passTokenOpt.get();

        // Double-check na expiração (caso o usuário demore entre validar e enviar o form)
        if (passToken.isTokenExpired()) {
            throw new IllegalArgumentException("Token expirado.");
        }

        User user = passToken.getUser();

        // Criptografia obrigatória
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // IMPORTANTE: Invalida o token imediatamente após o uso (One-Time Use)
        tokenRepository.delete(passToken);
    }
}
