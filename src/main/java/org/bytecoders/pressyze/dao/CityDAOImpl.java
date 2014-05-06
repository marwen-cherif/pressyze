
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
//import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.exceptions.DAOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class CityDAOImpl implements CityDAO {
	
	private Connection connection = new Connection();
	private DBCollection collection = connection.getDataBase().getCollection("city");
	
	
/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCity(City city) throws DAOException {
		
		DBObject cityDB = new BasicDBObject("_id",city.getId());
		cityDB.put("_id", city.getId());
		cityDB.put("label", city.getLabel());
		collection.save(cityDB);
		

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public City findCity(String id) throws DAOException {

		DBObject query = new BasicDBObject("_id",id);
		
		DBObject cityDB= new BasicDBObject(); 
		
		cityDB = collection.findOne(query);
		
		City city = new City();
		
		
		city.setId(cityDB.get("_id").toString());

		city.setLabel(cityDB.get("label").toString());
		
		
		return city;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<City> findAllCities() throws DAOException {
		
		Iterator<DBObject> iterator = collection.find().iterator();
		
		Set<City> cities= new HashSet<City>();
		
		while(iterator.hasNext())
		{
			BasicDBObject cityDB= (BasicDBObject) iterator.next();
			
			City city = new City();
			
			city.setId(cityDB.get("_id").toString());

			city.setLabel(cityDB.get("label").toString());
			
			
			cities.add(city);
			
		}	
		
		return cities;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCity(City city) throws DAOException {
		DBObject query = new BasicDBObject("_id",city.getId());
		collection.remove(query);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCity(City city) throws DAOException {
		DBObject cityDB = new BasicDBObject("_id",city.getId());
		cityDB.put("_id", city.getId());
		cityDB.put("label", city.getLabel());
		
		DBObject query = new BasicDBObject("_id",city.getId());
		collection.update(query, cityDB);
		

	}

}
