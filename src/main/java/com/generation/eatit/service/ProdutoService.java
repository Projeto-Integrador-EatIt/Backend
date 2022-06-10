package com.generation.eatit.service;

import com.generation.eatit.model.Produto;
import com.generation.eatit.repository.CategoriaRepository;
import com.generation.eatit.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;


    public Optional<Produto> prodValid (Produto produto){

        if(produtoRepository.findById(produto.getId()).isPresent()){

            Optional <Produto> buscaproduto = produtoRepository.findByNomeContainingIgnoreCase(produto.getNome());

            if(produto.getNome() == buscaproduto.get().getNome() ||
                    produto.getCategoria().getId()==null||
                    !categoriaRepository.existsById(produto.getCategoria().getId()))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Produto ja existe",null);
            }

            return Optional.of(produtoRepository.save(produto));

        }
        return Optional.empty();
    }

}
