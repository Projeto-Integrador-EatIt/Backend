package com.generation.eatit.service;

import com.generation.eatit.model.Usuario;
import com.generation.eatit.repository.UsuarioRepository;

import java.util.Base64;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            return Optional.empty();
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        return Optional.of(usuarioRepository.save(usuario));
    }


    public Optional<Usuario> atualizarUsuario(Usuario usuario) {
        if(usuarioRepository.findById(usuario.getId()).isPresent()){

            Optional<Usuario> buscausuario = usuarioRepository.findByUsuario(usuario.getUsuario());

            if (buscausuario.isPresent() && buscausuario.get().getId() != usuario.getId())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"usuario ja existe",null);

            usuario.setSenha(criptografarSenha(usuario.getSenha()));
            return Optional.ofNullable(usuarioRepository.save(usuario));

        }
        return Optional.empty();
    }

    public Optional<UsuarioLoginModel> autenticarUsuario(Optional<UsuarioLoginModel> usuarioLogin) {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
        if (usuario.isPresent()) {
            if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
                usuarioLogin.get().setSenha(usuario.get().getSenha());
                return usuarioLogin;
            }
        }
        return Optional.empty();
    }


    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaDigitada, senhaBanco);
    }


    private String gerarBasicToken(String usuario, String senha) {
        String token = usuario + ":" + senha;
        byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(tokenBase64);

    }
}