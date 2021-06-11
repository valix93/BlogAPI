package it.rdev.blog.api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.rdev.blog.api.dao.entity.Articolo;


@Repository
public interface ArticoloDao extends CrudRepository<Articolo, Integer> {
	
	Articolo findByTitolo(String titolo);
	Articolo findById(long id);
}