package it.rdev.blog.api.service;

import java.util.Set;

import it.rdev.blog.api.controller.dto.CategoriaDTO;
import it.rdev.blog.api.dao.entity.Categoria;

public interface CategoriaDetailsService {
	public Set<CategoriaDTO> findAll(); 
	public Categoria save(CategoriaDTO categoria);
}

