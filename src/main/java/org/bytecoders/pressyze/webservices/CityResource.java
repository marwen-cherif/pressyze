
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.dao.CityDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.bytecoders.pressyze.webservices.elements.CityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/city")
public class CityResource {

	private static final Logger LG = LoggerFactory.getLogger(CityResource.class);
	
	
	@PermitAll
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public List<CityResponse> getAllCities() {
		LG.debug("Recuperation de toutes les villes");
		
		
		List<CityResponse> responses = new ArrayList<CityResponse>();
		try {
			for(City city : new CityDAOImpl().findAllCities()) {
				CityResponse response = new CityResponse();
				response.setId(city.getId());
				response.setLabel(city.getLabel());
				responses.add(response);
			}
		} catch (DAOException e) {
			
			LG.error(e.getMessage());
	
		}
		return responses;
	}
}
