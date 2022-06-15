package com.generation.eatit.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_produtos")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 5, max = 255, message = "O atributo nome deve conter no minimo 5 e no maximo 255 caracteres")
	private String nome;
	
	private BigDecimal valor;
	
	@NotBlank
	@Size(min = 10, max = 500, message = "O atributo descricao deve conter no minimo 10 e no maximo 500 caracteres")
	private String descricao;
	
	private double peso;
	
	private String foto;
	
	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Categoria categoria;
}
