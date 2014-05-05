/*
 //	Le présent fichier fait partie du projet PRESSYZE, une application se proposant 
 //	d'encourager le journalisme citoyen et permettant d'avoir une vue globale 
 // sur les évènements se déroulant sur le sol tunisien.
 //
 //
 //	Ce projet entre dans le cadre du concours Java Developer Challenge (Edition 2014) 
 // organisé par ESPRIT JAVA USER GROUP qui met le focus sur les dernières technologies du monde Java.
 //
 // Ce projet a été réalisé par l'équipe << ByteCoders >> composée des élèves ingénieurs suivants :
 //
 //		- Mohamed Chehaibi
 //		- Mohamed Ali Ben Lassoued
 //		- Mohamed Melki
 //		- Marwen Chrif
 //		- Nabil Andriantomanga
 //
 //	Les technologies utilisées sont essentiellement :
 //
 //	AnularJS : un framework JavaScript proposé par Google et présente une méthodologie innovante 
 // et adaptée au monde de l'industrie, facilite la réalisation des applications mono-page 
 //	et permet la mise en place de plusieurs patrons de conception dont l'MVC.
 //
 //
 //	Mongo DB : SGBD NoSQL orientée documents répartissable sur un nombre quelconque d'ordinateurs.
 //
 //	REST JAX-RS 2.8 (Jersey Implementation) : Java API for RESTful Web Services est une interface 
 // de programmation Java permettant de créer des services Web avec une architecture REST.
 //
 //	Apache Tomcat 7.0.42 : Serveur d'application Java EE.
 //
 //	Maven 3.1 : système de gestion et d'automatisation de production des projets logiciels 
 // Java en général et Java EE en particulier.
 */
package org.bytecoders.pressyze.webservices;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.dao.UserDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
//import org.bytecoders.pressyze.webservices.elements.BooleanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/authentication")
public class AuthenticationResource {

	private static final Logger LG = LoggerFactory.getLogger(AuthenticationResource.class);
	
	// URL : http://localhost:9095/Pressyze/rest/authentication/username/password
	/**
	 * Permet d'authentifier l'utilisateur
	 * 
	 * @param username le nom d'utilisateur 
	 * @param password le mot de passe de l'utilisateur
	 * @return vrai si l'utilisateur est authentifie 
	 */
	
	@GET
	@Path("/{username}/{encryptedPassword}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public User authentify(@PathParam("username") String username,
			@PathParam("encryptedPassword") String password) {
//		BooleanResponse response = new BooleanResponse();

		LG.debug("Authentification de l'utilisateur {} en cours avec mot de passe hache : {}", username, password);
		
//		response.setValue(false);
//		
		try {
			User user = new UserDAOImpl().findUserByUsername(username);
			if(user == null) {
				LG.debug("Utilisateur non authentifie");
			} else
				if(user.getPassword().equalsIgnoreCase(password)) {
					LG.debug("L'utilisateur {} est authentifie ", username);
					return user;
				} else {
					LG.error("Combinaison Nom d'utilisateur/Mot de passe erronee");
				}
		} catch (DAOException e) {
			LG.error(e.getMessage());
		}
		
		return null;
	}
}
