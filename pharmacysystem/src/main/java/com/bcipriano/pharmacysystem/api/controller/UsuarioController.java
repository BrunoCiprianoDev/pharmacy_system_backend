package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.CredenciaisDTO;
import com.bcipriano.pharmacysystem.api.dto.TokenDTO;
import com.bcipriano.pharmacysystem.exception.InvalidPasswordException;
import com.bcipriano.pharmacysystem.model.entity.Usuario;
import com.bcipriano.pharmacysystem.security.JwtService;
import com.bcipriano.pharmacysystem.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody Usuario usuario ){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        System.out.println(usuario.isAdmin());
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);

            String rule = usuarioAutenticado
                    .getAuthorities()
                    .toArray()[0]
                    .toString()
                    .equals("ROLE_ADMIN") ? "manager" : "employee";

            System.out.println(usuario);

            return new TokenDTO(usuario.getLogin(), token, rule);
        } catch (UsernameNotFoundException | InvalidPasswordException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}