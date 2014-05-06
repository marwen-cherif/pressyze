
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

import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.exceptions.DAOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserDAOImpl implements UserDAO {
	
	private Connection connection = new Connection();
	private DBCollection collection = connection.getDataBase().getCollection("user");
	
	
/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addUser(User user) throws DAOException {
		// TODO Auto-generated method stub

		DBObject userDB = new BasicDBObject("_id",user.getId());
		userDB.put("username", user.getUsername());
		userDB.put("password", user.getPassword());
		if(user.isJournalist())
		{userDB.put("journalist", 1);}
		else {userDB.put("journalist", 0);}
		
		collection.save(userDB);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUser(User user) throws DAOException {
		
		DBObject query = new BasicDBObject("_id",user.getId());
		collection.remove(query);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUser(User user) throws DAOException {
		DBObject userDB = new BasicDBObject("_id",user.getId());
		userDB.put("username", user.getUsername());
		userDB.put("password", user.getPassword());
		if(user.isJournalist())
		{userDB.put("journalist", 1);}
		else {userDB.put("journalist", 0);}
		
		DBObject query = new BasicDBObject("_id",user.getId());

		collection.update(query, userDB);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User findUser(String id) throws DAOException {
		
		DBObject query = new BasicDBObject("_id",id);
		
		DBObject userDB= new BasicDBObject(); 
		
		userDB = collection.findOne(query);
		
		User user = new User();
		
		String id_user= userDB.get("_id").toString();
		user.setId(id_user);
		
		String nom_user=  userDB.get("username").toString();
		user.setUsername(nom_user);
		
		String password_user= userDB.get("password").toString();
		user.setPassword(password_user);
		
		String isJournalist= userDB.get("journalist").toString();
	
		if (isJournalist.equals("0"))
		{user.setJournalist(false);}
		else
			{user.setJournalist(true);}
		
		return user;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<User> findAllUsers() throws DAOException {
		
		Iterator<DBObject> iterator = collection.find().iterator();
		
		Set<User> users = new HashSet<User>();
		
		while(iterator.hasNext())
		{
			BasicDBObject userDB= (BasicDBObject) iterator.next();
			
			User user = new User();
			
			user.setId(userDB.get("_id").toString());
			user.setUsername(userDB.get("username").toString());
			user.setPassword(userDB.get("password").toString());
			String isJournalist= userDB.get("journalist").toString();
			if (isJournalist.equals("0"))
			{user.setJournalist(false);}
			else
				{user.setJournalist(true);}

			
			users.add(user);
			
		}
		
		return users;
	}
	
//////////////////////////////////////////////////////////////////	

	/**
	 * Getter pour la connexion
	 * 
	 * @return la connexion
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Setter pour la connexion
	 * 
	 * @param connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Getter pour la collection
	 * @return la collection 
	 */
	public DBCollection getCollection() {
		return collection;
	}

	/**
	 * Setter pour la collection
	 * 
	 * @param collection
	 */
	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User findUserByUsername(String username) throws DAOException {
		
		DBObject query = new BasicDBObject("username",username);
		
		DBObject userDB= new BasicDBObject(); 
		
		userDB = collection.findOne(query);
		
		if(userDB == null) {
			System.out.println("userDB est null");
			return null;
		}
		
		User user = new User();
		
		String id_user= userDB.get("_id").toString();
		user.setId(id_user);
		
		String nom_user=  userDB.get("username").toString();
		user.setUsername(nom_user);
		
		String password_user= userDB.get("password").toString();
		user.setPassword(password_user);
		
		String isJournalist= userDB.get("journalist").toString();
	
		if (isJournalist.equals("0"))
		{user.setJournalist(false);}
		else
			{user.setJournalist(true);}
		
		return user;
	}
	
	

}
