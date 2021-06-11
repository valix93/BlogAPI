package it.rdev.blog.api.dao.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "articolo")
public class Articolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column
	private String titolo;
	@Column
	private String sottotitolo;
	@Column
	private java.sql.Timestamp data_creazione;
	@Column
	private java.sql.Timestamp data_pubblicazione;
	@Column
	private java.sql.Timestamp data_modifica;
	@ManyToOne
    @JoinColumn(name = "categoria")
	private Categoria categoria;
	@ManyToOne
    @JoinColumn(name = "autore")
    private User autore;
    @ManyToMany(mappedBy = "articoli", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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