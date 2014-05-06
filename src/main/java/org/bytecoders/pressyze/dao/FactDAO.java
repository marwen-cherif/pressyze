
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
package org.bytecoders.pressyze.dao;

import java.util.Set;

import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.exceptions.DAOException;

public interface FactDAO {

	/**
	 * Permet de sauvegarder un fait dans la base
	 * 
	 * @param fact
	 * @throws DAOException
	 */
	public void addFact(Fact fact) throws DAOException;
	
	/**
	 * Permet de supprimer un fait de la base
	 * 
	 * @param fact
	 * @throws DAOException
	 */
	public void removeFact(Fact fact) throws DAOException;
	
	/**
	 * Permet de mettre a jour un fait dans la base
	 * 
	 * @param fact
	 * @throws DAOException
	 */
	public void updateFact(Fact fact) throws DAOException;
	
	/**
	 * Permet de recuperer un fait de la base
	 * 
	 * @param id identifiant du fait a recuperer 
	 * @return le fait et null si le fait n'existe pas
	 * @throws DAOException
	 */
	public Fact findFact(String id) throws DAOException;
	
	/**
	 * Permet de recuperer tous les faits de la base
	 * 
	 * @return la liste des faits de la base
	 * @throws DAOException
	 */
	public Set<Fact> findAllFacts() throws DAOException;
	
}
