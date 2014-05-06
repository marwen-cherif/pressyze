

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.common.Comment;
import org.bytecoders.pressyze.common.Confirmation;
import org.bytecoders.pressyze.common.Denial;
import org.bytecoders.pressyze.common.Event;
import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.common.Spam;
import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.exceptions.DAOException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class CommentDAOImpl implements CommentDAO {
	
	
	
	private Connection connection = new Connection();
	private DBCollection collection = connection.getDataBase().getCollection("comment");
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComment(Comment comment) throws DAOException {
		
		
		DBObject commentDB = new BasicDBObject("_id",comment.getId());
		commentDB.put("content", comment.getContent());
		commentDB.put("type", comment.getType());
		
		
/////////////////// fact
		
		Fact fact = comment.getFact();
		DBObject factDB = new BasicDBObject("_id",fact.getId());
		factDB.put("description", fact.getDescription());
		factDB.put("timestamp", fact.getTimestamp());

		DBObject eventDB = new BasicDBObject("_id",fact.getEvent().getId());
		eventDB.put("label",fact.getEvent().getLabel());
		factDB.put("event", eventDB);

		DBObject cityDB = new BasicDBObject("_id",fact.getCity().getId());
		cityDB.put("label",fact.getCity().getLabel());
		factDB.put("city", cityDB);

		DBObject userDB = new BasicDBObject("_id",fact.getReporter().getId());
		userDB.put("password",fact.getReporter().getPassword());
		userDB.put("username",fact.getReporter().getUsername());
		if(fact.getReporter().isJournalist())
		{userDB.put("journalist", 1);}
		else {userDB.put("journalist", 0);}
		factDB.put("reporter", userDB);

		Set<User> users = fact.getConfirmation().getCheckers();
				int ic = 0 ;
				DBObject listconfirmers = new BasicDBList();
		for (User user : users)
		{
			DBObject uDB = new BasicDBObject("_id",user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if(user.isJournalist())
			{uDB.put("journalist", 1);}
			else {uDB.put("journalist", 0);}
			listconfirmers.put(String.valueOf(ic), uDB);
			ic++;	
		}
		factDB.put("confirmation", listconfirmers);
		

		Set<User> deniers = fact.getDenial().getDeniers();
		int id = 0 ;
		DBObject listdeniers = new BasicDBList();
		for (User denier : deniers)
		{
			DBObject uDB = new BasicDBObject("_id",denier.getId());
			uDB.put("username",denier.getUsername());
			uDB.put("password", denier.getPassword());
			if(denier.isJournalist())
			{uDB.put("journalist", 1);}
			else {uDB.put("journalist", 0);}
			listdeniers.put(String.valueOf(id), uDB);
			id++;
		}
		factDB.put("denial", listdeniers);

		
		Set<User> spammers = fact.getSpam().getDenouncers();
		int is = 0 ;
		DBObject listspammers = new BasicDBList();
		for (User user : spammers)
		{
			DBObject uDB = new BasicDBObject("_id",user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if(user.isJournalist())
			{uDB.put("journalist", 1);}
			else {uDB.put("journalist", 0);}
			listspammers.put(String.valueOf(is), uDB);
			is++;
		}		
		
		factDB.put("spam", listspammers);
		
		commentDB.put("fact", factDB);

		
/////////////////// user
		User userComment = comment.getUser();
		
		DBObject userCommentDB = new BasicDBObject("_id",userComment.getId());
		userCommentDB.put("username", userComment.getUsername());
		userCommentDB.put("password", userComment.getPassword());
		if(userComment.isJournalist())
		{userCommentDB.put("journalist", 1);}
		else {userCommentDB.put("journalist", 0);}
		
		commentDB.put("user", userCommentDB);
		
		collection.save(commentDB);

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComment(Comment comment) throws DAOException {
		DBObject query = new BasicDBObject("_id",comment.getId());
		collection.remove(query);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateComment(Comment comment) throws DAOException {
		DBObject query = new BasicDBObject("_id",comment.getId());
		
		DBObject commentDB = new BasicDBObject("_id",comment.getId());
		commentDB.put("content", comment.getContent());
		commentDB.put("type", comment.getType());
		
		
/////////////////// fact
		
		Fact fact = comment.getFact();
		DBObject factDB = new BasicDBObject("_id",fact.getId());
		factDB.put("description", fact.getDescription());
		factDB.put("timestamp", fact.getTimestamp());

		DBObject eventDB = new BasicDBObject("_id",fact.getEvent().getId());
		eventDB.put("label",fact.getEvent().getLabel());
		factDB.put("event", eventDB);

		DBObject cityDB = new BasicDBObject("_id",fact.getCity().getId());
		cityDB.put("label",fact.getCity().getLabel());
		factDB.put("city", cityDB);

		DBObject userDB = new BasicDBObject("_id",fact.getReporter().getId());
		userDB.put("password",fact.getReporter().getPassword());
		userDB.put("username",fact.getReporter().getUsername());
		if(fact.getReporter().isJournalist())
		{userDB.put("journalist", 1);}
		else {userDB.put("journalist", 0);}
		factDB.put("reporter", userDB);

		Set<User> users = fact.getConfirmation().getCheckers();
				int ic = 0 ;
				DBObject listconfirmers = new BasicDBList();
		for (User user : users)
		{
			DBObject uDB = new BasicDBObject("_id",user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if(user.isJournalist())
			{uDB.put("journalist", 1);}
			else {uDB.put("journalist", 0);}
			listconfirmers.put(String.valueOf(ic), uDB);
			ic++;	
		}
		factDB.put("confirmation", listconfirmers);
		

		Set<User> deniers = fact.getDenial().getDeniers();
		int id = 0 ;
		DBObject listdeniers = new BasicDBList();
		for (User denier : deniers)
		{
			DBObject uDB = new BasicDBObject("_id",denier.getId());
			uDB.put("username",denier.getUsername());
			uDB.put("password", denier.getPassword());
			if(denier.isJournalist())
			{uDB.put("journalist", 1);}
			else {uDB.put("journalist", 0);}
			listdeniers.put(String.valueOf(id), uDB);
			id++;
		}
		factDB.put("denial", listdeniers);

		
		Set<User> spammers = fact.getSpam().getDenouncers();
		int is = 0 ;
		DBObject listspammers = new BasicDBList();
		for (User user : spammers)
		{
			DBObject uDB = new BasicDBObject("_id",user.getId());
			uDB.put("username", user.getUsername());
			uDB.put("password", user.getPassword());
			if(user.isJournalist())
			{uDB.put("journalist", 1);}
			else {uDB.put("journalist", 0);}
			listspammers.put(String.valueOf(is), uDB);
			is++;
		}		
		
		factDB.put("spam", listspammers);
		
		commentDB.put("fact", factDB);

		
/////////////////// user
		User userComment = comment.getUser();
		
		DBObject userCommentDB = new BasicDBObject("_id",userComment.getId());
		userCommentDB.put("username", userComment.getUsername());
		userCommentDB.put("password", userComment.getPassword());
		if(userComment.isJournalist())
		{userCommentDB.put("journalist", 1);}
		else {userCommentDB.put("journalist", 0);}
		
		commentDB.put("user", userCommentDB);
		
		
		
		collection.update(query, commentDB);
		
		
		

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comment findComment(String commentId) throws DAOException {
		
		DBObject query = new BasicDBObject("_id",commentId);
		DBObject commentDB= new BasicDBObject(); 
		commentDB = collection.findOne(query);
		
		Comment comment = new Comment();

		comment.setId(commentDB.get("_id").toString());
		comment.setContent(commentDB.get("content").toString());
		comment.setType(Integer.parseInt(commentDB.get("type").toString()));
		
		
		
		///////////// fact /////////
		DBObject factDB= (DBObject) commentDB.get("fact"); 
		Fact fact = new Fact();		
		fact.setId(factDB.get("_id").toString());		
		fact.setDescription(factDB.get("description").toString());
		fact.setTimestamp(Long.valueOf(factDB.get("timestamp").toString()));

		DBObject cityDB = new BasicDBObject();
		cityDB=(DBObject) factDB.get("city");
		City city = new City();
		city.setId(cityDB.get("_id").toString());
		city.setLabel(cityDB.get("label").toString());
		fact.setCity(city);

		DBObject reporterDB = new BasicDBObject();
		reporterDB=(DBObject) factDB.get("reporter");
		User reporter = new User();
		reporter.setId(reporterDB.get("_id").toString());
		reporter.setPassword(reporterDB.get("password").toString());
		reporter.setUsername(reporterDB.get("username").toString());
		String isJournalist= reporterDB.get("journalist").toString();
		if (isJournalist.equals("0"))
		{reporter.setJournalist(false);}
		else
			{reporter.setJournalist(true);}
		fact.setReporter(reporter);

		DBObject eventDB = new BasicDBObject();
		eventDB=(DBObject) factDB.get("event");		
		Event event= new Event();
		event.setId(eventDB.get("_id").toString());
		event.setLabel(eventDB.get("label").toString());
		fact.setEvent(event);

		DBObject ConfirmationDB = new BasicDBList();
		ConfirmationDB=  (DBObject) factDB.get("confirmation");
		Set<User> checkers = new HashSet<User>();
		for( String key : ConfirmationDB.keySet() )
		{
			DBObject checkerDB= (DBObject) ConfirmationDB.get(key);
			User user = new User();
			String id_user= checkerDB.get("_id").toString();
			user.setId(id_user);
			String nom_user=  checkerDB.get("username").toString();
			user.setUsername(nom_user);
			String password_user= checkerDB.get("password").toString();
			user.setPassword(password_user);
			if (checkerDB.get("journalist").toString().equals("0"))
			{user.setJournalist(false);}
			else
				{user.setJournalist(true);}
			checkers.add(user);	
			}
		Confirmation confirmation= new Confirmation();
		confirmation.setCheckers(checkers);
		fact.setConfirmation(confirmation);

		DBObject denialDB = new BasicDBList();
		denialDB=  (DBObject) factDB.get("denial");		
		Set<User> deniers = new HashSet<User>();		
		for( String key : denialDB.keySet() )
		{		
			DBObject denierDB= (DBObject) denialDB.get(key);			
			User user = new User();			
			String id_user= denierDB.get("_id").toString();
			user.setId(id_user);			
			String nom_user=  denierDB.get("username").toString();
			user.setUsername(nom_user);			
			String password_user= denierDB.get("password").toString();
			user.setPassword(password_user);
			if (denierDB.get("journalist").toString().equals("0"))
			{user.setJournalist(false);}
			else
				{user.setJournalist(true);}
			deniers.add(user);
		}
		Denial denial= new Denial();
		denial.setDeniers(deniers);
		fact.setDenial(denial);
	
		DBObject SpamDB = new BasicDBList();
		SpamDB=  (DBObject) factDB.get("spam");		
		Set<User> spammers = new HashSet<User>();		
		for( String key : SpamDB.keySet() )
		{		
			DBObject spammerDB= (DBObject) SpamDB.get(key);		
			User user = new User();		
			String id_user= spammerDB.get("_id").toString();
			user.setId(id_user);			
			String nom_user=  spammerDB.get("username").toString();
			user.setUsername(nom_user);		
			String password_user= spammerDB.get("password").toString();
			user.setPassword(password_user);
			if (spammerDB.get("journalist").toString().equals("0"))
			{user.setJournalist(false);}
			else
				{user.setJournalist(true);}		
			spammers.add(user);
		}
		Spam spam = new Spam();
		spam.setDenouncers(spammers);
		fact.setSpam(spam);
		comment.setFact(fact);
		
		////////// user
		
		DBObject userCommentDB= (DBObject) commentDB.get("user"); 
		
		User userComment = new User();
		
		String id_user= userCommentDB.get("_id").toString();
		userComment.setId(id_user);
		
		String nom_user=  userCommentDB.get("username").toString();
		userComment.setUsername(nom_user);
		
		String password_user= userCommentDB.get("password").toString();
		userComment.setPassword(password_user);
		
		String isJournalistComment= userCommentDB.get("journalist").toString();
	
		if (isJournalistComment.equals("0"))
		{userComment.setJournalist(false);}
		else
			{userComment.setJournalist(true);}

		comment.setUser(userComment);
//////////

		
		
		
		return comment;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Comment> findCommentByFactId(String factId) throws DAOException {
		
		DBObject query = new BasicDBObject("fact._id",factId);
		Iterator<DBObject> iterator = collection.find(query).iterator();
		
		List<Comment> comments = new ArrayList<Comment>();
		
		while(iterator.hasNext())
		{
			BasicDBObject commentDB= (BasicDBObject) iterator.next();
			
			Comment comment = new Comment();

			comment.setId(commentDB.get("_id").toString());
			comment.setContent(commentDB.get("content").toString());
			comment.setType(Integer.parseInt(commentDB.get("type").toString()));
			
			
			
			///////////// fact /////////
			DBObject factDB= (DBObject) commentDB.get("fact"); 
			Fact fact = new Fact();		
			fact.setId(factDB.get("_id").toString());		
			fact.setDescription(factDB.get("description").toString());
			fact.setTimestamp(Long.valueOf(factDB.get("timestamp").toString()));

			DBObject cityDB = new BasicDBObject();
			cityDB=(DBObject) factDB.get("city");
			City city = new City();
			city.setId(cityDB.get("_id").toString());
			city.setLabel(cityDB.get("label").toString());
			fact.setCity(city);

			DBObject reporterDB = new BasicDBObject();
			reporterDB=(DBObject) factDB.get("reporter");
			User reporter = new User();
			reporter.setId(reporterDB.get("_id").toString());
			reporter.setPassword(reporterDB.get("password").toString());
			reporter.setUsername(reporterDB.get("username").toString());
			String isJournalist= reporterDB.get("journalist").toString();
			if (isJournalist.equals("0"))
			{reporter.setJournalist(false);}
			else
				{reporter.setJournalist(true);}
			fact.setReporter(reporter);

			DBObject eventDB = new BasicDBObject();
			eventDB=(DBObject) factDB.get("event");		
			Event event= new Event();
			event.setId(eventDB.get("_id").toString());
			event.setLabel(eventDB.get("label").toString());
			fact.setEvent(event);

			DBObject ConfirmationDB = new BasicDBList();
			ConfirmationDB=  (DBObject) factDB.get("confirmation");
			Set<User> checkers = new HashSet<User>();
			for( String key : ConfirmationDB.keySet() )
			{
				DBObject checkerDB= (DBObject) ConfirmationDB.get(key);
				User user = new User();
				String id_user= checkerDB.get("_id").toString();
				user.setId(id_user);
				String nom_user=  checkerDB.get("username").toString();
				user.setUsername(nom_user);
				String password_user= checkerDB.get("password").toString();
				user.setPassword(password_user);
				if (checkerDB.get("journalist").toString().equals("0"))
				{user.setJournalist(false);}
				else
					{user.setJournalist(true);}
				checkers.add(user);	
				}
			Confirmation confirmation= new Confirmation();
			confirmation.setCheckers(checkers);
			fact.setConfirmation(confirmation);

			DBObject denialDB = new BasicDBList();
			denialDB=  (DBObject) factDB.get("denial");		
			Set<User> deniers = new HashSet<User>();		
			for( String key : denialDB.keySet() )
			{		
				DBObject denierDB= (DBObject) denialDB.get(key);			
				User user = new User();			
				String id_user= denierDB.get("_id").toString();
				user.setId(id_user);			
				String nom_user=  denierDB.get("username").toString();
				user.setUsername(nom_user);			
				String password_user= denierDB.get("password").toString();
				user.setPassword(password_user);
				if (denierDB.get("journalist").toString().equals("0"))
				{user.setJournalist(false);}
				else
					{user.setJournalist(true);}
				deniers.add(user);
			}
			Denial denial= new Denial();
			denial.setDeniers(deniers);
			fact.setDenial(denial);
		
			DBObject SpamDB = new BasicDBList();
			SpamDB=  (DBObject) factDB.get("spam");		
			Set<User> spammers = new HashSet<User>();		
			for( String key : SpamDB.keySet() )
			{		
				DBObject spammerDB= (DBObject) SpamDB.get(key);		
				User user = new User();		
				String id_user= spammerDB.get("_id").toString();
				user.setId(id_user);			
				String nom_user=  spammerDB.get("username").toString();
				user.setUsername(nom_user);		
				String password_user= spammerDB.get("password").toString();
				user.setPassword(password_user);
				if (spammerDB.get("journalist").toString().equals("0"))
				{user.setJournalist(false);}
				else
					{user.setJournalist(true);}		
				spammers.add(user);
			}
			Spam spam = new Spam();
			spam.setDenouncers(spammers);
			fact.setSpam(spam);
			comment.setFact(fact);
			
			////////// user
			
			DBObject userCommentDB= (DBObject) commentDB.get("user"); 
			
			User userComment = new User();
			
			String id_user= userCommentDB.get("_id").toString();
			userComment.setId(id_user);
			
			String nom_user=  userCommentDB.get("username").toString();
			userComment.setUsername(nom_user);
			
			String password_user= userCommentDB.get("password").toString();
			userComment.setPassword(password_user);
			
			String isJournalistComment= userCommentDB.get("journalist").toString();
		
			if (isJournalistComment.equals("0"))
			{userComment.setJournalist(false);}
			else
				{userComment.setJournalist(true);}

			comment.setUser(userComment);
			
	//////////

			comments.add(comment);

		}
		
		return comments;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Comment> findCommentByFactIdAndUserId(String factId,
			String userId) throws DAOException {

		
//		BasicDBObject andQuery = new BasicDBObject();
//		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//		obj.add(new BasicDBObject("fact._id", factId));
//		obj.add(new BasicDBObject("user._id", userId));
//		andQuery.put("$and", obj);
		
		DBObject query = BasicDBObjectBuilder.start("fact._id", factId).add("user._id", userId).get();
		
		Iterator<DBObject> iterator = collection.find(query).iterator();

		
		List<Comment> comments = new ArrayList<Comment>();
		
		while(iterator.hasNext())
		{
			BasicDBObject commentDB= (BasicDBObject) iterator.next();
			
			Comment comment = new Comment();

			comment.setId(commentDB.get("_id").toString());
			comment.setContent(commentDB.get("content").toString());
			comment.setType(Integer.parseInt(commentDB.get("type").toString()));
			
			
			
			///////////// fact /////////
			DBObject factDB= (DBObject) commentDB.get("fact"); 
			Fact fact = new Fact();		
			fact.setId(factDB.get("_id").toString());		
			fact.setDescription(factDB.get("description").toString());
			fact.setTimestamp(Long.valueOf(factDB.get("timestamp").toString()));

			DBObject cityDB = new BasicDBObject();
			cityDB=(DBObject) factDB.get("city");
			City city = new City();
			city.setId(cityDB.get("_id").toString());
			city.setLabel(cityDB.get("label").toString());
			fact.setCity(city);

			DBObject reporterDB = new BasicDBObject();
			reporterDB=(DBObject) factDB.get("reporter");
			User reporter = new User();
			reporter.setId(reporterDB.get("_id").toString());
			reporter.setPassword(reporterDB.get("password").toString());
			reporter.setUsername(reporterDB.get("username").toString());
			String isJournalist= reporterDB.get("journalist").toString();
			if (isJournalist.equals("0"))
			{reporter.setJournalist(false);}
			else
				{reporter.setJournalist(true);}
			fact.setReporter(reporter);

			DBObject eventDB = new BasicDBObject();
			eventDB=(DBObject) factDB.get("event");		
			Event event= new Event();
			event.setId(eventDB.get("_id").toString());
			event.setLabel(eventDB.get("label").toString());
			fact.setEvent(event);

			DBObject ConfirmationDB = new BasicDBList();
			ConfirmationDB=  (DBObject) factDB.get("confirmation");
			Set<User> checkers = new HashSet<User>();
			for( String key : ConfirmationDB.keySet() )
			{
				DBObject checkerDB= (DBObject) ConfirmationDB.get(key);
				User user = new User();
				String id_user= checkerDB.get("_id").toString();
				user.setId(id_user);
				String nom_user=  checkerDB.get("username").toString();
				user.setUsername(nom_user);
				String password_user= checkerDB.get("password").toString();
				user.setPassword(password_user);
				if (checkerDB.get("journalist").toString().equals("0"))
				{user.setJournalist(false);}
				else
					{user.setJournalist(true);}
				checkers.add(user);	
				}
			Confirmation confirmation= new Confirmation();
			confirmation.setCheckers(checkers);
			fact.setConfirmation(confirmation);

			DBObject denialDB = new BasicDBList();
			denialDB=  (DBObject) factDB.get("denial");		
			Set<User> deniers = new HashSet<User>();		
			for( String key : denialDB.keySet() )
			{		
				DBObject denierDB= (DBObject) denialDB.get(key);			
				User user = new User();			
				String id_user= denierDB.get("_id").toString();
				user.setId(id_user);			
				String nom_user=  denierDB.get("username").toString();
				user.setUsername(nom_user);			
				String password_user= denierDB.get("password").toString();
				user.setPassword(password_user);
				if (denierDB.get("journalist").toString().equals("0"))
				{user.setJournalist(false);}
				else
					{user.setJournalist(true);}
				deniers.add(user);
			}
			Denial denial= new Denial();
			denial.setDeniers(deniers);
			fact.setDenial(denial);
		
			DBObject SpamDB = new BasicDBList();
			SpamDB=  (DBObject) factDB.get("spam");		
			Set<User> spammers = new HashSet<User>();		
			for( String key : SpamDB.keySet() )
			{		
				DBObject spammerDB= (DBObject) SpamDB.get(key);		
				User user = new User();		
				String id_user= spammerDB.get("_id").toString();
				user.setId(id_user);			
				String nom_user=  spammerDB.get("username").toString();
				user.setUsername(nom_user);		
				String password_user= spammerDB.get("password").toString();
				user.setPassword(password_user);
				if (spammerDB.get("journalist").toString().equals("0"))
				{user.setJournalist(false);}
				else
					{user.setJournalist(true);}		
				spammers.add(user);
			}
			Spam spam = new Spam();
			spam.setDenouncers(spammers);
			fact.setSpam(spam);
			comment.setFact(fact);
			
			////////// user
			
			DBObject userCommentDB= (DBObject) commentDB.get("user"); 
			
			User userComment = new User();
			
			String id_user= userCommentDB.get("_id").toString();
			userComment.setId(id_user);
			
			String nom_user=  userCommentDB.get("username").toString();
			userComment.setUsername(nom_user);
			
			String password_user= userCommentDB.get("password").toString();
			userComment.setPassword(password_user);
			
			String isJournalistComment= userCommentDB.get("journalist").toString();
		
			if (isJournalistComment.equals("0"))
			{userComment.setJournalist(false);}
			else
				{userComment.setJournalist(true);}

			comment.setUser(userComment);
			
	//////////

			comments.add(comment);

		}
		
		return comments;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User findCommentUser(String commentId) throws DAOException {
		
		DBObject query = new BasicDBObject("_id",commentId);
		DBObject commentDB= new BasicDBObject(); 
		commentDB = collection.findOne(query);
		
//////////user
		
		DBObject userCommentDB= (DBObject) commentDB.get("user"); 
		
		User userComment = new User();
		
		String id_user= userCommentDB.get("_id").toString();
		userComment.setId(id_user);
		
		String nom_user=  userCommentDB.get("username").toString();
		userComment.setUsername(nom_user);
		
		String password_user= userCommentDB.get("password").toString();
		userComment.setPassword(password_user);
		
		String isJournalistComment= userCommentDB.get("journalist").toString();
	
		if (isJournalistComment.equals("0"))
		{userComment.setJournalist(false);}
		else
			{userComment.setJournalist(true);}

//////////

		return userComment;
	}

}
