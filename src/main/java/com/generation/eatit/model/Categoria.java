package com.generation.eatit.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_categorias")
public class Categoria {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O tipo é obrigatorio e não pode ser com espaços em branco)")
	@Size(min = 3, max = 255, message = "tipo deve conter no min 5 caracteres e no máximo 255")
	private String tipo;
	
	@NotBlank(message = "A descricao é obrigatorio e não pode ser com espaços em branco)")
	@Size(min = 3, max = 255, message = "A descricao deve conter no min 5 caracteres e no máximo 255")
	private String descricao;
	
	@OneToMany(mappedBy = "categoria", cascade =CascadeType.REMOVE)
	@JsonIgnoreProperties("categoria")
	private List <Produto> produto;

	public Categoria() {}

	public Categoria(Long id, String tipo, String descricao) {
		this.id = id;
		this.tipo = tipo;
		this.descricao = descricao;
	}

//Colocar List no relacionamento de produtos.
}
