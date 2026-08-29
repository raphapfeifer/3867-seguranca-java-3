package br.com.forum_hub.domain.usuario;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String nomeUsuario;
    private String biografia;
    private String miniBiografia;

    public Usuario(){}

    public Usuario(@Valid DadosCadastroUsuario dados, String passwordEncoded) {
        this.nomeCompleto = dados.nomeCompleto();
        this.email = dados.email();
        this.senha = passwordEncoded;
        this.nomeUsuario = dados.nomeUsuario();
        this.biografia = dados.biografia();
        this.miniBiografia = dados.miniBiografia();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getMiniBiografia() {
        return miniBiografia;
    }

    public Long getId() {
        return id;
    }
}
