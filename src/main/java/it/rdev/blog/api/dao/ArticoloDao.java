package it.rdev.blog.api.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.rdev.blog.api.dao.entity.Articolo;


@Repository
public interface ArticoloDao extends CrudRepository<Articolo, Integer> {
	
	Articolo findByTitolo(String titolo);
	Articolo findById(long id);
	
	@Query(value = "SELECT * FROM Articolo a WHERE lower(a.titolo) like lower(:testo) or lower(a.sottotitolo) like lower(:testo) or lower(a.testo) like lower(:testo)", 
			  nativeQuery = true)
	Set<Articolo> findByTesto(@Param("testo") String testo);
	
}