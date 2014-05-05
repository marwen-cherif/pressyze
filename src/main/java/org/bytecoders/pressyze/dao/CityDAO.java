
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
package org.bytecoders.pressyze.dao;

import java.util.Set;

import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.exceptions.DAOException;

public interface CityDAO {

	/**
	 * Permet de sauvegarder une ville dans la base 
	 * 
	 * @param city
	 * @throws DAOException
	 */
	public void addCity(City city) throws DAOException;
	
	/**
	 * Permet de recuperer une ville de la base 
	 * 
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public City findCity(String id) throws DAOException;
	
	/**
	 * Permet de recuperer toutes les villes de la base 
	 * @return
	 * @throws DAOException
	 */
	public Set<City> findAllCities() throws DAOException;
	
	/**
	 * Permet de supprimer une ville de la base 
	 * 
	 * @param city
	 * @throws DAOException
	 */
	public void removeCity(City city) throws DAOException;
	
	/**
	 * Permet de mettre a jour une ville de la base 
	 * 
	 * @param city
	 * @throws DAOException
	 */
	public void updateCity(City city) throws DAOException;
	
}
