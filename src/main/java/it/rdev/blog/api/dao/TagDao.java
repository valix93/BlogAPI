package it.rdev.blog.api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.rdev.blog.api.dao.entity.Tag;

@Repository
public interface TagDao extends CrudRepository<Tag, Integer> {
	
	Tag findByTitolo(String titolo);
	
}