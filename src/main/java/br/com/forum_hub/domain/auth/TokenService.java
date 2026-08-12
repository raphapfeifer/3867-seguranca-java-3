package br.com.forum_hub.domain.auth;

import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${spring.user.password}")
    private String password;

    public String generateToken(Usuario usuario){
       try{
           Algorithm algorithm = Algorithm.HMAC256(password);
           return  JWT.create()
                   .withIssuer("Forum Hub")
                   .withSubject(usuario.getUsername())
                   .withExpiresAt(expires(30))
                   .sign(algorithm);
        }catch (JWTCreationException ex) {
           throw new RegraDeNegocioException("Error when try to generate token");
       }
    }

    private Instant expires(Integer minutes) {
        return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
    }

    public String verifyToken(String token) {
        DecodedJWT decoadedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Forum Hub")
                    .build();

            decoadedJWT = verifier.verify(token);
            return decoadedJWT.getSubject();
        } catch (JWTVerificationException ex) {
            throw new RegraDeNegocioException("An error with token validation");
        }
    }
}
