package it.rdev.blog.api.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.dao.ArticoloDao;
import it.rdev.blog.api.dao.entity.Articolo;
import it.rdev.blog.api.dao.entity.User;
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

	public Set<ArticoloDTO> findArticoloByIdCategoriaTagAutore(Map<String, String> req) {
		Set<Articolo> articoli = null;
		if (req.size()==1 && req.containsKey("testo")) {
			String param = req.get("testo");
			articoli = articoloDao.findByTesto(param);
		}
		else {
			Map<String, String> params = ArticoloUtils.componiQuery(req);
			long id = Integer.parseInt(params.get("id"));
			String categoria = params.get("categoria");
			String tag = params.get("tag");
			String autore = params.get("autore");
		}
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
		//newArticolo.setCategoria(articolo.getCategoria());
		//newArticolo.setTags(articolo.getTags());
		newArticolo.setAutore(autore);
		articolo = ArticoloUtils.convertArticoloToArticoloDTO(articoloDao.save(newArticolo));
		return articolo;
	}





}