package br.com.forum_hub.controller;

import br.com.forum_hub.domain.perfil.DadosPerfil;
import br.com.forum_hub.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/registrar")
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
                                                          UriComponentsBuilder uriComponentsBuilder){

        var usuario = service.cadastrar(dados);
        var uri = uriComponentsBuilder.path("/{nomeUsuario}").buildAndExpand(usuario.getNomeUsuario()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @GetMapping("/get-usuarios")
    public ResponseEntity<List<DadosListagemUsuario>> getUsuarios(){
        List<DadosListagemUsuario> usuarios = service.getUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/get-usuario")
    public ResponseEntity<DadosListagemUsuario> getUsuario(@RequestParam String nomeUsuario){
        var usuario = service.getUsuario(nomeUsuario);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verficarEmail(@RequestParam String codigo){
        service.verficarEmail(codigo);
        return ResponseEntity.ok("Conta verficada com sucesso");
    }

    @PutMapping("/editar-perfil")
    public ResponseEntity<DadosListagemUsuario> updateUser(@RequestBody @Valid DadosEdicaoUsuario dados,
                                                           @AuthenticationPrincipal Usuario logado){
        var usuario = service.updateUser(dados,logado);
        return ResponseEntity.ok().body(new DadosListagemUsuario(usuario));
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid DadosAlteracaoSenha dados,
                                               @AuthenticationPrincipal Usuario logado){
        service.updatePassword(dados,logado);
        return ResponseEntity.ok("Senha alterada");
    }

    @PatchMapping("/desativar")
    public ResponseEntity<String> changeUserStatus(@RequestParam Long id, @RequestParam Boolean ativo){
        Usuario usuario = service.changeUserStatus(id,ativo);
        return ResponseEntity.ok("Status do usuário: " + (usuario.getAtivo() ? "Ativo" : "Desativado"));
    }

    @PatchMapping("adicionar-perfil/{id}")
    public ResponseEntity<String> adicionarPerfil(@PathVariable Long id ,@RequestBody @Valid DadosPerfil dados){
        var usuario = service.addProfile(id, dados);
        return ResponseEntity.ok("Perfil adicionado: " + usuario.getAuthorities());
    }

    @PatchMapping("remover-perfil/{id}")
    public ResponseEntity<DadosListagemUsuario> removerPerfil(@PathVariable Long id, @RequestBody @Valid DadosPerfil dados){
        var usuario = service.removeProfile(id,dados);
        return ResponseEntity.ok().body(new DadosListagemUsuario(usuario));
    }

    @DeleteMapping("/deletar-usuario")
    public ResponseEntity<String> deletar(@RequestParam Long id){
         service.deletar(id);
         return ResponseEntity.ok("Usuario deletado na base");
    }
}
