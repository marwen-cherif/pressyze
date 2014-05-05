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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.common.Event;
import org.bytecoders.pressyze.exceptions.DAOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class EventDAOImpl implements EventDAO {

	private Connection connection = new Connection();
	private DBCollection collection = connection.getDataBase().getCollection(
			"event");

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addEvent(Event event) throws DAOException {
		DBObject eventDB = new BasicDBObject("_id", event.getId());
		eventDB.put("_id", event.getId());
		eventDB.put("label", event.getLabel());
		collection.save(eventDB);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeEvent(Event event) throws DAOException {
		DBObject query = new BasicDBObject("_id", event.getId());
		collection.remove(query);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEvent(Event event) throws DAOException {

		DBObject eventDB = new BasicDBObject("_id", event.getId());
		eventDB.put("_id", event.getId());
		eventDB.put("label", event.getLabel());

		DBObject query = new BasicDBObject("_id", event.getId());
		collection.update(query, eventDB);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event findEvent(String id) throws DAOException {
		DBObject query = new BasicDBObject("_id", id);

		DBObject eventDB = new BasicDBObject();

		eventDB = collection.findOne(query);

		Event event = new Event();

		event.setId(eventDB.get("_id").toString());

		event.setLabel(eventDB.get("label").toString());

		return event;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Event> findAllEvents() throws DAOException {
		Iterator<DBObject> iterator = collection.find().iterator();

		Set<Event> events = new HashSet<Event>();

		while (iterator.hasNext()) {
			BasicDBObject eventDB = (BasicDBObject) iterator.next();

			Event event = new Event();

			event.setId(eventDB.get("_id").toString());

			event.setLabel(eventDB.get("label").toString());

			events.add(event);

		}

		return events;

	}

}
