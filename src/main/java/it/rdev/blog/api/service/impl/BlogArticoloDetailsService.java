package it.rdev.blog.api.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.dao.ArticoloDao;
import it.rdev.blog.api.dao.entity.Articolo;
import it.rdev.blog.api.service.ArticoloDetailsService;

@Service
public class BlogArticoloDetailsService implements ArticoloDetailsService {
	
	@Autowired
	private ArticoloDao articoloDao;

	@Override
	public Set<ArticoloDTO> findAll() {
		Iterable<Articolo> articoli = articoloDao.findAll();
		Set<ArticoloDTO> listaCategorie = null;
		if (articoli!=null) {
			listaCategorie = new HashSet<>();
			for (Articolo a : articoli) {
				ArticoloDTO articoloDTO = new ArticoloDTO();
				articoloDTO.setTitolo(a.getTitolo());
				articoloDTO.setSottotitolo(a.getSottotitolo());
				articoloDTO.setData_creazione(a.getData_creazione());
				articoloDTO.setData_modifica(a.getData_modifica());
				articoloDTO.setData_pubblicazione(a.getData_pubblicazione());
				articoloDTO.setCategoria(a.getCategoria());
				articoloDTO.setTags(a.getTags());
				articoloDTO.setAutore(a.getAutore());
				listaCategorie.add(articoloDTO);
			}
		}
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

}