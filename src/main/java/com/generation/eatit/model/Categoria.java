package com.generation.eatit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_categorias")
public class Categoria {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O título é obrigatorio e não pode ser co espaços em branco)")
	@Size(min = 3, max = 255, message = "título deve conter no min 5 caracteres e no máximo 255")
	private String tipo;
	
	@NotBlank(message = "O título é obrigatorio e não pode ser co espaços em branco)")
	@Size(min = 3, max = 255, message = "título deve conter no min 5 caracteres e no máximo 255")
	private String descricao;
	
	

}
