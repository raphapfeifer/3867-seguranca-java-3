package br.com.forum_hub.controller;

import br.com.forum_hub.domain.auth.DadosLongin;
import br.com.forum_hub.domain.auth.DadosToken;
import br.com.forum_hub.domain.auth.TokenService;
import br.com.forum_hub.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosToken> login(@Valid @RequestBody DadosLongin dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String token = tokenService.generateToken((Usuario) authentication.getPrincipal());
        String refreshToken = tokenService.generateRefreshToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosToken(token,refreshToken));
    }
}
