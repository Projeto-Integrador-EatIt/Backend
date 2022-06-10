package com.generation.eatit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	public Optional<Produto> prodValid (Produto produto){

        Optional <Produto> buscaproduto = produtoRepository.findByNomeContainingIgnoreCase(produto.getNome());

        if(produto.getNome() == buscaproduto.get().getNome()||
                produto.getCategoria().getId()==null||
                !categoriaRepository.existsById(produto.getCategoria().getId()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Produto ja existe",null);
        }

        return Optional.of(produto);

                ///Optional.of(produtoRepository.save(produto));

}
}