package com.generation.eatit.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioLogin {
	
	private Long Id;
	
	private String nome;
	
	private String usuario;
	
	private String senha;
	
	private String foto;
	
	private String token;
	
	private String tipo;

	public UsuarioLogin() {}

	public UsuarioLogin(Long id, String nome, String usuario, String senha, String foto, String tipo,String token) {
		Id = id;
		this.nome = nome;
		this.usuario = usuario;
		this.senha = senha;
		this.foto = foto;
		this.token = token;
		this.tipo = tipo;
	}
}
