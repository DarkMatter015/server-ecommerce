package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.security.dto;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserResponseDTO {

    private Long id;
    private String email;
    private String displayName;
    private Set<AuthorityResponseDTO> authorities;

    public SecurityUserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.authorities = new HashSet<>();
        for (GrantedAuthority authority: user.getAuthorities()) {
            authorities.add( new AuthorityResponseDTO(authority.getAuthority()) );
        }
    }

}