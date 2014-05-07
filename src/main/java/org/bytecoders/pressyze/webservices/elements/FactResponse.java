
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
package org.bytecoders.pressyze.webservices.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FactResponse {

	private String id;
	
	private String event;
	
	private String description;
	
	private UserResponse user;
	
	private String date;
	
	/*
	 * Contributions des citoyens normaux
	 */
	private long confirmations;
	
	private long denials;

	private long spams;
	
	
	
	/*
	 * contributions des journalistes 
	 */
	private long journalistConfirmations;
	
	private long journalistDenials;
	
	private long journalistSpams;
	
	/*
	 * ===============================
	 * Convention de l'equipe ByteCoders :
	 * 
	 * 		Permet de connaitre si l'utilisateur courant (partie cliente)
	 * 		a reagi a ce fait.
	 * 
	 * 		Les valeurs prévue :
	 * 
	 * 			- 0 	: aucune reaction et le fait n'appartient pas egalement a l'utilisateur 
	 * 			- 1 	: confirmation
	 * 			- 2 	: reniement
	 * 			- 3		: spam
	 * 			- 4 	: l'utilisateur a lui meme publié ce fait (pas de droit de reaction)
	 */
	private int reaction = 0;
	
	
	@XmlElement
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@XmlElement
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}

	@XmlElement
	public long getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(long confirmations) {
		this.confirmations = confirmations;
	}

	@XmlElement
	public long getDenials() {
		return denials;
	}

	public void setDenials(long denials) {
		this.denials = denials;
	}

	@XmlElement
	public long getSpams() {
		return spams;
	}

	public void setSpams(long spams) {
		this.spams = spams;
	}

	@XmlElement
	public int getReaction() {
		return reaction;
	}

	public void setReaction(int reaction) {
		this.reaction = reaction;
	}

	@XmlElement
	public long getJournalistConfirmations() {
		return journalistConfirmations;
	}

	public void setJournalistConfirmations(long journalistConfirmations) {
		this.journalistConfirmations = journalistConfirmations;
	}

	@XmlElement
	public long getJournalistDenials() {
		return journalistDenials;
	}

	public void setJournalistDenials(long journalistDenials) {
		this.journalistDenials = journalistDenials;
	}

	@XmlElement
	public long getJournalistSpams() {
		return journalistSpams;
	}

	public void setJournalistSpams(long journalistSpams) {
		this.journalistSpams = journalistSpams;
	}

	
}
