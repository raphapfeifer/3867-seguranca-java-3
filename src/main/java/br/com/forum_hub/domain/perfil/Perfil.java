package br.com.forum_hub.domain.perfil;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "perfil")
public class Perfil implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PerfilNomeEnum nome;

    @Override
    public String getAuthority() {
        return "ROLE_" + nome;
    }
}
