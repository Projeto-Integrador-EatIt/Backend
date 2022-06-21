package com.generation.eatit.controller;

import com.generation.eatit.model.Categoria;
import com.generation.eatit.model.Produto;
import com.generation.eatit.repository.CategoriaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoriaControllerTests {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    public void start(){
        categoriaRepository.deleteAll();

    }

    @Test
    @Order(1)
    @DisplayName("cadastra categoria")
    public void devecadastrarproduto(){

        HttpEntity<Categoria> corpoRequisição = new HttpEntity<Categoria>(
                (new Categoria(null,"categoria teste","categoria para testes")));

        ResponseEntity<Categoria> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias", HttpMethod.POST,corpoRequisição,Categoria.class);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    @Order(2)
    @DisplayName("lista categorias")
    public void develistar(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Busca categorias por id")
    public void devebuscarID(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias/1",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Busca categorias por tipo")
    public void devebuscaportipo(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias/tipo/categoria",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("atualiza categoria")
    public void Atualizaprod(){

        HttpEntity<Categoria> corpoRequisição = new HttpEntity<Categoria>(
                (new Categoria(1L,"categoria teste atualizada","categoria para testes atualizada")));

        ResponseEntity<Produto> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias", HttpMethod.PUT,corpoRequisição,Produto.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(categoriaRepository.findById(1L).get().getTipo(),"categoria teste atualizada");
    }

    @Test
    @Order(7)
    @DisplayName("deleta Categorias")
    public void deletarproduto(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias/1",HttpMethod.DELETE,null,String.class);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    @Test
    @Order(8)
    @DisplayName("exibe notfound ao deletar algo que nao existe")
    public void deletarprodutoinexistente(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/categorias/1",HttpMethod.DELETE,null,String.class);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}
