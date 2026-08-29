package br.com.forum_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nomeCompleto,
        @NotBlank String nomeUsuario,
        String miniBiografia,
        String biografia
        ) {

}
