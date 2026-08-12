package br.com.forum_hub.domain.auth;

import jakarta.validation.constraints.NotBlank;

public record DadosLongin(@NotBlank String email,
                          @NotBlank String password) {


}
