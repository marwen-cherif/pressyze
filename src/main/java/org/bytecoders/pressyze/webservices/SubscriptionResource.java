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

import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.dao.UserDAO;
import org.bytecoders.pressyze.dao.UserDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.bytecoders.pressyze.webservices.elements.BooleanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/subscription")
public class SubscriptionResource {

	private static Logger LOG = LoggerFactory
			.getLogger(SubscriptionResource.class);

	/**
	 * Permet de verifier que le nom d'utilisateur existe deja ou non
	 * 
	 * @param username
	 *            le nom d'utilisateur a verifier
	 * @return true si le nom d'utilisateur existe et false sinon
	 */
	@GET
	@Path("/username/{usernameToCheck}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public BooleanResponse userNameExists(
			@PathParam("usernameToCheck") String username) {

		LOG.debug("Verification de l'existance du nom d'utilisateur {}",
				username);

		BooleanResponse response = new BooleanResponse();
		
		UserDAO userDAO = new UserDAOImpl();
		@SuppressWarnings("unused")
		User user;
		
		try {
			if( (user = userDAO.findUserByUsername(username)) == null) {
				response.setValue(false);
			} else {
				response.setValue(true);
			}
		} catch (DAOException e) {
			
			e.printStackTrace();
			response.setValue(false);
		}
		return response;
	}

	/**
	 * Permet d'inscrire un nouvel utilisateur
	 * 
	 * @param nom
	 *            le nom d'utilisateur
	 * @param pass
	 *            le mot de passe de l'utilisateur
	 * @param estJournaliste
	 *            true si l'utilisateur est un journaliste
	 * @return true si l'inscription s'est bien passee
	 */
//	@POST
//	@Consumes("application/json")
//	@Produces(MediaType.APPLICATION_JSON)
//	public BooleanResponse subscribe2(@FormParam("nom") String nom,
//			@FormParam("pass") String pass,
//			@FormParam("estJournaliste") boolean estJournaliste) {
//
//		LOG.debug("Inscription de l'utilisateur {}, pass {}, estJournaliste : {}", nom, pass, estJournaliste);
//
//		BooleanResponse response = new BooleanResponse();
//
//		response.setValue(true);
//		return response;
//	}
	
	@GET
	@Path("/{user}/{pass}/{estJournaliste}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public User subscribe(@PathParam("user") String user, @PathParam("pass") String pass, @PathParam("estJournaliste") boolean estJournaliste) {
		LOG.debug("Inscription de l'utilisateur {}, pass {}, estJournaliste : {}", user, pass, estJournaliste);

//		BooleanResponse response = new BooleanResponse();

		User aUser = new User();
		
		aUser.setId("" + new Date().getTime());
		aUser.setUsername(user);
		aUser.setPassword(pass);
		aUser.setJournalist(estJournaliste);
		
		try {
			new UserDAOImpl().addUser(aUser);
		
			LOG.debug("L'utilisateur {} vient d'etre insere avec succees", user);
			return aUser;
			
		} catch (DAOException e) {
			
			e.printStackTrace();
//			response.setValue(false);
			LOG.error("Erreur lors de l'insertion de l'utilisateur {}", user);
		}
		
		return null;
	}
	
	
	

}
