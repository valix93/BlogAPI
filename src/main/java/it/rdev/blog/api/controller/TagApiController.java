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

import it.rdev.blog.api.controller.dto.TagDTO;
import it.rdev.blog.api.service.impl.BlogTagDetailsService;

@RestController
public class TagApiController {
	
	@Autowired
	private BlogTagDetailsService service;

	/*
	 * Recupero tag lista di tags
	 * GET /api/categoria
	 * restituisce la lista di tags presenti nel database
	 */
	@RequestMapping(value = "/api/tag", method = RequestMethod.GET)
	public ResponseEntity<?> find(){
		ResponseEntity<?> response;
		Set<TagDTO> tags = service.findAll();
		if (tags==null || tags.isEmpty()) {
			// status code 404 se non è presente alcun tag all'interno del database
			response = new ResponseEntity<>("Non è presente nessuna categoria all'interno del database!", HttpStatus.NOT_FOUND);		}
		else
			// status code 200 se sono state restituite delle categorie
			response = new ResponseEntity<>(tags, HttpStatus.OK);
		return response;
	}
	
	/*
	 * Inserimento di un tag nel database
	 * POST /api/tag
	 * Prende in input un tag in formato JSON compilato nel seguente modo {"titolo":"titolo01"} e lo salva nel db
	 */
	@RequestMapping(value = "/api/tag", method = RequestMethod.POST)
	public ResponseEntity<?> saveTag(@RequestBody TagDTO tag,
			@RequestHeader(required = true, value = "Authorization") String token) throws Exception {
		if(token != null && token.startsWith("Bearer")) {
			return ResponseEntity.ok(service.save(tag));
		}
		// status code 401 se un utente non loggato prova ad inserire un nuovo tag nel db
		return new ResponseEntity<>("Solo gli utenti loggati possono inserire novi tag",HttpStatus.UNAUTHORIZED);
	}
}