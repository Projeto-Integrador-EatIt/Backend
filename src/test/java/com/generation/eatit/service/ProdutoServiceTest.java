package com.generation.eatit.service;

import com.generation.eatit.model.Categoria;
import com.generation.eatit.model.Produto;
import com.generation.eatit.repository.CategoriaRepository;
import com.generation.eatit.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProdutoServiceTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @BeforeAll
    void start(){
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
        categoriaRepository.save(new Categoria(0L,"categoriateste","categoriateste"));

    }

    @Test
    @Order(1)
    @DisplayName("nega cadastro de produto com mesmo nome")
    void produtomesmonome(){
        var categoria = new Categoria(1L,"categoriateste","categoriateste");
        var produto= new Produto(0L,"Produtoteste", BigDecimal.valueOf(100),
                "produto para teste",10,null,categoria);
        produtoRepository.save(produto);

        Assertions.assertThrows(ResponseStatusException.class,()->produtoService.prodValid(produto));
    }

    @Test
    @Order(2)
    @DisplayName("nega cadastro de produto com categorias inexistentes")
    void produtocategoriainexistente(){
        var categoria = new Categoria(5L,"categoriateste","categoriateste");
        var produto= new Produto(0L,"Produtoteste", BigDecimal.valueOf(100),
                "produto para teste",10,null,categoria);

        Assertions.assertThrows(ResponseStatusException.class,()->produtoService.prodValid(produto));
    }

    @Test
    @Order(3)
    @DisplayName("nega cadastro de produto com categorias nullas")
    void produtocategoriainull(){
        var categoria = new Categoria(null,"categoriateste","categoriateste");
        var produto= new Produto(0L,"Produtoteste", BigDecimal.valueOf(100),
                "produto para teste",10,null,categoria);

        Assertions.assertThrows(ResponseStatusException.class,()->produtoService.prodValid(produto));
    }

    @Test
    @Order(4)
    @DisplayName("efetua cadastro de um produto")
    void cadastraproduto(){
        var categoria = new Categoria(1L,"categoriateste","categoriateste");
        var produto= new Produto(0L,"Produtoteste2", BigDecimal.valueOf(150),
                "produto para teste 2",30,null,categoria);

        produtoService.prodValid(produto);

        Assertions.assertTrue(produtoRepository.findByNomeContainingIgnoreCase("Produtoteste2").isPresent());
    }

    @Test
    @Order(5)
    @DisplayName("lança exception ao tentar atualizar com categoria nula")
    void attprod(){
        var categoria = new Categoria(null,"categoriateste","categoriateste");
        var produto= new Produto(0L,"Produtoteste", BigDecimal.valueOf(100),
                "produto para teste",10,null,categoria);

        Assertions.assertThrows(ResponseStatusException.class,()->produtoService.prodatt(produto));

    }
}