
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

import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.exceptions.DAOException;

public interface UserDAO {

	/**
	 * Permet de sauvegarder un utilisateur dans la base
	 * 
	 * @param user
	 * @throws DAOException
	 */
	public void addUser(User user)  throws DAOException;
	
	/**
	 * Permet de supprimer un utilisateur de la base
	 * 
	 * @param user
	 * @throws DAOException
	 */
	public void removeUser(User user)  throws DAOException;
	
	/**
	 * Permet de mettre a jour un utilisateur dans la base
	 * 
	 * @param user
	 * @throws DAOException
	 */
	public void updateUser(User user)  throws DAOException;
	
	/**
	 * Permet de recuperer un utilisateur de la base
	 * 
	 * @param id identifiant de l'utilisateur a recuperer
	 * @return l'utilisateur et null si inexistant
	 * @throws DAOException
	 */
	public User findUser(String id) throws DAOException;
	
	/**
	 * Permet de recuperere tous les utilisateurs
	 * 
	 * @return la liste des utilisateurs
	 * @throws DAOException
	 */
	public Set<User> findAllUsers() throws DAOException;
	
	/**
	 * Permet de recuperer un utilisateur en utilisant son nom d'utilisateur
	 * 
	 * @param username le nom d'utilisateur relatif a l'utilisateur a rechercher
	 * @return l'utilisateur et null si inexistant
	 * @throws DAOException
	 */
	public User findUserByUsername(String username) throws DAOException;
	
}
