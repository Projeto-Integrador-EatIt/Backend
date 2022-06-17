package com.generation.eatit.service;

import java.util.Optional;

import com.generation.eatit.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.eatit.model.Produto;
import com.generation.eatit.repository.CategoriaRepository;
import com.generation.eatit.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Optional<Produto> prodValid(Produto produto) {

        if (produtoRepository.findByNomeContainingIgnoreCase(produto.getNome()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto ja existe", null);

        if (produto.getCategoria().getId() == null ||
                !categoriaRepository.existsById(produto.getCategoria().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria nula ou inexistente", null);
        }

        return Optional.of(produtoRepository.save(produto));
    }

    public Optional<Produto> attProduto (Produto produto) {

        if(produtoRepository.findById(produto.getId()).isPresent()) {
            Optional<Produto> buscaproduto = produtoRepository.findByNomeContainingIgnoreCase(produto.getNome());

            if ((buscaproduto.isPresent()) && (buscaproduto.get().getId() != produto.getId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "produto ja existe", null);

            if (produto.getCategoria().getId() == null ||
                    !categoriaRepository.existsById(produto.getCategoria().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria nula ou inexistente", null);
            }
            return Optional.of(produtoRepository.save(produto));
        }
        return Optional.empty();
    }



}