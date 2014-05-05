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

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class Connection {
	
	private DB dataBase ;
	private MongoClient mongoClient;
	
	
	
	////////////////////////// Constructor /////////////////////////////////////////
	
	public Connection()
	{
		
		
		try
		{
			
			this.mongoClient = new MongoClient("localhost", 27017);
			this.dataBase=mongoClient.getDB("PressYze");
			
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}



	public DB getDataBase() {
		return dataBase;
	}



	public void setDataBase(DB dataBase) {
		this.dataBase = dataBase;
	}



	public MongoClient getMongoClient() {
		return mongoClient;
	}



	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	
	
}
