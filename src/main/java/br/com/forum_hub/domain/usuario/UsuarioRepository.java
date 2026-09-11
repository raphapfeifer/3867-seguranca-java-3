package br.com.forum_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrue(String email);

    Optional<Usuario> findByEmailIgnoreCaseOrNomeUsuarioIgnoreCase(String email, String nomeUsuario);

    Optional<Usuario> findByToken(String codigo);

    Optional<Usuario> getByNomeUsuario(String nomeUsuario);

}
