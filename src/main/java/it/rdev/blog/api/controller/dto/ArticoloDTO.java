package it.rdev.blog.api.controller.dto;

import java.util.HashSet;
import java.util.Set;

import it.rdev.blog.api.dao.entity.Categoria;
import it.rdev.blog.api.dao.entity.Tag;
import it.rdev.blog.api.dao.entity.User;

public class ArticoloDTO {
	
	private String titolo;
	private String sottotitolo;
	private java.sql.Timestamp data_creazione;
	private java.sql.Timestamp data_pubblicazione;
	private java.sql.Timestamp data_modifica;
	private Categoria categoria;
    private User autore;
    private Set<Tag> tags = new HashSet<>();
    
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getSottotitolo() {
		return sottotitolo;
	}
	public void setSottotitolo(String sottotitolo) {
		this.sottotitolo = sottotitolo;
	}
	public java.sql.Timestamp getData_creazione() {
		return data_creazione;
	}
	public void setData_creazione(java.sql.Timestamp data_creazione) {
		this.data_creazione = data_creazione;
	}
	public java.sql.Timestamp getData_pubblicazione() {
		return data_pubblicazione;
	}
	public void setData_pubblicazione(java.sql.Timestamp data_pubblicazione) {
		this.data_pubblicazione = data_pubblicazione;
	}
	public java.sql.Timestamp getData_modifica() {
		return data_modifica;
	}
	public void setData_modifica(java.sql.Timestamp data_modifica) {
		this.data_modifica = data_modifica;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public User getAutore() {
		return autore;
	}
	public void setAutore(User autore) {
		this.autore = autore;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

}
