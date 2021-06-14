package it.rdev.blog.api.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import it.rdev.blog.api.config.JwtTokenUtil;
import it.rdev.blog.api.controller.dto.ArticoloDTO;
import it.rdev.blog.api.controller.dto.UserDTO;
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
			Long id = null;
			String autore = null;
			String categoria = null;
			for (String  r : req.keySet()) {
				if (r.equals("id") || r.equals("categoria") || r.equals("tag") || r.equals("testo") || r.equals("autore")) {
					String testo = req.get(r);
					if (r.equals("testo")) {
						if (testo.length()>2) articoli = service.findArticoloByTesto(testo);
						else return new ResponseEntity<>("Inserisci almeno 3 caratteri come parametro della richiesta", HttpStatus.BAD_REQUEST);	
					}
					if (r.equals("id")) {
						id = Long.parseLong(testo);
					}
					if (r.equals("autore")) {
						autore = testo;

					}
					if (r.equals("categoria")) {
						categoria = testo;
					}
				}
				else {
					// status code 400 se uno dei parametri passati in input non è formalmente corretto
					return new ResponseEntity<>("Inserisci i parametri corretti nella richiesta", HttpStatus.BAD_REQUEST);
				}
			}
			if (id!=null || autore!=null || categoria!=null) {
				ArticoloDTO articolo = service.findArticoloByIdAndAutoreAndCategoria(id,autore,categoria);
				if (articolo!=null) return new ResponseEntity<>(articolo,HttpStatus.OK);
				else return new ResponseEntity<>("Nessun articolo disponibile", HttpStatus.NOT_FOUND);
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
				// TODO token scaduto
				long idUtente = jwtUtil.getUserIdFromToken(token);
				if (articolo.getAutore().getId()!=idUtente) {
					response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				else {
					response = new ResponseEntity<>(articolo, HttpStatus.OK);
				}
				return response;
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
	
	/*
	 *  Inserimento di un articolo
	 *  POST /api/articolo/
	 *  Il servizio prende in input un articolo in formato JSON compilato in ogni sua parte ed indica all'utente l'avvenuto inserimento.
	 *  L'articolo inserito è sempre in bozza, il passaggio in stato pubblicato è effettuato da un altro servizio.
	 */
	@RequestMapping(value = "/api/articolo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> post (
			@RequestHeader(required = true, value = "Authorization") String token,
			@RequestBody ArticoloDTO articolo) {
		
		if(token != null && token.startsWith("Bearer")) {
			token = token.replaceAll("Bearer ", "");
			Long idUtente = jwtUtil.getUserIdFromToken(token);				
			String username = jwtUtil.getUsernameFromToken(token);
			service.saveOrUpdate(articolo);
			return new ResponseEntity<>(HttpStatus.OK);
		} 
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		
	}
	
	/*
	 *  Modifica di un articolo
	 *  POST /api/articolo/<:id>
	 *  Il servizio permette l'update di un articolo passando in input un articolo in formato JSON. 
	 *  L'aggiornamento è consentito solo all'autore dell'articolo dopo aver effettuato il login
	 */
	@RequestMapping(value = "/api/articolo/{id:\\d+}", method = RequestMethod.PUT)
	public ResponseEntity<?> deleteArticoloById (@PathVariable final long id,
			@RequestHeader(required = true, value = "Authorization") String token,
			@RequestBody ArticoloDTO articoloInput) {
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
					String titoloInput = articoloInput.getTitolo();
					String sottotitoloInput = articoloInput.getSottotitolo();
					java.sql.Timestamp dataPubblicazione = articoloInput.getData_pubblicazione();
					if (titoloInput!=null && !titoloInput.isEmpty()) articolo.setTitolo(titoloInput);
					if (sottotitoloInput!=null && !sottotitoloInput.isEmpty()) articolo.setSottotitolo(sottotitoloInput);
					// TODO stato bozza o pubblicato
					articolo = service.saveOrUpdate(articolo);
					if (articolo!=null) {
						// status code 204 se l'operazione di modifica va a buon fine
						return new ResponseEntity<>("articolo modificato con successo!",HttpStatus.NO_CONTENT);
					}
					else {
						// status code 500 se l'operazione di modifica fallisce
						return new ResponseEntity<>("articolo non creato!",HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
			else {
				// 401 se un utente non loggato prova ad effettuare la modifica di un articolo
				return new ResponseEntity<>("Devi essere un utente loggato per modificare un articolo",HttpStatus.UNAUTHORIZED);
			}
		}
	}
	
	
	
	/*
	 *  Eliminazione di un articolo
	 *  DELETE /api/articolo/<:id>
	 *  Il servizio elimina un articolo presente all'interno del db. 
	 *  L'eliminazione è consentita solo all'autore dell'articolo dopo aver effettuato il login
	 */
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
	
	/*@PostMapping({ "/api/articolo" })
	public String post(@RequestHeader(name = "Authorization") String token) {
		String username = null;
		if(token != null && token.startsWith("Bearer")) {
			token = token.replaceAll("Bearer ", "");
			username = jwtUtil.getUsernameFromToken(token);
		}
		return "Risorsa Protetta [" + username + "]";
	}
	*/

}