package it.rdev.blog.api.utility;

import java.util.HashSet;
import java.util.Set;

import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.dao.entity.Articolo;

public class ArticoloUtils {
	
	public static ArticoloDTO convertArticoloToArticoloDTO(Articolo articolo) {
		ArticoloDTO articoloDTO = new ArticoloDTO();
		articoloDTO.setTitolo(articolo.getTitolo());
		articoloDTO.setSottotitolo(articolo.getSottotitolo());
		articoloDTO.setData_creazione(articolo.getData_creazione());
		articoloDTO.setData_modifica(articolo.getData_modifica());
		articoloDTO.setData_pubblicazione(articolo.getData_pubblicazione());
		articoloDTO.setCategoria(articolo.getCategoria());
		articoloDTO.setTags(articolo.getTags());
		articoloDTO.setAutore(articolo.getAutore());
		return articoloDTO;
		
	}

	public static Set<ArticoloDTO> convertiArticoliToArticoliDTO(Iterable<Articolo> articoli){
		Set<ArticoloDTO> articoliDTO = null;
		if (articoli!=null) {
			articoliDTO = new HashSet<>();
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
				articoliDTO.add(articoloDTO);
			}
		}
		return articoliDTO;
	}

}
