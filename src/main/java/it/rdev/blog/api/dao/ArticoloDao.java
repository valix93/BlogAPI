package it.rdev.blog.api.dao;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.rdev.blog.api.dao.entity.Articolo;
import it.rdev.blog.api.dao.entity.User;


@Repository
public interface ArticoloDao extends CrudRepository<Articolo, Integer> {
	
	Articolo findByTitolo(String titolo);
	Articolo findById(long id);
	
	@Query(value = "SELECT * FROM Articolo a WHERE lower(a.titolo) like lower(:testo) or lower(a.sottotitolo) like lower(:testo) or lower(a.testo) like lower(:testo)", 
			  nativeQuery = true)
	Set<Articolo> findByTesto(@Param("testo") String testo);
	
	@Query(value = "SELECT * FROM articolo a inner join users u on a.autore = u.id and u.username LIKE :autore OR :autore IS NULL inner join categoria c on a.categoria = c.id and c.titolo LIKE :categoria OR :id IS NULL inner join articolotag at on a.id = at.id_articolo inner join tag t on t.id = at.id_tag and t.titolo LIKE :tag OR :tag IS NULL where a.id = :id OR :id IS NULL", 
			  nativeQuery = true)
	Set<Articolo> findByIdAndCategoriaAndTagAndAutore(long id, String categoria, String tag, String autore);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM Articolo a WHERE a.id = :idArticolo and a.autore = :autore",
			  nativeQuery = true)
	int deleteByIdAndAutore(long idArticolo, User autore);
	
	@Query(value = "SELECT * FROM Articolo a inner join Users u on a.autore = u.id inner join Categoria c on a.categoria = c.id inner join ArticoloTag arttag on a.id = arttag.id_articolo inner join Tag t on arttag.id_tag = t.id "
			+ "where a.id = :id OR :id IS NULL and a.autore = :autore OR :autore IS NULL and a.categoria = :categoria OR :categoria IS NULL and t.id = :tag OR :tag IS NULL", 
			  nativeQuery = true)
	Articolo findArticoloByIdAndAutoreAndCategoriaAndTags(@Param("id") Long id, @Param("autore") Long autore, Long categoria, Long tag);

	
	@Query(value = "SELECT * FROM Articolo a inner join Users u on a.autore = u.id inner join Categoria c on a.categoria = c.id "
			+ "where a.id = :id OR :id IS NULL and a.autore = :autore OR :autore IS NULL and a.categoria = :categoria OR :categoria IS NULL", 
			  nativeQuery = true)
	Set<Articolo> findArticoliByIdAndAutoreAndCategoria(@Param("id") Long id, @Param("autore") Long autore, @Param("categoria") Long categoria);

	@Query(value = "SELECT * FROM Articolo a where a.id = :id OR :id IS NULL and a.autore = :autore OR :autore IS NULL and a.categoria = :categoria OR :categoria IS NULL", 
			  nativeQuery = true)
	Set<Articolo> findArticoliByParams(@Param("id") Long id, @Param("autore") Long autore, @Param("categoria") Long categoria);

	
}


