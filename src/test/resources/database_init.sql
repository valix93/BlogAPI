--create database blog;
--use blog;

CREATE TABLE IF NOT EXISTS users (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  PRIMARY KEY (id)
);
--
--
-- ddinuzzo/password02
INSERT INTO users
(username, password)
VALUES('ddinuzzo', '$2a$10$vj3PqvSqQSsLhknZpxU2oOIUOdmm6cpPu1shwcyXHVzba.xBWLe4K');

-- Categoria(id, titolo) PK id
-- Articolo(id, titolo, sottotitolo, testo, data_creazione, data_pubblicazione, data_modifica, categoria, autore)
-- PK id, FK categoria ref. Categoria, FK autore ref. Utente
-- Tag(id, titolo) PK id
-- ArticoloTag(id_articolo, id_tag) FK id_articolo ref. Articolo, FK id_tag ref. Tag

CREATE TABLE IF NOT EXISTS categoria (
	id bigint unsigned NOT NULL AUTO_INCREMENT,
	titolo varchar(20),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS articolo (
	id bigint unsigned NOT NULL AUTO_INCREMENT,
	titolo varchar(120) NOT NULL,
	sottotitolo varchar(120),
	testo text(1000),
	data_creazione datetime DEFAULT CURRENT_TIMESTAMP,
	data_pubblicazione datetime DEFAULT null,
	data_modifica  DATETIME ON UPDATE CURRENT_TIMESTAMP,
	autore bigint unsigned,
	categoria bigint unsigned,
	PRIMARY KEY(id),
	FOREIGN KEY (autore) 
	REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (categoria) 
	REFERENCES categoria(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tag (
	id bigint unsigned NOT NULL AUTO_INCREMENT,
	titolo varchar(20),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS articolotag (
	id_articolo bigint unsigned,
	id_tag bigint unsigned,
    FOREIGN KEY (id_articolo) 
	REFERENCES articolo(id) ON DELETE CASCADE,
    FOREIGN KEY (id_tag) 
	REFERENCES tag(id) ON DELETE CASCADE
); 

insert into categoria (titolo) values ('sport');