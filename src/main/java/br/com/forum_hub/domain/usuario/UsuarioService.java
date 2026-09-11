package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.infra.email.EmailService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não foi encontrado"));
    }

    @Transactional
    public Usuario cadastrar(@Valid DadosCadastroUsuario dados) {
        Optional<Usuario> oldUser = usuarioRepository.findByEmailIgnoreCaseOrNomeUsuarioIgnoreCase(dados.email(), dados.nomeUsuario());

        if(oldUser.isPresent()){
            throw new RegraDeNegocioException("Usuário já existe na base");
        }

        var passwordEncoded = encoder.encode(dados.password());
        var usuario = new Usuario(dados, passwordEncoded);
        emailService.enviarEmailVerificacao(usuario);
        return usuarioRepository.save(usuario);
    }

    public void deletar(@NotBlank Long id) {
        usuarioRepository.deleteById(id);
    }

    public void verficarEmail(String codigo) {
        var usuario = usuarioRepository.findByToken(codigo).orElseThrow();
        usuario.verficar();
        usuarioRepository.saveAndFlush(usuario);
    }

    public Usuario getUsuario(String nomeUsuario) {
        return usuarioRepository.getByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não foi encontrado"));
    }

    public Usuario updateUser(@Valid DadosEdicaoUsuario dados,
                              Usuario usuario) {
        usuario.updateUser(dados);
        return usuarioRepository.saveAndFlush(usuario);
    }

    public void updatePassword(DadosAlteracaoSenha dados, Usuario logado) {
        if(!encoder.matches(dados.currentPassword(), logado.getPassword())){
            throw new RegraDeNegocioException("Senha digitada não confere com a senha atual");
        }

        if(!dados.newPassword().equals(dados.newPasswordConfirm())){
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        var passwordEncoded = encoder.encode(dados.newPassword());
        logado.updatePassword(passwordEncoded);
        usuarioRepository.saveAndFlush(logado);
    }

    public Usuario changeUserStatus(Long id, Boolean ativo) {
       Usuario usuario = usuarioRepository.findById(id)
               .orElseThrow(() -> new RegraDeNegocioException("Usuario não foi encontrado"));

       usuario.changeUserStatus(ativo);

       return usuarioRepository.saveAndFlush(usuario);
    }
}
