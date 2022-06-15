package com.generation.eatit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name = "tb_usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 100)
	private String nome;
	
	@NotNull
	@Email(message = "Digite um e-mail")
	private String usuario;
	
	@NotNull
	@Size(min = 8, message = "Digite uma senha com no mínimo 8 caracteres")
	private String senha;
	
	private String foto;

}
