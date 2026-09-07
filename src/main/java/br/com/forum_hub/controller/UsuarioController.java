package br.com.forum_hub.controller;

import br.com.forum_hub.domain.usuario.DadosCadastroUsuario;
import br.com.forum_hub.domain.usuario.DadosListagemUsuario;
import br.com.forum_hub.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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


    @DeleteMapping("/deletar-usuario")
    public ResponseEntity<String> deletar(@RequestParam Long id){
         service.deletar(id);
         return ResponseEntity.ok("Usuario deletado na base");
    }
}
