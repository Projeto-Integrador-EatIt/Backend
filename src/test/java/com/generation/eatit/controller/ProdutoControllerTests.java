package com.generation.eatit.controller;

import com.generation.eatit.model.Categoria;
import com.generation.eatit.model.Produto;
import com.generation.eatit.repository.CategoriaRepository;
import com.generation.eatit.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProdutoControllerTests {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    void start(){
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
        categoriaRepository.save(new Categoria(0L,"Teste categoria","teste categoria"));

    }

    @Test
    @Order(1)
    @DisplayName("cadastra produto")
    public void devecadastrarproduto(){
        var categoria=new Categoria(1L,"Teste categoria","teste categoria");

        HttpEntity<Produto> corpoRequisição = new HttpEntity<Produto>
                (new Produto(null,"produto teste", BigDecimal.valueOf(10.99),"produto teste"
                        ,10,null,categoria));

        ResponseEntity<Produto> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos", HttpMethod.POST,corpoRequisição,Produto.class);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    @Order(2)
    @DisplayName("Badrequest produto")
    public void deveretornarBadrequest(){
        var categoria=new Categoria(3L,"Teste categoria","teste categoria");

        HttpEntity<Produto> corpoRequisição = new HttpEntity<Produto>
                (new Produto(null,"produto teste", BigDecimal.valueOf(10.99),"produto teste"
                        ,10,null,categoria));

        ResponseEntity<Produto> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos", HttpMethod.POST,corpoRequisição,Produto.class);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("lista produtos")
    public void develistar(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Busca produtos por id")
    public void devebuscaID(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/1",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Busca produtos por nome")
    public void devebuscapornome(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/nome/produto",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("Busca produtos por valores menores")
    public void buscamenorque(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/menorque/10",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(7)
    @DisplayName("Busca produtos por valores menores")
    public void buscamaiorque(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/maiorque/10",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    @Order(8)
    @DisplayName("Busca produtos com valores entre dois valores")
    public void buscaentre(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/valor/de10a100",HttpMethod.GET,null,String.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("atualiza produto")
    public void Atualizaprod(){
        var categoria=new Categoria(1L,"Teste categoria","teste categoria");

        HttpEntity<Produto> corpoRequisição = new HttpEntity<Produto>
                (new Produto(1L,"produto teste atualizado", BigDecimal.valueOf(10.99),
                        "produto teste atualizado",10,null,categoria));

        ResponseEntity<Produto> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos", HttpMethod.PUT,corpoRequisição,Produto.class);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
        assertEquals(corpoRequisição.getBody().getNome(),produtoRepository.findById(1L).get().getNome());
    }

    @Test
    @Order(10)
    @DisplayName("mostra erro ao tentar atualizar com categoria inexistente")
    public void Atualizaprodnull(){
        var categoria=new Categoria(5L,"Teste categoria","teste categoria");


        HttpEntity<Produto> corpoRequisição = new HttpEntity<Produto>
                (new Produto(1L,"produto teste teste atualizado", BigDecimal.valueOf(10.99),
                        "produto teste atualizado",10,null,categoria));

        ResponseEntity<Produto> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos", HttpMethod.PUT,corpoRequisição,Produto.class);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertFalse(produtoRepository.findByNomeContainingIgnoreCase("Produto teste teste atualizado").isPresent());
    }


    @Test
    @Order(11)
    @DisplayName("deleta Produto")
    public void deletarproduto(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/1",HttpMethod.DELETE,null,String.class);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    @Test
    @Order(12)
    @DisplayName("exibe notfound ao deletar algo que nao existe")
    public void deletarprodutoinexistente(){

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root","root")
                .exchange("/produtos/1",HttpMethod.DELETE,null,String.class);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }


}
