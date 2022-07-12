package com.generation.eatit.service;

import com.generation.eatit.model.Usuario;
import com.generation.eatit.model.UsuarioLogin;
import com.generation.eatit.repository.UsuarioRepository;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.Charset;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @BeforeAll
    public void start() {
        usuarioRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("cadastrar usuario")
    public void cadastrarusuario(){

        var usuario= new Usuario(0L,"Nestor","nestor@gmail.com","123456789",null);

        var responseUsuario=usuarioService.cadastrarUsuario(usuario);

        Optional<Usuario> responseUsuario2 = Optional.of(usuario);

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            responseUsuario2 = Optional.empty();
        }

        Assertions.assertTrue(usuarioRepository.findByUsuario("nestor@gmail.com").isPresent());
        Assertions.assertEquals(Optional.empty(),responseUsuario2);

    }

    @Test
    @Order(2)
    @DisplayName("atualizar usuario")
    public void attUsuario(){

        var usuario1= new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278",
                "https://i.imgur.com/FETvs2O.jpg");
        usuarioRepository.save(usuario1);

        var usuariofalse= new Usuario(5L,"Nestor","nestor@gmail.com","123456789",null);
        var usuariofalse1= new Usuario(2L,"Nestor","nestor@gmail.com","123456789",null);
        var usuarioCorrect= new Usuario(2L,"Vinicius","vinicius@email.com.br","123456789",null);

        usuarioService.atualizarUsuario(usuarioCorrect);

        Assertions.assertEquals(Optional.empty(),usuarioService.atualizarUsuario(usuariofalse));
        Assertions.assertThrows(ResponseStatusException.class,()->usuarioService.atualizarUsuario(usuariofalse1));
        Assertions.assertEquals(usuarioCorrect.getUsuario(),usuarioRepository.findByUsuario("vinicius@email.com.br").get().getUsuario());

    }

    @Test
    @Order(3)
    @DisplayName("autenticar usuario")
    public void autenticar(){
        var usuariologin = Optional.of(new UsuarioLogin(null,
                null,"nestor@gmail.com",
                "123456789",null,null, null));
        var usuariologin2 = Optional.of(new UsuarioLogin(null,
                null,"nestor@gmail.com",
                "123456",null,null,null));
        var usuariologin3 = Optional.of(new UsuarioLogin(null,
                null,"nestora@gmail.com",
                "123456789",null,null, null));

        var autenticar = usuarioService.autenticarUsuario(usuariologin);
        var autenticar1 = usuarioService.autenticarUsuario(usuariologin2);

        Assertions.assertTrue(autenticar.isPresent());
        Assertions.assertTrue(autenticar1.isEmpty());
        Assertions.assertTrue(usuarioService.autenticarUsuario(usuariologin3).isEmpty());
    }
}