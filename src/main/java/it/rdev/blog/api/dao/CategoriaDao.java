package it.rdev.blog.api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.rdev.blog.api.dao.entity.Categoria;

@Repository
public interface CategoriaDao extends CrudRepository<Categoria, Integer> {
	
	Categoria findByTitolo(String titolo);
	
	
	
}