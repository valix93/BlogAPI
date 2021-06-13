package it.rdev.blog.api.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.rdev.blog.api.controller.dto.CategoriaDTO;
import it.rdev.blog.api.service.impl.BlogCategoriaDetailsService;

@RestController
public class CategoriaApiController {
	
	@Autowired
	private BlogCategoriaDetailsService service;

	/*
	 * Recupero della lista di categorie
	 * GET /api/categoria
	 * restituisce la lista di categorie presenti nel database
	 */
	@RequestMapping(value = "/api/categoria", method = RequestMethod.GET)
	public ResponseEntity<?> find(){
		ResponseEntity<?> response;
		Set<CategoriaDTO> categorie = service.findAll();
		if (categorie.isEmpty())
			// status code 404 se non è presente alcuna categoria all'interno del database
			response = new ResponseEntity<>("Non è presente nessuna categoria all'interno del database!", HttpStatus.NOT_FOUND);
		else
			// status code 200 se sono state restituite delle categorie
			response = new ResponseEntity<>(categorie, HttpStatus.OK);
		return response;
	}
	
	/*
	 * Inserimento di una categoria nel database
	 * POST /api/categoria
	 * Prende in input una categoria in formato JSON compilato nel seguente modo {"titolo":"titolo01"} e lo salva nel db
	 */
	@RequestMapping(value = "/api/categoria", method = RequestMethod.POST)
	public ResponseEntity<?> saveCategoria(@RequestBody CategoriaDTO categoria,
			@RequestHeader(required = true, value = "Authorization") String token) throws Exception {
		if(token != null && token.startsWith("Bearer")) {
			return ResponseEntity.ok(service.save(categoria));
		}
		// status code 401 se un utente non loggato prova ad inserire una nuova categoria nel db
		return new ResponseEntity<>("Solo gli utenti loggati possono inserire nuove categorie",HttpStatus.UNAUTHORIZED);
	}
}