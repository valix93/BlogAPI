package it.rdev.blog.api.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.dao.ArticoloDao;
import it.rdev.blog.api.dao.entity.Articolo;
import it.rdev.blog.api.service.ArticoloDetailsService;
import it.rdev.blog.api.utility.ArticoloUtils;

@Service
public class BlogArticoloDetailsService implements ArticoloDetailsService {
	
	@Autowired
	private ArticoloDao articoloDao;

	@Override
	public Set<ArticoloDTO> findAll() {
		Iterable<Articolo> articoli = articoloDao.findAll();
		Set<ArticoloDTO> listaCategorie = ArticoloUtils.convertiArticoliToArticoliDTO(articoli);
		return listaCategorie;
	}
	
	@Override
	public Articolo save(ArticoloDTO articolo) {
		Articolo newArticolo = new Articolo();
		newArticolo.setTitolo(articolo.getTitolo());
		newArticolo.setTitolo(articolo.getTitolo());
		newArticolo.setSottotitolo(articolo.getSottotitolo());
		newArticolo.setCategoria(articolo.getCategoria());
		newArticolo.setTags(articolo.getTags());
		newArticolo.setAutore(articolo.getAutore());
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


}