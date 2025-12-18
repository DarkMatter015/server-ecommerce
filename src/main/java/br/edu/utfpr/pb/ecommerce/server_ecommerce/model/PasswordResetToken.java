package br.edu.utfpr.pb.ecommerce.server_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_password_reset_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 36, max = 36)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private User user;

    @NotNull
    private LocalDateTime expiryDate;

    public void setExpiryDate(int minutes){
        this.expiryDate = LocalDateTime.now().plusMinutes(minutes);
    }

    public boolean isTokenExpired() {
        return this.expiryDate.isBefore(LocalDateTime.now());
    }

}
