
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
import java.util.List;
import java.util.Set;

@Path("/facts")
public class FactResource {

	private static final Logger LG = LoggerFactory
			.getLogger(FactResource.class);

	// URL : http://localhost:9095/Pressyze/rest/facts/{i}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{i}")
	@PermitAll
	public List<FactResponse> getLastFacts(@PathParam("i") int index) {

		LG.debug("Affichage des faits de {} jusqu'a {}", index * 10,
				index * 10 + 10);

		List<FactResponse> factResponses = new ArrayList<FactResponse>();
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

			for (int i = start; i < factList.size() && i < start + 10; i++) {
				
				FactResponse fr = new FactResponse();
				Fact fact = factList.get(i);

				fr.setId(fact.getId());
				fr.setEvent(fact.getEvent().getLabel());

				fr.setConfirmations(fact.getConfirmation().getCheckers().size());
				fr.setDenials(fact.getDenial().getDeniers().size());
				fr.setSpams(fact.getSpam().getDenouncers().size());
				
				
				fr.setDescription(fact.getDescription());

				UserResponse user = new UserResponse();
				user.setId(fact.getReporter().getId());
				user.setUsername(fact.getReporter().getUsername());

				fr.setUser(user);
				fr.setDate(PressyzeUtil.convertTime(fact.getTimestamp()));

				factResponses.add(fr);
			}
		}

		return factResponses;
	}

	// URL : http://localhost:9095/Pressyze/rest/facts/add/{userId}/{eventId}/{description}/{cityId}/{date}
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

}
