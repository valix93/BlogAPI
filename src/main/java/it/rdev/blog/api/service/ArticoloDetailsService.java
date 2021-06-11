package it.rdev.blog.api.service;

import java.util.Set;

import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.dao.entity.Articolo;

public interface ArticoloDetailsService {
	public Set<ArticoloDTO> findAll(); 
	public Articolo save(ArticoloDTO articolo);
	public ArticoloDTO findArticoloById(long id);
}
