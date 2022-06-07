package com.generation.eatit.repository;

import com.generation.eatit.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Long> {


    public List<Produto> findAllByNomeContainingIgnoreCase(@Param("nome")String nome);

    public List<Produto> findByValorGreaterThanOrderByValor(@Param("valor") BigDecimal valor);

    public List<Produto> findByValorLessThanOrderByValorDesc(@Param("valor")BigDecimal valor);

    public List<Produto> findAllByValorBetween(BigDecimal valorMenor, BigDecimal valorMaior);



}
