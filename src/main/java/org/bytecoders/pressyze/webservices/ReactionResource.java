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
 //	AngularJS : un framework JavaScript proposé par Google et présente une méthodologie innovante 
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
  
 //
 */
package org.bytecoders.pressyze.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.dao.FactDAO;
import org.bytecoders.pressyze.dao.FactDAOImpl;
import org.bytecoders.pressyze.dao.UserDAO;
import org.bytecoders.pressyze.dao.UserDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.bytecoders.pressyze.webservices.elements.BooleanResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Reste a faire : la partie sécurité pour les méthodes à sécuriser
// Comme l'ajout de reaction ...
@Path("/reaction")
public class ReactionResource {

	private static final Logger LG = LoggerFactory	// Log.
			.getLogger(ReactionResource.class);

	/**
	 * La reaction est de type confirmation
	 */
	private static final int REACTION_TYPE_CONFIRMATION = 0;

	
	/**
	 * La reaction est de type reniement
	 */
	private static final int REACTION_TYPE_DENIAL = 1;

	
	/**
	 * La reaction est de type denonciation de spam
	 */
	private static final int REACTION_TYPE_SPAM = 2;

	// URL : http://localhost:9095/Pressyze/rest/reaction/{userId}/{factId}/{reactionType}
	/**
	 * Permet de sauvegarder la reaction d'un utilisateur par rapport a un fait.
	 * Le systeme prend en compte 3 (trois) types de reaction : confirmation,
	 * reniement et spam.
	 * 
	 * @param userId
	 *            l'identifiant de l'utilisateur
	 * @param factId
	 *            l'identifiant du fait
	 * @param reactionType
	 *            le type de reaction
	 * @return
	 */
	@GET
	@Path("/{userId}/{factId}/{reactionType}")
	@Produces(MediaType.APPLICATION_JSON)
	public BooleanResponse processReaction(@PathParam("userId") String userId,
			@PathParam("factId") String factId,
			@PathParam("reactionType") int reactionType) {
		LG.debug("Traitement d'une reaction d'un utilisateur en cours ...");

		BooleanResponse response = new BooleanResponse(); // false par defaut
															// ...

		if (userId == null || factId == null) {
			LG.debug("L'identifiant de l'utilisateur ou celui du fait est null");
			return response;
		}

		if (userId.trim().isEmpty() || factId.trim().isEmpty()) {
			LG.debug("L'identifiant de l'utilisateur ou celui du fait est vide");
			return response;
		}

		if (!reactionTypeExists(reactionType)) {
			LG.debug(
					new StringBuilder("Le type de reaction ")
							.append("doit etre l'un des valeurs suivants\n")
							.append("\t {} : Confirmation d'un fait")
							.append("\t {} : Reniement d'un fait")
							.append("\t {} : Pour declarer qu'un fait est un spam")
							.toString(), REACTION_TYPE_CONFIRMATION,
					REACTION_TYPE_DENIAL, REACTION_TYPE_SPAM);
			return response;
		}

		final FactDAO factDAO = new FactDAOImpl();		
		final UserDAO userDAO = new UserDAOImpl();

		try {
			Fact selectedFact = factDAO.findFact(factId);

			if (selectedFact == null) {
				LG.debug("Impossible de recuperer le fait identifie par : {}",
						factId);
				return response;
			}

			User actor = userDAO.findUser(userId);

			if (actor == null) {
				LG.debug(
						"Impossible de recuperer l'utilisateur identifie par : {}",
						userId);
				return response;
			}

			// Verifier si c'est pas lui meme qui a posté ce commentaire
			if(selectedFact.getReporter().equals(actor)) {
				LG.debug("Un utilisateur ne peut pas donner un avis sur sa propre publication");
				return response;
			}
			
			// Verifier si l'utilisateur n'a pas deja réagi par rapport a ce
			// fait
			if (selectedFact.getConfirmation().hasChecker(actor)
					|| selectedFact.getDenial().hasDenier(actor)
					|| selectedFact.getSpam().hasDenouncer(actor)) {
				LG.debug("{} a deja reagi au fait ({}) identifie par {}", actor
						.getUsername(), selectedFact
						.getEvent().getLabel(), factId);
				return response;
			}

			switch (reactionType) {

			case REACTION_TYPE_CONFIRMATION:

				selectedFact.getConfirmation().addChecker(actor);

				break;

			case REACTION_TYPE_DENIAL:

				selectedFact.getDenial().addDenier(actor);

				break;

			default:
				selectedFact.getSpam().addDenouncer(actor);
			}

			factDAO.updateFact(selectedFact);

			response.setValue(true);

			LG.debug("{} vient de {} le fait {}", actor.getUsername(),
					verbe(reactionType), selectedFact.getEvent().getLabel());

			return response;

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Permet de verifier que ce type de reaction est pris en compte par le
	 * systeme.
	 * 
	 * @param reactionType
	 *            le type de reaction
	 * @return vrai si le type de reaction existe
	 */
	private boolean reactionTypeExists(int reactionType) {
		return reactionType == REACTION_TYPE_CONFIRMATION
				|| reactionType == REACTION_TYPE_DENIAL
				|| reactionType == REACTION_TYPE_SPAM;
	}

	/**
	 * Permet d'avoir le verbe a utiliser selon le type de reaction
	 * 
	 * @param reactionType
	 *            le type de reaction
	 * @return le verbe a utiliser
	 */
	private String verbe(int reactionType) {
		switch (reactionType) {
		case REACTION_TYPE_CONFIRMATION:
			return "confirmer";
		case REACTION_TYPE_DENIAL:
			return "renier";
		default:
			return "declarer comme spam";
		}
	}

}
