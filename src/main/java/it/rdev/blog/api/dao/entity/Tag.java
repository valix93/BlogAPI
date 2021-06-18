package it.rdev.blog.api.dao.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tag")
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column
	private String titolo;
	@ManyToMany
	@JoinTable(name="articolotag",
				joinColumns = @JoinColumn(name="id_articolo"),
				inverseJoinColumns =  @JoinColumn(name="id_tag") )
    private Set<Articolo> articoli = new HashSet<>();
	
    
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
	public Set<Articolo> getArticoli() {
		return articoli;
	}

	public void setArticoli(Set<Articolo> articoli) {
		this.articoli = articoli;
	}
	
}