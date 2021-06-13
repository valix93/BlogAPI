package it.rdev.blog.api.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import it.rdev.blog.api.config.JwtTokenUtil;
import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.service.impl.BlogArticoloDetailsService;

@RestController
public class ArticoloApiController {
	
	@Autowired
	private JwtTokenUtil jwtUtil;
	
	@Autowired
	private BlogArticoloDetailsService service;

	// TODO migliorare questo metodo, favorire il riutilizzo del codice
	@RequestMapping(value = "/api/articolo", method = RequestMethod.GET)
	public ResponseEntity<?> find(
			@RequestHeader(required = false, value = "Authorization") String token,
			@RequestParam(required = false) Map<String, String> req){
		Set<ArticoloDTO> articoli = null;
		if (req==null || req.isEmpty()) {
			articoli = service.findAll();
		}
		else {
			//articoli = service.findArticoloByIdCategoriaTagAutore(req);
			for (String  r : req.keySet()) {
				if (r.equals("id") || r.equals("categoria") || r.equals("tag") || r.equals("testo")) {
					String testo = req.get(r);
					if (r.equals("testo")) {
						articoli = service.findArticoloByTesto(testo);
					}
				}
				else {
					// status code 400 se uno dei parametri passati in input non è formalmente corretto
					return new ResponseEntity<>("Inserisci i parametri corretti nella richiesta", HttpStatus.BAD_REQUEST);
				}
			}
		}
		if (articoli!=null && !articoli.isEmpty()) {
			// status code 200 se la ricerca produce risultati
			return new ResponseEntity<>(articoli,HttpStatus.OK);
		}
		else {
			// status code 404 se la ricerca non produce risultati
			return new ResponseEntity<>("Nessun articolo disponibile", HttpStatus.NOT_FOUND);	
		}			
	}
	
	@RequestMapping(value = "/api/articolo/{id:\\d+}", method = RequestMethod.GET)
	public ResponseEntity<?> getArticoloById(@PathVariable final long id, 
			@RequestHeader(required = false, value = "Authorization") String token) {
		ResponseEntity<ArticoloDTO> response;
		ArticoloDTO articolo = service.findArticoloById(id);
		if (articolo!=null && articolo.getData_pubblicazione()==null) {
			if(token != null && token.startsWith("Bearer")) {
				token = token.replaceAll("Bearer ", "");
				long idUtente = jwtUtil.getUserIdFromToken(token);
				if (articolo.getAutore().getId()!=idUtente) {
					response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
					return response;
				}
			}
		}
		if (articolo==null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else if (articolo.getData_pubblicazione()==null){
			response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		else {
			response = new ResponseEntity<>(articolo, HttpStatus.OK);
		}
		return response;
	}
	
	@RequestMapping(value = "/api/articolo/{id:\\d+}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteArticoloById (@PathVariable final long id,
			@RequestHeader(required = true, value = "Authorization") String token) {
		ArticoloDTO articolo = service.findArticoloById(id);
		if (articolo==null) {
			// 404 se l'id passato in input non è associato ad alcun articolo presente nel database
			return new ResponseEntity<>("Articolo non presente!", HttpStatus.NOT_FOUND);
		} else {
			if(token != null && token.startsWith("Bearer")) {
				token = token.replaceAll("Bearer ", "");
				long idUtente = jwtUtil.getUserIdFromToken(token);
				if (articolo.getAutore().getId() != idUtente) {
					// status code 403 se un utente loggato che non è l'autore dell'articolo che cerca di eliminare
					return new ResponseEntity<>("Non sei il creatore dell'articolo!", HttpStatus.FORBIDDEN);
				}
				else {
					int ret = service.deleteArticoloByIdAutore(id, idUtente);
					String msg = "Articolo con id " + id;
					if (ret==1) {
						// status code 204 se l'operazione di cancellazione va a buon fine
						return new ResponseEntity<>(msg + " cancellato con successo!",HttpStatus.NO_CONTENT);
					}
					else {
						// status code 500 se l'operazione di cancellazione fallisce
						return new ResponseEntity<>(msg + " non cancellato!",HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
			else {
				// 401 se un utente non loggato prova ad effettuare l'eliminazione di un articolo
				return new ResponseEntity<>("Devi essere un utente loggato per eliminare un articolo",HttpStatus.UNAUTHORIZED);
			}
		}
	}
	
	@PostMapping({ "/api/articolo" })
	public String post(@RequestHeader(name = "Authorization") String token) {
		String username = null;
		if(token != null && token.startsWith("Bearer")) {
			token = token.replaceAll("Bearer ", "");
			username = jwtUtil.getUsernameFromToken(token);
		}
		return "Risorsa Protetta [" + username + "]";
	}

}