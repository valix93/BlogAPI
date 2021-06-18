package it.rdev.blog.api.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.dao.ArticoloDao;
import it.rdev.blog.api.dao.CategoriaDao;
import it.rdev.blog.api.dao.TagDao;
import it.rdev.blog.api.dao.UserDao;
import it.rdev.blog.api.dao.entity.Articolo;
import it.rdev.blog.api.dao.entity.Categoria;
import it.rdev.blog.api.dao.entity.Tag;
import it.rdev.blog.api.dao.entity.User;
import it.rdev.blog.api.service.ArticoloDetailsService;
import it.rdev.blog.api.utility.ArticoloUtils;

@Service
public class BlogArticoloDetailsService implements ArticoloDetailsService {
	
	@Autowired
	private ArticoloDao articoloDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private CategoriaDao categoriaDao;
	
	@Autowired
	private TagDao tagDao;
	
	@Override
	public Set<ArticoloDTO> findAll() {
		Iterable<Articolo> articoli = articoloDao.findAll();
		Set<ArticoloDTO> listaCategorie = ArticoloUtils.convertiArticoliToArticoliDTO(articoli);
		return listaCategorie;
	}
	
	@Override
	public Articolo save(ArticoloDTO articolo) {
		Articolo newArticolo = new Articolo();
		User autore = new User();
		autore.setId(articolo.getAutore().getId());
		autore.setUsername(articolo.getAutore().getUsername());
		newArticolo.setAutore(autore);
		newArticolo.setTitolo(articolo.getTitolo());
		newArticolo.setTitolo(articolo.getTitolo());
		newArticolo.setSottotitolo(articolo.getSottotitolo());
		//newArticolo.setCategoria(articolo.getCategoria());
		//newArticolo.setTags(articolo.getTags());
		newArticolo.setAutore(autore);
		return articoloDao.save(newArticolo);
	}

	@Override
	public ArticoloDTO findArticoloById(long id) {
		Articolo articolo = articoloDao.findById(id);
		ArticoloDTO articoloDTO = ArticoloUtils.convertArticoloToArticoloDTO(articolo);
		return articoloDTO;
	}

	@Override
	public Set<ArticoloDTO> findArticoloByTesto(String testo) {
		Set<Articolo> articoli = articoloDao.findByTesto(testo);
		Set<ArticoloDTO> articoliDTO = ArticoloUtils.convertiArticoliToArticoliDTO(articoli);
		return articoliDTO;
	}
	
	public int deleteArticoloByIdAutore(long id, long idAutore) {
		User user = new User();
		user.setId(idAutore);
		int cancellazione = articoloDao.deleteByIdAndAutore(id, user);
		return cancellazione;
		
	}

	public ArticoloDTO saveOrUpdate(ArticoloDTO articolo) {
		Articolo newArticolo = new Articolo();
		User autore = new User();
		autore.setId(articolo.getAutore().getId());
		autore.setUsername(articolo.getAutore().getUsername());
		newArticolo.setAutore(autore);
		newArticolo.setTitolo(articolo.getTitolo());
		newArticolo.setTitolo(articolo.getTitolo());
		newArticolo.setSottotitolo(articolo.getSottotitolo());
		newArticolo.setAutore(autore);
		articolo = ArticoloUtils.convertArticoloToArticoloDTO(articoloDao.save(newArticolo));
		return articolo;
	}
	
	public Set<ArticoloDTO> findArticoliByIdAndAutoreAndCategoria(Long id, String autore, String categoria, String tag) {
		Set<ArticoloDTO> articoliDTO = null;
		Set<Articolo> articoli = new HashSet<>();
		User autoreObj = new User();
		Categoria categoriaObj = new Categoria();
		Tag tagObj = new Tag();
		Long idAutore = null;
		Long idCategoria = null;
		Long idTag = null;
		categoriaObj = categoriaDao.findByTitolo(categoria);
		autoreObj = userDao.findByUsername(autore);
		tagObj = tagDao.findByTitolo(tag);
		if (autoreObj!=null) idAutore = autoreObj.getId();
		if (categoriaObj!=null) idCategoria = categoriaObj.getId();
		if (tagObj!=null) idTag = tagObj.getId();
		if (categoria!=null && categoriaObj==null) return null;
		if (autore!=null && autoreObj==null) return null;
		if (id!=null) {
			if (articoloDao.findById(id)==null) return null;
			Articolo articolo = articoloDao.findArticoloByParams(id, idAutore, idCategoria);
			articoli.add(articolo);
		}
		else if (tag!=null) {	 
			if (tagObj==null)return null;
				articoli = articoloDao.findArticoloByIdAndAutoreAndCategoriaAndTags(id, idAutore, idCategoria, idTag);
		}
		else {
			articoli = articoloDao.findArticoliByParams(id, idAutore, idCategoria);

		}
		articoliDTO = ArticoloUtils.convertiArticoliToArticoliDTO(articoli);
	
		return articoliDTO;
	}

}