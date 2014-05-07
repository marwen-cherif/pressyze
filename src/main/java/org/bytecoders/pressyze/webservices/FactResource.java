/*
// Le présent fichier fait partie du projet PRESSYZE, une application se proposant
// d'encourager le journalisme citoyen et permettant d'avoir une vue globale
// sur les évènements se déroulant sur le sol tunisien.
//
//
// Ce projet entre dans le cadre du concours Java Developer Challenge (Edition 2014)
// organisé par ESPRIT JAVA USER GROUP qui met le focus sur les dernières technologies du monde Java.
//
// Ce projet a été réalisé par l'équipe << ByteCoders >> composée des élèves ingénieurs suivants :
//
// - Mohamed Chehaibi
// - Mohamed Ali Ben Lassoued
// - Mohamed Melki
// - Marwen Chrif
// - Nabil Andriantomanga
//
// Les technologies utilisées sont essentiellement :
//
// AngularJS : un framework JavaScript proposé par Google et présente une méthodologie innovante
// et adaptée au monde de l'industrie, facilite la réalisation des applications mono-page
// et permet la mise en place de plusieurs patrons de conception dont l'MVC.
//
//
// Mongo DB : SGBD NoSQL orientée documents répartissable sur un nombre quelconque d'ordinateurs.
//
// REST JAX-RS 2.8 (Jersey Implementation) : Java API for RESTful Web Services est une interface
// de programmation Java permettant de créer des services Web avec une architecture REST.
//
// Apache Tomcat 7.0.42 : Serveur d'application Java EE.
//
// Maven 3.1 : système de gestion et d'automatisation de production des projets logiciels
// Java en général et Java EE en particulier.
//
 */
package org.bytecoders.pressyze.webservices;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.*;
import org.bytecoders.pressyze.dao.CityDAO;
import org.bytecoders.pressyze.dao.CityDAOImpl;
import org.bytecoders.pressyze.dao.EventDAO;
import org.bytecoders.pressyze.dao.EventDAOImpl;
import org.bytecoders.pressyze.dao.FactDAO;
import org.bytecoders.pressyze.dao.FactDAOImpl;
import org.bytecoders.pressyze.dao.UserDAO;
import org.bytecoders.pressyze.dao.UserDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.bytecoders.pressyze.util.PressyzeUtil;
import org.bytecoders.pressyze.webservices.elements.BooleanResponse;
import org.bytecoders.pressyze.webservices.elements.FactResponse;
import org.bytecoders.pressyze.webservices.elements.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Path("/facts")
public class FactResource {

	private static final Logger LG = LoggerFactory
			.getLogger(FactResource.class);

	/**
	 * Utilisé pour spécifier la reaction d'un utilisateur par rapport à un
	 * fait. Cas : confirmation...
	 */
	private static final int FACT_CONFIRMATION = 1;

	/**
	 * Utilisé pour spécifier la reaction d'un utilisateur par rapport à un
	 * fait. Cas : Reniement...
	 */
	private static final int FACT_DENIAL = 2;

	/**
	 * Utilisé pour spécifier la reaction d'un utilisateur par rapport à un
	 * fait. Cas : spam...
	 */
	private static final int FACT_SPAM = 3;

	/**
	 * Utilisé pour spécifier la reaction d'un utilisateur par rapport à un
	 * fait. Cas : proprietaire...
	 */
	private static final int FACT_OWNER = 4;

	// URL : http://localhost:9095/Pressyze/rest/facts/{userId}/{i}
	/**
	 * Permet de recuperer une liste de fait
	 * 
	 * @param userId
	 *            l'identifiant de l'utilisateur courant (lancant la requete au
	 *            serveur)
	 * @param index
	 *            indice pour la pagination (recuperer les faits par partie)
	 * @return la liste des faits relativement a l'index fourni
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{userId}/{i}")
	@PermitAll
	public List<FactResponse> getLastFacts(@PathParam("userId") String userId,
			@PathParam("i") int index) {

		List<FactResponse> factResponses = new ArrayList<FactResponse>();

		if (index < 0) {
			
			LG.debug("L'index doit etre strictement positif");
			return factResponses;
			
		}

		UserDAO userDAO = new UserDAOImpl();
		User currentUser = null;
		try {
			
			currentUser = userDAO.findUser(userId);
			
		} catch (DAOException e1) {
			
			e1.printStackTrace();
			
		}

		if (currentUser == null) {
			LG.debug("Impossible de trouver un utilisateur ayant l'id : {}",
					userId);
			return factResponses;
		}

		LG.debug("Recuperation des faits de {} jusqu'a {} par {}", index * 10,
				index * 10 + 10, currentUser.getUsername());

		Set<Fact> facts;

		try {
			facts = new FactDAOImpl().findAllFacts();
		} catch (DAOException e) {
			e.printStackTrace();
			facts = new HashSet<Fact>();
		}

		List<Fact> factList = new ArrayList<Fact>();
		factList.addAll(facts);
		Collections.sort(factList);

		final int start = index * 10;

		if (factList.size() > start) {

			User reporter;			// Celui qui raporte l'info ...
			Set<User> checkers;		// Ceux qui ont confirmé ...
			Set<User> deniers;		// Ceux qui ont renié ...
			Set<User> denouncers;	// Ceux qui ont dénoncé ...

			for (int i = start; i < factList.size() && i < start + 10; i++) {

				FactResponse fr 	= new FactResponse();
				Fact fact 			= factList.get(i);

				fr.setId(fact.getId());

				fr.setEvent(fact.getEvent().getLabel());

				checkers 			= fact.getConfirmation().getCheckers();

				deniers 			= fact.getDenial().getDeniers();

				denouncers 			= fact.getSpam().getDenouncers();

				// Contributions de tout le monde
				fr.setConfirmations(checkers.size());
				
				fr.setDenials(deniers.size());
				
				fr.setSpams(denouncers.size());

				// Contributions des journalistes
				fr.setJournalistConfirmations(getJournalistNumber(checkers));

				fr.setJournalistDenials(getJournalistNumber(deniers));

				fr.setJournalistSpams(getJournalistNumber(denouncers));

				fr.setDescription(fact.getDescription());

				UserResponse user 	= new UserResponse();

				reporter 			= fact.getReporter();

				user.setId(reporter.getId());
				
				user.setUsername(reporter.getUsername());
				
				user.setJournalist(reporter.isJournalist());

				fr.setUser(user);
				fr.setDate(PressyzeUtil.convertTime(fact.getTimestamp()));

				// Gestion des reactions de l'utilisateur courant
				if (fact.getReporter()
						.equals(currentUser)) {
					
					fr.setReaction(FACT_OWNER);
					
				} else if (fact.getConfirmation()
						.hasChecker(currentUser)) {
					
					fr.setReaction(FACT_CONFIRMATION);
					
				} else if (fact.getDenial()
						.hasDenier(currentUser)) {
					
					fr.setReaction(FACT_DENIAL);
					
				} else if (fact.getSpam()
						.hasDenouncer(currentUser)) {
					
					fr.setReaction(FACT_SPAM);
				}

				factResponses.add(fr);
			}
		}

		return factResponses;
	}

	// URL :
	// http://localhost:9095/Pressyze/rest/facts/add/{userId}/{eventId}/{description}/{cityId}/{date}
	/**
	 * Permet d'ajouter un nouveau fait
	 * 
	 * @param userId
	 *            l' identifiant de celui qui publie le fait
	 * @param eventId
	 *            l'identifiant de l'evenement associe
	 * @param description
	 *            la description de l'evenement
	 * @param cityId
	 *            l'idendifiant de la ville ou s'est deroule l'evenement
	 * @param date
	 *            date de l'evenement
	 * @return true si succes de l'ajout
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add/{userId}/{eventId}/{description}/{cityId}/{date}")
	public BooleanResponse addNewFact(@PathParam("userId") String userId,
			@PathParam("eventId") String eventId,
			@PathParam("description") String description,
			@PathParam("cityId") String cityId, @PathParam("date") String date) {
		BooleanResponse response = new BooleanResponse();

		response.setValue(true);

		FactDAO factDAO = new FactDAOImpl();
		Fact fact = new Fact();

		UserDAO userDAO = new UserDAOImpl();
		try {

			User user = userDAO.findUser(userId);
			if (user == null) {

				response.setValue(false);
				LG.debug("Utilisateur inexistant");
				return response;

			} else {
				fact.setReporter(user);
			}

		} catch (DAOException e) {

			e.printStackTrace();
			response.setValue(false);
			return response;
		}

		EventDAO eventDAO = new EventDAOImpl();
		try {
			Event event = eventDAO.findEvent(eventId);
			if (event == null) {

				response.setValue(false);
				LG.debug("Evenement inexistant");
				return response;

			} else {
				fact.setEvent(event);
			}
		} catch (DAOException e) {

			e.printStackTrace();
			response.setValue(false);
			return response;
		}

		fact.setDescription(description);

		CityDAO cityDAO = new CityDAOImpl();

		try {

			City city = cityDAO.findCity(cityId);
			if (city == null) {

				response.setValue(false);
				LG.debug("Ville inexistante");
				return response;

			} else {
				fact.setCity(city);
			}

		} catch (DAOException e) {

			e.printStackTrace();
			response.setValue(false);
			return response;
		}

		fact.setTimestamp(PressyzeUtil.toDate(date).getTime());

		try {

			factDAO.addFact(fact);
			LG.debug("Nouveau fait enregistre");

		} catch (DAOException e) {

			e.printStackTrace();
			response.setValue(false);
		}

		return response;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	@RolesAllowed("MEMBER")
	@GET
	@Path("/secured")
	@Produces(MediaType.APPLICATION_JSON)
	public String securedMethod(String value) {

		return "securedData";
	}

	/**
	 * Permet de recuperer le nombre de journalistes parmi la liste des
	 * utilisateurs fournie
	 * 
	 * @param users
	 *            la liste des utilisateurs
	 * @return le nombre de journalistes
	 */
	private long getJournalistNumber(Set<User> users) {
		long counter = 0;
		for (Iterator<User> userIterator = users.iterator(); userIterator
				.hasNext();) {
			if (userIterator.next().isJournalist()) {
				counter++;
			}
		}
		return counter;
	}

}