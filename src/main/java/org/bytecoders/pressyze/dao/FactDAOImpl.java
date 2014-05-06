
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.common.Confirmation;
import org.bytecoders.pressyze.common.Denial;
import org.bytecoders.pressyze.common.Event;
import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.common.Spam;
import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.exceptions.DAOException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class FactDAOImpl implements FactDAO {

	private Connection connection = new Connection();
	private DBCollection collection = connection.getDataBase().getCollection(
			"fact");

	// ///////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFact(Fact fact) throws DAOException {
		// TODO Auto-generated method stub

		DBObject factDB = new BasicDBObject("_id", fact.getId());
		factDB.put("description", fact.getDescription());
		factDB.put("timestamp", fact.getTimestamp());

		// ///////// event ////////////
		DBObject eventDB = new BasicDBObject("_id", fact.getEvent().getId());
		eventDB.put("label", fact.getEvent().getLabel());

		factDB.put("event", eventDB);

		// ///////// city ////////////
		DBObject cityDB = new BasicDBObject("_id", fact.getCity().getId());
		cityDB.put("label", fact.getCity().getLabel());

		factDB.put("city", cityDB);

		// ////////// user////////////
		DBObject userDB = new BasicDBObject("_id", fact.getReporter().getId());
		userDB.put("password", fact.getReporter().getPassword());
		userDB.put("username", fact.getReporter().getUsername());

		if (fact.getReporter().isJournalist()) {
			userDB.put("journalist", 1);
		} else {
			userDB.put("journalist", 0);
		}

		factDB.put("reporter", userDB);

		// /////// confirmation/////////////

		Set<User> users = fact.getConfirmation().getCheckers();

		int ic = 0;
		DBObject listconfirmers = new BasicDBList();
		for (User user : users) {
			DBObject uDB = new BasicDBObject("_id", user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if (user.isJournalist()) {
				uDB.put("journalist", 1);
			} else {
				uDB.put("journalist", 0);
			}

			listconfirmers.put(String.valueOf(ic), uDB);
			ic++;

		}

		factDB.put("confirmation", listconfirmers);

		// /////// denial/////////////

		Set<User> deniers = fact.getDenial().getDeniers();

		int id = 0;
		DBObject listdeniers = new BasicDBList();
		for (User denier : deniers) {
			DBObject uDB = new BasicDBObject("_id", denier.getId());
			uDB.put("username", denier.getUsername());
			uDB.put("password", denier.getPassword());
			if (denier.isJournalist()) {
				uDB.put("journalist", 1);
			} else {
				uDB.put("journalist", 0);
			}

			listdeniers.put(String.valueOf(id), uDB);
			id++;

		}

		factDB.put("denial", listdeniers);

		// /////// Spam/////////////

		Set<User> spammers = fact.getSpam().getDenouncers();

		int is = 0;
		DBObject listspammers = new BasicDBList();
		for (User user : spammers) {
			DBObject uDB = new BasicDBObject("_id", user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if (user.isJournalist()) {
				uDB.put("journalist", 1);
			} else {
				uDB.put("journalist", 0);
			}

			listspammers.put(String.valueOf(is), uDB);
			is++;
		}

		factDB.put("spam", listspammers);
		collection.save(factDB);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFact(Fact fact) throws DAOException {

		DBObject query = new BasicDBObject("_id", fact.getId());
		collection.remove(query);

	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateFact(Fact fact) throws DAOException {

		DBObject factDB = new BasicDBObject("_id", fact.getId());
		factDB.put("description", fact.getDescription());
		factDB.put("timestamp", fact.getTimestamp());

		// ///////// event ////////////
		DBObject eventDB = new BasicDBObject("_id", fact.getEvent().getId());
		eventDB.put("label", fact.getEvent().getLabel());

		factDB.put("event", eventDB);

		// ///////// city ////////////
		DBObject cityDB = new BasicDBObject("_id", fact.getCity().getId());
		cityDB.put("label", fact.getCity().getLabel());

		factDB.put("city", cityDB);

		// ////////// user////////////
		DBObject userDB = new BasicDBObject("_id", fact.getReporter().getId());
		userDB.put("password", fact.getReporter().getPassword());
		userDB.put("username", fact.getReporter().getUsername());

		if (fact.getReporter().isJournalist()) {
			userDB.put("journalist", 1);
		} else {
			userDB.put("journalist", 0);
		}

		factDB.put("reporter", userDB);

		// /////// confirmation/////////////

		Set<User> users = fact.getConfirmation().getCheckers();

		int ic = 0;
		DBObject listconfirmers = new BasicDBList();
		for (User user : users) {
			DBObject uDB = new BasicDBObject("_id", user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if (user.isJournalist()) {
				uDB.put("journalist", 1);
			} else {
				uDB.put("journalist", 0);
			}

			listconfirmers.put(String.valueOf(ic), uDB);
			ic++;

		}

		factDB.put("confirmation", listconfirmers);

		// /////// denial/////////////

		Set<User> deniers = fact.getDenial().getDeniers();

		int id = 0;
		DBObject listdeniers = new BasicDBList();
		for (User denier : deniers) {
			DBObject uDB = new BasicDBObject("_id", denier.getId());
			uDB.put("username", denier.getUsername());
			uDB.put("password", denier.getPassword());
			if (denier.isJournalist()) {
				uDB.put("journalist", 1);
			} else {
				uDB.put("journalist", 0);
			}

			listdeniers.put(String.valueOf(id), uDB);
			id++;

		}

		factDB.put("denial", listdeniers);

		// /////// Spam/////////////

		Set<User> spammers = fact.getSpam().getDenouncers();

		int is = 0;
		DBObject listspammers = new BasicDBList();
		for (User user : spammers) {
			DBObject uDB = new BasicDBObject("_id", user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if (user.isJournalist()) {
				uDB.put("journalist", 1);
			} else {
				uDB.put("journalist", 0);
			}

			listspammers.put(String.valueOf(is), uDB);
			is++;
		}

		factDB.put("spam", listspammers);

		DBObject query = new BasicDBObject("_id", fact.getId());

		collection.update(query, factDB);

	}

	// //////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fact findFact(String id) throws DAOException {

		DBObject query = new BasicDBObject("_id", id);

		DBObject factDB = new BasicDBObject();

		factDB = collection.findOne(query);

		Fact fact = new Fact();

		fact.setId(factDB.get("_id").toString());

		fact.setDescription(factDB.get("description").toString());

		fact.setTimestamp(Long.valueOf(factDB.get("timestamp").toString()));

		// ////////city////////////

		DBObject cityDB = new BasicDBObject();
		cityDB = (DBObject) factDB.get("city");

		City city = new City();
		city.setId(cityDB.get("_id").toString());
		city.setLabel(cityDB.get("label").toString());

		fact.setCity(city);

		// /////////user///////////

		DBObject reporterDB = new BasicDBObject();
		reporterDB = (DBObject) factDB.get("reporter");

		User reporter = new User();
		reporter.setId(reporterDB.get("_id").toString());
		reporter.setPassword(reporterDB.get("password").toString());
		reporter.setUsername(reporterDB.get("username").toString());

		String isJournalist = reporterDB.get("journalist").toString();

		if (isJournalist.equals("0")) {
			reporter.setJournalist(false);
		} else {
			reporter.setJournalist(true);
		}

		fact.setReporter(reporter);

		// /////// Event////////////

		DBObject eventDB = new BasicDBObject();
		eventDB = (DBObject) factDB.get("event");

		Event event = new Event();
		event.setId(eventDB.get("_id").toString());
		event.setLabel(eventDB.get("label").toString());

		fact.setEvent(event);

		// /////////////////////////confirmation///////////////

		DBObject ConfirmationDB = new BasicDBList();

		ConfirmationDB = (DBObject) factDB.get("confirmation");

		Set<User> checkers = new HashSet<User>();

		for (String key : ConfirmationDB.keySet()) {

			DBObject checkerDB = (DBObject) ConfirmationDB.get(key);

			User user = new User();

			String id_user = checkerDB.get("_id").toString();
			user.setId(id_user);

			String nom_user = checkerDB.get("username").toString();
			user.setUsername(nom_user);

			String password_user = checkerDB.get("password").toString();
			user.setPassword(password_user);

			if (checkerDB.get("journalist").toString().equals("0")) {
				user.setJournalist(false);
			} else {
				user.setJournalist(true);
			}

			checkers.add(user);

		}

		Confirmation confirmation = new Confirmation();
		confirmation.setCheckers(checkers);

		fact.setConfirmation(confirmation);

		// ///////deniers///////////

		DBObject denialDB = new BasicDBList();

		denialDB = (DBObject) factDB.get("denial");

		Set<User> deniers = new HashSet<User>();

		for (String key : denialDB.keySet()) {

			DBObject denierDB = (DBObject) denialDB.get(key);

			User user = new User();

			String id_user = denierDB.get("_id").toString();
			user.setId(id_user);

			String nom_user = denierDB.get("username").toString();
			user.setUsername(nom_user);

			String password_user = denierDB.get("password").toString();
			user.setPassword(password_user);

			if (denierDB.get("journalist").toString().equals("0")) {
				user.setJournalist(false);
			} else {
				user.setJournalist(true);
			}

			deniers.add(user);

		}

		Denial denial = new Denial();
		denial.setDeniers(deniers);

		fact.setDenial(denial);

		// ////////spammers/////////////

		DBObject SpamDB = new BasicDBList();

		SpamDB = (DBObject) factDB.get("spam");

		Set<User> spammers = new HashSet<User>();

		for (String key : SpamDB.keySet()) {

			DBObject spammerDB = (DBObject) SpamDB.get(key);

			User user = new User();

			String id_user = spammerDB.get("_id").toString();
			user.setId(id_user);

			String nom_user = spammerDB.get("username").toString();
			user.setUsername(nom_user);

			String password_user = spammerDB.get("password").toString();
			user.setPassword(password_user);

			if (spammerDB.get("journalist").toString().equals("0")) {
				user.setJournalist(false);
			} else {
				user.setJournalist(true);
			}

			spammers.add(user);

		}

		Spam spam = new Spam();
		spam.setDenouncers(spammers);

		fact.setSpam(spam);

		return fact;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Fact> findAllFacts() throws DAOException {

		Iterator<DBObject> iterator = collection.find().iterator();

		Set<Fact> facts = new HashSet<Fact>();

		while (iterator.hasNext()) {
			BasicDBObject factDB = (BasicDBObject) iterator.next();

			Fact fact = new Fact();

			fact.setId(factDB.get("_id").toString());

			fact.setDescription(factDB.get("description").toString());

			fact.setTimestamp(Long.valueOf(factDB.get("timestamp").toString()));

			// ////////city////////////

			DBObject cityDB = new BasicDBObject();
			cityDB = (DBObject) factDB.get("city");

			City city = new City();
			city.setId(cityDB.get("_id").toString());
			city.setLabel(cityDB.get("label").toString());

			fact.setCity(city);

			// /////////user///////////

			DBObject reporterDB = new BasicDBObject();
			reporterDB = (DBObject) factDB.get("reporter");

			User reporter = new User();
			reporter.setId(reporterDB.get("_id").toString());
			reporter.setPassword(reporterDB.get("password").toString());
			reporter.setUsername(reporterDB.get("username").toString());

			String isJournalist = reporterDB.get("journalist").toString();

			if (isJournalist.equals("0")) {
				reporter.setJournalist(false);
			} else {
				reporter.setJournalist(true);
			}

			fact.setReporter(reporter);

			// /////// Event////////////

			DBObject eventDB = new BasicDBObject();
			eventDB = (DBObject) factDB.get("event");

			Event event = new Event();
			event.setId(eventDB.get("_id").toString());
			event.setLabel(eventDB.get("label").toString());

			fact.setEvent(event);

			// /////////////////////////confirmation///////////////

			DBObject ConfirmationDB = new BasicDBList();

			ConfirmationDB = (DBObject) factDB.get("confirmation");

			Set<User> checkers = new HashSet<User>();

			
			for (String key : ConfirmationDB.keySet()) {

				DBObject checkerDB = (DBObject) ConfirmationDB.get(key);

				User user = new User();

				String id_user = checkerDB.get("_id").toString();
				user.setId(id_user);

				String nom_user = checkerDB.get("username").toString();
				user.setUsername(nom_user);

				String password_user = checkerDB.get("password").toString();
				user.setPassword(password_user);

				if (checkerDB.get("journalist").toString().equals("0")) {
					user.setJournalist(false);
				} else {
					user.setJournalist(true);
				}

				checkers.add(user);

			}

			Confirmation confirmation = new Confirmation();
			confirmation.setCheckers(checkers);

			fact.setConfirmation(confirmation);

			// ///////deniers///////////

			DBObject denialDB = new BasicDBList();

			denialDB = (DBObject) factDB.get("denial");

			Set<User> deniers = new HashSet<User>();

			for (String key : denialDB.keySet()) {

				DBObject denierDB = (DBObject) denialDB.get(key);

				User user = new User();

				String id_user = denierDB.get("_id").toString();
				user.setId(id_user);

				String nom_user = denierDB.get("username").toString();
				user.setUsername(nom_user);

				String password_user = denierDB.get("password").toString();
				user.setPassword(password_user);

				if (denierDB.get("journalist").toString().equals("0")) {
					user.setJournalist(false);
				} else {
					user.setJournalist(true);
				}

				deniers.add(user);

			}

			Denial denial = new Denial();
			denial.setDeniers(deniers);

			fact.setDenial(denial);

			// ////////spammers/////////////

			DBObject SpamDB = new BasicDBList();

			SpamDB = (DBObject) factDB.get("spam");

			Set<User> spammers = new HashSet<User>();

			for (String key : SpamDB.keySet()) {

				DBObject spammerDB = (DBObject) SpamDB.get(key);

				User user = new User();

				String id_user = spammerDB.get("_id").toString();
				user.setId(id_user);

				String nom_user = spammerDB.get("username").toString();
				user.setUsername(nom_user);

				String password_user = spammerDB.get("password").toString();
				user.setPassword(password_user);

				if (spammerDB.get("journalist").toString().equals("0")) {
					user.setJournalist(false);
				} else {
					user.setJournalist(true);
				}

				spammers.add(user);

			}

			Spam spam = new Spam();
			spam.setDenouncers(spammers);

			fact.setSpam(spam);

			facts.add(fact);

		}

		return facts;
	}

}
