

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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.Comment;
import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.dao.CommentDAOImpl;
import org.bytecoders.pressyze.dao.FactDAOImpl;
import org.bytecoders.pressyze.dao.UserDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.bytecoders.pressyze.webservices.elements.BooleanResponse;
import org.bytecoders.pressyze.webservices.elements.CommentResponse;
import org.bytecoders.pressyze.webservices.elements.FactResponse;
import org.bytecoders.pressyze.webservices.elements.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/comments")
public class CommentResource {

	private static final Logger LG = LoggerFactory
			.getLogger(CommentResource.class);

	
	// URL :
		// localhost:9095/Pressyze/rest/comments/get/{commentId}
	/**
	 * Permet de recuperer le commentaire dont l'id est fourni
	 * 
	 * @param commentId identifiant du commentaire a recuperer
	 * @return le commentaire au format JSON
	 */
	@PermitAll
	@GET
	@Path("/get/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public CommentResponse retrieveComment(@PathParam("commentId") String commentId) {
		LG.debug("Recuperation d'une commentaire en cours");

		if (commentId == null || commentId.trim().length() == 0) {
			LG.debug("L'identifiant fourni est null. Interruption de l'operation.");
			return null;
		}

		try {
			final Comment comment = new CommentDAOImpl().findComment(commentId);
			if (comment != null) {

				LG.debug("Commentaire trouve. Envoi en cours");

				final CommentResponse response = new CommentResponse();

				response.setId(comment.getId());
				response.setContent(comment.getContent());
				response.setType(comment.getType());

				FactResponse fact = new FactResponse();
				fact.setId(comment.getFact().getId()); // l'id est suffisant ..

				if (fact.getId() == null) {
					LG.debug("Un probleme avec l'identifiant du fait "
							+ "relatif au commentaire a recuperer");
					return null;
				}

				UserResponse user = new UserResponse();
				user.setId(comment.getUser().getId()); // l'id est suffisant ..

				if (user.getId() == null) {
					LG.debug("Un probleme avec l'identifiant de l'utilisateur "
							+ "relatif au commentaire a recuperer");
					return null;
				}

				response.setFact(fact);
				response.setUser(user);

				LG.debug("Succes de la recuperation du commentaire");

				return response;

			}
		} catch (DAOException e) {

			e.printStackTrace();
		}
		LG.debug("Probleme lors de la recuperation du commentaire {}",
				commentId);
		return null;
	}

	
	
	// ============== Reste a faire ici : gestion de la securite ==============
	// URL :
	// localhost:9095/Pressyze/rest/comments/add/{commentContent}/{commentType}/{userId}/{factId}
	/**
	 * Permet d'ajouter un nouveau commentaire
	 * 
	 * @param commentId identifiant du commentaire a sauvegarder 
	 * @param commentContent contenu du commentaire (au moins un caractere)
	 * @param commentType (type du commentaire. Le type peut etre 1 (favorable) ou 0 (defavorable))
	 * @param factId identifiant du fait auquel est relatif le commentaire
	 * @return true si succes de l'operation
	 */
	@GET
	@Path("/add/{commentContent}/{commentType}/{userId}/{factId}")
	@Produces(MediaType.APPLICATION_JSON)
	public BooleanResponse persistComment(
			@PathParam("commentContent") String commentContent,
			@PathParam("commentType") int commentType,
			@PathParam("userId") String userId,
			@PathParam("factId") String factId) {

		LG.debug("Demande d'ajout d'un nouveau commentaire en cours de traitement...");

		BooleanResponse response = new BooleanResponse(); // false par defaut

		if (factId == null || factId.trim().length() == 0 || userId == null
				|| userId.trim().length() == 0) {

			LG.debug("Un probleme avec l'un des parametres suivants : "
					+ "identifiant du fait relatif au nouveau commentaire "
					+ "ou celui de l'utilisateur qui l'a poste");
			return response;
		}

		if (commentContent == null || commentContent.trim().length() == 0) {
			LG.debug("Le commentaire doit contenir au moins un caractere. "
					+ "Interruption de l'operation d'ajout.");
			return response;
		}

		if (commentType != Comment.CONFIRMATION_COMMENT
				&& commentType != Comment.DENIAL_COMMENT) {
			LG.debug("Le type du commentaire est inconnu. Interruption de l'operation d'ajout.");
			return response;
		}

		try {
			LG.debug("Verification de l'existance du fait en vue de l'ajout d'un commentaire");
			Fact fact = new FactDAOImpl().findFact(factId);

			if (fact == null) {
				LG.debug("Le fait n'existe pas");
				return response;
			}

			LG.debug("Verification de l'existance de l'utilisateur en vue de l'ajout d'un commentaire");
			User user = new UserDAOImpl().findUser(userId);

			if (user == null) {
				LG.debug("L'utilisateur n'existe pas");
				return response;
			}

			LG.debug("Succes des verification. Sauvegarde du commentaire en cours ...");

			Comment newComment = new Comment();
			newComment.setId("" + new Date().getTime());
			newComment.setContent(commentContent);
			newComment.setType(commentType);
			newComment.setFact(fact);
			newComment.setUser(user);

			new CommentDAOImpl().addComment(newComment);

			LG.debug("Nouveau commentaire ajoute avec succes ! ");

			response.setValue(true);

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return response;
	}

	
	
	// URL : localhost:9095/Pressyze/rest/comments/confirmation/{factId}
	/**
	 * Permet de recuperer tous les commentaires favorables du fait dont
	 * l'identifiant est fourni.
	 * 
	 * @param factId
	 *            identifiant du fait
	 * @return la liste des commentaires << Pour >>
	 */
	@PermitAll
	@GET
	@Path("/confirmation/{factId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentResponse> getFactConfirmationComments(
			@PathParam("factId") String factId) {

		LG.debug(
				"Recuperation de tous les commentaires confirmant le fait dont l'identifiant est {}",
				factId);

		List<CommentResponse> responses = new ArrayList<CommentResponse>();

		try {
			for (Iterator<Comment> iterator = new CommentDAOImpl()
					.findCommentByFactId(factId).iterator(); iterator.hasNext();) {

				Comment comment = iterator.next();
				if (comment.getType() == Comment.CONFIRMATION_COMMENT) {

					CommentResponse response = new CommentResponse();
					response.setId(comment.getId());
					response.setContent(comment.getContent());
					response.setType(comment.getType());

					responses.add(response);
				}

			}

			LG.debug("Execution sans erreur ...");

		} catch (DAOException e) {
			e.printStackTrace();
		}

		return responses;
	}
	
	

	// URL : localhost:9095/Pressyze/rest/comments/denial/{factId}
	/**
	 * Permet de recuperer tous les commentaires defavorables du fait dont
	 * l'identifiant est fourni
	 * 
	 * @param factId
	 *            identifiant du fait
	 * @return la liste des commentaires << Contre >>
	 */
	@PermitAll
	@GET
	@Path("/denial/{factId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentResponse> getFactDenialComments(
			@PathParam("factId") String factId) {

		LG.debug(
				"Recuperation de tous les commentaires reniant le fait dont l'identifiant est {}",
				factId);

		List<CommentResponse> responses = new ArrayList<CommentResponse>();
		try {
			for (Iterator<Comment> iterator = new CommentDAOImpl()
					.findCommentByFactId(factId).iterator(); iterator.hasNext();) {

				Comment comment = iterator.next();
				if (comment.getType() == Comment.DENIAL_COMMENT) {

					CommentResponse response = new CommentResponse();
					response.setId(comment.getId());
					response.setContent(comment.getContent());
					response.setType(comment.getType());

					responses.add(response);
				}

			}
			LG.debug("Execution sans erreur ...");
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return responses;
	}

	
	
	// URL : localhost:9095/Pressyze/rest/comments/all/{factId}
	/**
	 * Permet de recuperer tous les commentaires du fait dont l'identifiant est
	 * fourni
	 * 
	 * @param factId
	 *            identifiant du fait
	 * @return la liste des commentaires (sans exceptions)
	 */
	@PermitAll
	@GET
	@Path("/all/{factId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentResponse> getFactComments(String factId) {

		LG.debug(
				"Recuperation de tous les commentaires relatifs fait dont l'identifiant est {}",
				factId);

		List<CommentResponse> responses = new ArrayList<CommentResponse>();
		try {
			for (Iterator<Comment> iterator = new CommentDAOImpl()
					.findCommentByFactId(factId).iterator(); iterator.hasNext();) {

				Comment comment = iterator.next();

				CommentResponse response = new CommentResponse();
				response.setId(comment.getId());
				response.setContent(comment.getContent());
				response.setType(comment.getType());

				responses.add(response);

			}
			LG.debug("Execution sans erreur ...");
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return responses;
	}

}
