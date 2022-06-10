package com.generation.eatit.controller;

import com.generation.eatit.model.Produto;
import com.generation.eatit.repository.CategoriaRepository;
import com.generation.eatit.repository.ProdutoRepository;
import com.generation.eatit.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> getAll(){
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> findByID (@PathVariable Long id){
        return produtoRepository.findById(id)
                .map(r-> ResponseEntity.ok(r))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity <List<Produto>> findByNome(@PathVariable String nome){
       return ResponseEntity.ok(produtoRepository
               .findAllByNomeContainingIgnoreCase(nome));
    }

    @GetMapping("/maiorque/{valor}")
    public ResponseEntity<List<Produto>> findHigherValor(@PathVariable BigDecimal valor){
        return ResponseEntity.ok(produtoRepository
                .findByValorGreaterThanOrderByValor(valor));
    }

    @GetMapping("/menorque/{valor}")
    public ResponseEntity<List<Produto>> findLowerValor(@PathVariable BigDecimal valor){
        return ResponseEntity.ok(produtoRepository
                .findByValorLessThanOrderByValorDesc(valor));
    }

    @GetMapping("/valor/de{valorMenor}a{valorMaior}")
    public ResponseEntity<List<Produto>> findValorBetween(@PathVariable BigDecimal valorMenor, @PathVariable BigDecimal valorMaior){
        return ResponseEntity.ok(produtoRepository.findAllByValorBetween(valorMenor,valorMaior));
    }

    //Ajustar para não postar duas vezes o mesmo produto.
    @PostMapping
    public ResponseEntity<Produto> postProdutos(@Valid @RequestBody Produto produto){
    	if(produto.getCategoria().getId()==null||
    	        !categoriaRepository.existsById(produto.getCategoria().getId())){
    	            return ResponseEntity.badRequest().build();
    	        }
    	return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

        
    }

    @PutMapping
    public ResponseEntity<Produto> putProdutos(@Valid @RequestBody Produto produto){
        if(produto.getCategoria().getId()==null||
        !categoriaRepository.existsById(produto.getCategoria().getId())){
            return ResponseEntity.badRequest().build();
        }

        return produtoRepository.findById(produto.getId())
                .map(r-> ResponseEntity.ok(produtoRepository.save(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable Long id){
        return produtoRepository.findById(id)
                .map(r-> {
                    produtoRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
