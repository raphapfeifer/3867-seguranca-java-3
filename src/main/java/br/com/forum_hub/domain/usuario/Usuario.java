package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
    private Boolean verificado;
    private String token;
    private LocalDateTime expiracaoToken;
    private Boolean ativo;

    public Usuario(){}

    public Usuario(@Valid DadosCadastroUsuario dados, String passwordEncoded) {
        this.nomeCompleto = dados.nomeCompleto();
        this.email = dados.email();
        this.senha = passwordEncoded;
        this.nomeUsuario = dados.nomeUsuario();
        this.biografia = dados.biografia();
        this.miniBiografia = dados.miniBiografia();
        this.verificado = false;
        this.token = UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(30);
        this.ativo = true;
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

    public String getToken() {
        return token;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void verficar() {
        if(expiracaoToken.isBefore(LocalDateTime.now())){
            throw new RegraDeNegocioException("link de verficação exepirou!");
        }
        this.verificado = true;
        this.token = null;
        this.expiracaoToken = null;
    }

    public Usuario updateUser(@Valid DadosEdicaoUsuario dados) {
        if(dados.nomeUsuario() != null){
            this.nomeUsuario = dados.nomeUsuario();
        }

        if(dados.biografia() != null){
            this.biografia = dados.biografia();
        }

        if(dados.miniBiografia() != null){
            this.miniBiografia = dados.miniBiografia();
        }

        return this;
    }

    public void updatePassword(String encodedPassword){
        this.senha = encodedPassword;
    }

    public void changeUserStatus(Boolean ativo) {
      this.ativo = ativo ? true : false;
    }
}
