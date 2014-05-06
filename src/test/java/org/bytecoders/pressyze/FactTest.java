package org.bytecoders.pressyze;

import java.util.Date;

import org.bytecoders.pressyze.common.*;
import org.bytecoders.pressyze.dao.*;
import org.bytecoders.pressyze.exceptions.DAOException;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

public class FactTest {

	@AfterClass
	public static void after(){
		P.print("fin des tests");
	}
	
	
	@Test 
	public void testconf() {
		// recuperer le fait 53661715ea6ad80a64103ab0
		// l'user 1 dalas
		//dalas confirme le fait 
		
		try {
			Fact f = new FactDAOImpl().findFact("53661715ea6ad80a64103ab0");
			User u = new UserDAOImpl().findUser("" +1);
			
			f.getConfirmation().addChecker(u);
			
			new FactDAOImpl().updateFact(f);
			
			
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void addConfirmation() {

		FactDAO dao = new FactDAOImpl();
		Date now = new Date();
		
		for(int i = 0; i < 3; i++) {	
			Fact fact = new Fact();
			
			fact.setId("" + new Date().getTime() + i);
			Event event;
			try {
				event = new EventDAOImpl().findEvent("" + 12);
				
				fact.setDescription("Description de l'evenement " + event.getLabel());
				
				fact.setTimestamp(Long.valueOf(now.getTime()));
				
				City city = new CityDAOImpl().findCity("" + 12);
				
				User reporter = new UserDAOImpl().findUser("" + 1);
				
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
	public void checkUser() {
		try {
			for(User u : new UserDAOImpl().findAllUsers()) {
				P.print(u);
			}
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	@Ignore
	@Test 
	public void checkBugAnalyse() {
		
		try {
			int conf = 0;
			int den = 0;
			int spam = 0;
			
			for(Fact fact : new FactDAOImpl().findAllFacts()) {
				
				conf += fact.getConfirmation().getCheckers().size();
				den += fact.getDenial().getDeniers().size();
				spam += fact.getSpam().getDenouncers().size();
				
				
			}
			
			P.print("confirmation : " + conf);
			
			P.print("denial : " + den);
			
			P.print("spam : " + spam);
			
			
		} catch (DAOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	
}
