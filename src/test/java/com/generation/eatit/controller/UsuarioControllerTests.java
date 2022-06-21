package com.generation.eatit.controller;

import com.generation.eatit.model.Usuario;
import com.generation.eatit.model.UsuarioLogin;
import com.generation.eatit.repository.UsuarioRepository;
import com.generation.eatit.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTests {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){

        usuarioRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Cadastrar Um Usuário")
    public void deveCriarUmUsuario() {

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

        ResponseEntity<Usuario> resposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
        assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
    }

    @Test
    @Order(2)
    @DisplayName("Não deve permitir duplicação do Usuário")
    public void naoDeveDuplicarUsuario() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

        ResponseEntity<Usuario> resposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("Alterar um Usuário")
    public void deveAtualizarUmUsuario() {

        Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
                "Juliana Andrews", "juliana_andrews@email.com.br",
                "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));

        Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
                "Juliana Andrews Ramos", "juliana_ramos@email.com.br",
                "juliana123", "https://i.imgur.com/yDRVeK7.jpg");

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
        assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
    }

    @Test
    @Order(4)
    @DisplayName("Listar todos os Usuários")
    public void deveMostrarTodosUsuarios() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Sabrina Sanches", "sabrina_sanches@email.com.br",
                "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Ricardo Marques", "ricardo_marques@email.com.br",
                "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Logar usuario")
    public void develogarusuario(){
        HttpEntity<UsuarioLogin> requisicao =
                new HttpEntity<UsuarioLogin>(new UsuarioLogin(null,null,"ricardo_marques@email.com.br",
                        "ricardo123",null,null));

        ResponseEntity<String> response= testRestTemplate
                .withBasicAuth(requisicao.getBody().getUsuario(),requisicao.getBody().getSenha())
                .exchange("/usuarios/logar",HttpMethod.POST,requisicao,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }


    @Test
    @Order(6)
    @DisplayName("trazer usuario por ID")
    public void findId(){

        HttpEntity<Usuario> requisicao =
                new HttpEntity<Usuario>(new Usuario(null,null,"ricardo_marques@email.com.br",
                        "ricardo123",null));

        ResponseEntity<String> response= testRestTemplate
                .withBasicAuth(requisicao.getBody().getUsuario(),requisicao.getBody().getSenha())
                .exchange("/usuarios/1",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }


}
