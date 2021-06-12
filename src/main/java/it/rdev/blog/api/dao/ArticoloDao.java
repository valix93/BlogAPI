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

	@Query(value = "SELECT * FROM Articolo a WHERE a.titolo = :testo or a.sottotitolo = :testo or a.testo = :testo", 
			  nativeQuery = true)
	Set<Articolo> findByTesto(
			  @Param("testo") String testo);
	
	//@Query("SELECT a From Articolo a where a.titolo like :testo OR a.sottotitolo like :testo OR a.testo like :testo")
	//Set<Articolo> findByTesto(@Param("testo") String ricerca);
}