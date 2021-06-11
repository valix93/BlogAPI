package it.rdev.blog.api.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.CategoriaDTO;
import it.rdev.blog.api.dao.CategoriaDao;
import it.rdev.blog.api.dao.entity.Categoria;
import it.rdev.blog.api.service.CategoriaDetailsService;

@Service
public class BlogCategoriaDetailsService implements CategoriaDetailsService {
	
	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public Set<CategoriaDTO> findAll() {
		Iterable<Categoria> categorie = categoriaDao.findAll();
		Set<CategoriaDTO> listaCategorie = new HashSet<>();
		for (Categoria c : categorie) {
			CategoriaDTO categoriaDTO = new CategoriaDTO();
			categoriaDTO.setTitolo(c.getTitolo());
			listaCategorie.add(categoriaDTO);
		}
		return listaCategorie;
	}
	
	@Override
	public Categoria save(CategoriaDTO categoria) {
		Categoria newCategoria = new Categoria();
		newCategoria.setTitolo(categoria.getTitolo());
		return categoriaDao.save(newCategoria);
	}

}