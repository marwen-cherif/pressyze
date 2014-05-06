
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
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.dao.CityDAO;
import org.bytecoders.pressyze.dao.CityDAOImpl;
import org.bytecoders.pressyze.dao.FactDAO;
import org.bytecoders.pressyze.dao.FactDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.bytecoders.pressyze.util.PressyzeUtil;
import org.bytecoders.pressyze.webservices.elements.AnalyseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/analyse")
public class AnalyseResource {

	private static final Logger LOG = LoggerFactory
			.getLogger(AnalyseResource.class);

	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{startDate}/{endDate}/{eventLabel}")
	public List<AnalyseResponse> processAnalyse(
			@PathParam("startDate") String formatedStartDate,
			@PathParam("endDate") String formatedEndDate,
			@PathParam("eventLabel") String eventLabel) {

		LOG.debug("Lancement d'une procedure d'analyse en cours ...");

		List<AnalyseResponse> responses = new ArrayList<AnalyseResponse>();

		FactDAO factDAO = new FactDAOImpl();
		CityDAO cityDAO = new CityDAOImpl();

		long startTime = PressyzeUtil.toDate(formatedStartDate).getTime();
		long endTime = PressyzeUtil.toDate(formatedEndDate).getTime();

		try {

			long occurrence = 0; // Nombre d'occurrence de l'évènement ou de
									// tous les évenements sans distinctions
									// entre les deux dates

			long nbConfirmation = 0; // Nombre de confirmations relatives à ce
										// ou ces évènements
			long nbDenial = 0; // Nombre de reniements
			long nbSpam = 0; // Nombre de déclarations de spam relatives à ces
								// évènements

			if (eventLabel.equalsIgnoreCase("all")) {

				// Calcul relatif à tous les évènements

				for (City city : cityDAO.findAllCities()) {

					AnalyseResponse response = new AnalyseResponse();

					response.setId(city.getId());
					response.setLabel(city.getLabel());
					occurrence = 0;

					nbConfirmation = 0;
					nbDenial = 0;
					nbSpam = 0;
					
					for (Fact fact : factDAO.findAllFacts()) {
						long factTime = fact.getTimestamp();

						if (factTime >= startTime
								&& factTime <= endTime
								&& fact.getCity().getLabel()
										.equalsIgnoreCase(city.getLabel())) {
							occurrence++;
							nbConfirmation += fact.getConfirmation()
									.getCheckers().size();
							nbDenial += fact.getDenial().getDeniers().size();
							nbSpam += fact.getSpam().getDenouncers().size();
						}

					}

					response.setOccurrence(occurrence);
					response.setNbConfirmation(nbConfirmation);
					response.setNbDenial(nbDenial);
					response.setNbSpam(nbSpam);

					responses.add(response);
				}

			} else {

				// Calcul relatif à un évènement en particulier

				for (City city : cityDAO.findAllCities()) {
					AnalyseResponse response = new AnalyseResponse();

					response.setId(city.getId());
					response.setLabel(city.getLabel());
					occurrence = 0;
					
					nbConfirmation = 0;
					nbDenial = 0;
					nbSpam = 0;
					
					for (Fact fact : factDAO.findAllFacts()) {
						long factTime = fact.getTimestamp();

						if (eventLabel.equalsIgnoreCase(fact.getEvent()
								.getLabel())
								&& factTime >= startTime
								&& factTime <= endTime
								&& fact.getCity().getLabel()
										.equalsIgnoreCase(city.getLabel())) {
							occurrence++;
							nbConfirmation += fact.getConfirmation()
									.getCheckers().size();
							nbDenial += fact.getDenial().getDeniers().size();
							nbSpam += fact.getSpam().getDenouncers().size();
						}

					}

					response.setOccurrence(occurrence);
					response.setNbConfirmation(nbConfirmation);
					response.setNbDenial(nbDenial);
					response.setNbSpam(nbSpam);

					responses.add(response);
				}

			}

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return responses;
	}
}
