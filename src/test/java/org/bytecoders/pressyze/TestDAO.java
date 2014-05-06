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
package org.bytecoders.pressyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bytecoders.pressyze.common.City;
import org.bytecoders.pressyze.common.Confirmation;
import org.bytecoders.pressyze.common.Denial;
import org.bytecoders.pressyze.common.Event;
import org.bytecoders.pressyze.common.Fact;
import org.bytecoders.pressyze.common.Spam;
import org.bytecoders.pressyze.common.User;
import org.bytecoders.pressyze.dao.CityDAO;
import org.bytecoders.pressyze.dao.CityDAOImpl;
import org.bytecoders.pressyze.dao.EventDAO;
import org.bytecoders.pressyze.dao.EventDAOImpl;
import org.bytecoders.pressyze.dao.FactDAO;
import org.bytecoders.pressyze.dao.FactDAOImpl;
import org.bytecoders.pressyze.dao.UserDAO;
import org.bytecoders.pressyze.dao.UserDAOImpl;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.junit.Ignore;
import org.junit.Test;

public class TestDAO {

	@Ignore
	@Test
	public void testUsernameExistance() {
		try {
			User user = new UserDAOImpl().findUserByUsername("Marwen Chrif");
			if(user == null) {
				System.out.println("user est null");
			} else
			System.out.println("User : [ " + user.toString() + " ]");
			
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Ignore
	@Test
	public void testGetFacts() {
		try {
			
			List<Fact> f = new ArrayList<Fact>();
			f.addAll(new FactDAOImpl().findAllFacts());
			Collections.sort(f);
			
			for(Fact fact : f) {
				System.out.println(fact);
			}
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Ignore
	@Test
	public void testFactDAO() {
		
		FactDAO dao = new FactDAOImpl();
		Date now = new Date();
		
		for(int i = 0; i < 100; i++) {	
			Fact fact = new Fact();
			
			fact.setId("" + new Date().getTime() + i);
			Event event;
			try {
				event = new EventDAOImpl().findEvent("" + random(2, 10));
				
				fact.setDescription("Description de l'evenement " + event.getLabel());
				
				fact.setTimestamp(Long.valueOf(now.getTime() - 3*24*60*60*1000));
				
				City city = new CityDAOImpl().findCity("" + random(5,24));
				
				User reporter = new UserDAOImpl().findUser("" + random(1,5));
				
				fact.setReporter(reporter);
				fact.setEvent(event);
				fact.setCity(city);
				
				Confirmation cfr = new Confirmation();
				Denial dnl = new Denial();
				
				int j = 0;
				for(User user : new UserDAOImpl().findAllUsers()) {
					if(!user.equals(reporter)) {
						if(j%2 == 0) {
							cfr.addChecker(user);
						} else {
							dnl.addDenier(user);
						}
						j++;
					}
				}
				
				fact.setConfirmation(cfr);
				fact.setDenial(dnl);
				fact.setSpam(new Spam());
				
				dao.addFact(fact);
				
			} catch (DAOException e) {
				
				e.printStackTrace();
			}
		}
		
		System.out.println("fin des inserts des facts");
		
	}
	
	@Ignore
	@Test
	public void testGetCities() {
		try {
			for(City city : new CityDAOImpl().findAllCities()) {
				System.out.println(city);
			}
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testAddCity() {
		String[] cities = new String[] {
			"Ariana", "Béjà", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba", "Kairouan",
			"Kasserine", "Kébili", "Kef", "Mahdia", "Manouba", "Médenine", "Monastir", "Nabeul",
			"Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
		};
		
		CityDAO dao = new CityDAOImpl();
		
		int i = 1;
		
		for(String cityName : cities) {
			City city = new City();
			
			city.setId("" + i);
			city.setLabel(cityName);
			
			try {
				dao.addCity(city);
			} catch (DAOException e) {
				
				e.printStackTrace();
			}
			
			i++;
		}
		
		System.out.println("fin");
	}
	
	
	@Ignore
	@Test
	public void testGetAllUsers() {
		try {
			for(User user : new UserDAOImpl().findAllUsers()) {
				System.out.println(user);
			}
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testUserDAO() {
		
		UserDAO dao = new UserDAOImpl();
		
		String[] values = new String[] {
			"dalas", "melki", "marwen", "chehaibi", "nabil"	
		};
		
		int i = 1;
		
		for(String value : values) {
		
			User user = new User();
			user.setId("" + i);
			user.setUsername(value);
			user.setPassword("cfcd208495d565ef66e7dff9f98764da"); // <- 0 
			user.setJournalist(i%2 == 0 ? true : false);
			
			try {
				dao.addUser(user);
			} catch (DAOException e) {
				
				e.printStackTrace();
			}
			
			i++;
		}
		
	}
	
	
	
	//@Ignore
	@Test
	public void testEventDAO() {
		
		EventDAO dao = new EventDAOImpl();
		
		try {
			for(Event e : dao.findAllEvents()) {
				System.out.println(e);
			}
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testInsertEventDAO() {
		String[] evts = new String[] {
			"Spectacle", "Randonnée", "Visite touristique", "Visite diplomatique", "Accident de voiture", "Accident de moto",	
			"Concours de programmation", "Concours de dessin", "Concours de littérature", "Débat philosophique", "Débat politique",
			"Manifestation populaire", "Grève générale"
		};
		EventDAO dao = new EventDAOImpl();
		int i = 2;
		for(String evt : evts) {
			Event event = new Event();
			event.setId("" + i);
			event.setLabel(evt);
			
			try {
				
				dao.addEvent(event);
				
			} catch (DAOException e) {
				
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("fin des inserts");
	}
	
	public static int random(int bound1, int bound2) {
	    int min = Math.min(bound1, bound2);
	    int max = Math.max(bound1, bound2);
	    return min + (int)(Math.random() * (max - min + 1));
	}
}
