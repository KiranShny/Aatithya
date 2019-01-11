package aatithya;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

//to display purpose, can add more in the future
enum purpose
{ 
	Business, Interview, Personal;
} 

/**
 * This class is used to  do all the operations done  by the user such as check-in and check-out.
 * this is implemented using Hibernate.
 * JPA methods of executing a query is also used.
 * 
 * @author Kiran.Shenoy
 *
 */
public class VisitorDAO {
	private final Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
	private final SessionFactory sf=configuration.buildSessionFactory();
	private  Session s ;
	private Transaction tx;

	//method to make a checkin operation
	public String checkIn(VisitorDTO visitor) {
		if (!checkinExists(visitor.getPhoneNumber())) {
			try {
				configureIn();
				visitor.setDate(getTodaysDate());
				visitor.setCheckinTime(getCurrentTime());
				visitor.setStatus("Pending");
				s.save(visitor);
				configureOut();
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
			}
			return "success";
		}
		return "failure";
	}

	//method to make a checkout operation
	public String checkOut(long phone)
	{
		if(checkinExists(phone)) {
			try {
				configureIn();
				Query sqlQuery = s.createSQLQuery("UPDATE VisitorDTO set Status='Completed', Checkout_Time='"+getCurrentTime()
				+"' where Phone_Number="+phone
				+" and Checkout_Time IS NULL");
				sqlQuery.executeUpdate();
				configureOut();
				return "success";
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				return "failure";
			}
		}
		return "failure";
	}

	//method that returns a list of all Visitors ever recorded  by the database.
	@SuppressWarnings("unchecked")
	public  List<VisitorDTO> getAllVisitors() {
		try {
			configureIn();
			List<VisitorDTO> visitorList=  s.createQuery("from VisitorDTO").getResultList();
			configureOut();
			return visitorList;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}

	//a method that returns list of alll visitors of the current month only
	@SuppressWarnings("unchecked")
	public List<VisitorDTO> getAllVisitorsOfThisMonth() {
		try {
			configureIn();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
			String month = simpleDateFormat.format(new Date());
			List<VisitorDTO> list=  s.createQuery("from VisitorDTO where Date like '%"+month+"%'").getResultList();
			configureOut();
			return list;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;		
		}
	}

	//a method that returns true if a user has already checked in with same mobile number
	private Boolean checkinExists(long phoneNumber) {
		try {
			configureIn();
			NativeQuery<?> query = s.createNativeQuery("select 1 from VisitorDTO where Phone_Number="
					+phoneNumber+" and Checkout_Time IS NULL");
			if(query.uniqueResult() != null)
			{
				configureOut();
				return true;
			}
			else
			{
				configureOut();
				return false;
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}

	//a configuration method used by many methods in the class
	private void configureIn() {
		s = sf.openSession();
		tx = s.beginTransaction();
	}

	//to close the configuration resources started by the methods after getting their usage.
	private void configureOut() {
		tx.commit();
		s.close();
	}

	//a method that returns todays date in dd-mmm-yyyy format as a string.
	private String getTodaysDate()
	{
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
			String date = simpleDateFormat.format(new Date());
			return date;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}

	//a method that returns todays time in hh:mm AM/PM format as a string.
	private String getCurrentTime()
	{
		try {
			DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
			String timeString = dateFormat.format(new Date()).toString();
			return timeString;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}

	//a method  that converts a given string to  its MD5 hash, used for hashing passwords.
	private String getMd5(String input) 
	{ 
		try {  
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			byte[] messageDigest = md.digest(input.getBytes()); 
			BigInteger no = new BigInteger(1, messageDigest);  
			String hashtext = no.toString(16); 
			while (hashtext.length() < 32) { 
				hashtext = "0" + hashtext; 
			} 
			return hashtext; 
		}  
		catch (NoSuchAlgorithmException e) { 
			throw new RuntimeException(e); 
		} 
	} 

	///a method  that returns true if the login credentials are valid.
	private Boolean isLoginExists(Login login) {
		try {
			configureIn();
			NativeQuery<?> query = s.createNativeQuery("select 1 from Login where username='"+login.getUsername()
			+"' and  password ='"+login.getPassword()+"'");
			if (query.uniqueResult() != null) {
				configureOut();
				return true;
			}
			else {
				configureOut();
				return false;
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}

	//a method that converts a given username-password pair to its equivalent hash,  by generating a salt.
	public String checkLoginObject(Login login) {
		if(login.getUsername()!=null&&login.getPassword()!=null)
		{	
			int indice=(int)login.getUsername().charAt(3);
			if(indice>=48&&indice<=57) {
				indice = indice-47;
				indice = indice %3;
			}
			else if(indice>=65&&indice<=90) {
				indice = indice-64;
				indice = indice%5;
			}
			else if(indice>=97&&indice<=122) {
				indice = indice-96;
				indice = indice%5;
			}
			if(indice==0)
			{
				indice++;
			}
			int saltMax= login.getPassword().length()%indice;
			String password = login.getPassword() + login.getUsername().charAt(login.getPassword().length() % 3);
			for(int salt=0;salt<=saltMax;salt++) {
				password = getMd5(password);
			}
			login.setPassword(password);
			if(isLoginExists(login)) {
				return "success";
			}
			else {
				return "failure";
			}
		}
		return "failure";
	} 

	//A method to create a new set of username-password, can be used in future for signing up a ADMIN/Mod. 
	public String createLogin(Login login) {
		if(login.getUsername()!=null&&login.getPassword()!=null)
		{	
			int indice=(int)login.getUsername().charAt(3);
			if(indice>=48&&indice<=57) {
				indice = indice-47;
				indice = indice %3;
			} else if(indice>=65&&indice<=90) {
				indice = indice-64;
				indice = indice%5;
			} else if(indice>=97&&indice<=122) {
				indice = indice-96;
				indice = indice%5;
			} else {
				return "invalid";
			}
			if(indice==0)
			{
				indice++;
			}
			int saltMax= login.getPassword().length()%indice;
			String password = login.getPassword() + login.getUsername().charAt(login.getPassword().length() % 3);
			for(int salt=0;salt<=saltMax;salt++) {
				password = getMd5(password);
			}
			login.setPassword(password);
			if(!isLoginExists(login)) {
				try {
					configureIn();
					s.save(login);
					configureOut();
					return "success";
				} catch (Exception e) {
					return null;
				}
			}
			else {
				return "failure";
			}
		}
		return "failure";
	}

	//a  methodd that returns a list of purposes.
	public String getpurpose() {
		List<purpose> purposeList = Arrays.asList(purpose.values());
		String jsonBody = "";
		for(int index=0;index<purposeList.size();index++) {
			jsonBody=jsonBody+"{ \"Purpose\" : \""+purposeList.get(index)+"\"  }, ";
		}
		return jsonBody.substring(0, jsonBody.length()-2);
	}

	// a method that returns a list of employees , used by the front end to display employees
	@SuppressWarnings("unchecked")
	public List<Employee> getHostName() {
		try {
			configureIn();
			List<Employee> listOfEmployees =s.createQuery("from Employee").getResultList();
			configureOut();
			return listOfEmployees;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}

	//A method  that can add an employee to  the emplloyee DB and also display  the name as the host.
	public String addHosts(Employee employee) {
		try {
			configureIn();
			s.save(employee);
			configureOut();
			return "success";
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return "failure";
		}
	}

	//method to return all the visitors enterred on current date
	@SuppressWarnings("unchecked")
	public List<VisitorDTO> getTodaysVisitors() {
		try {
			configureIn();
			List<VisitorDTO> listOfEmployees =s.createQuery("from Employee where date = '"+getTodaysDate()+"'").getResultList();
			configureOut();
			return listOfEmployees;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}
	
	//to Search a visitor by name
	@SuppressWarnings("unchecked")
	public List<VisitorDTO> searchVisitor(VisitorDTO visitor) {
		try {
			configureIn();
			List<VisitorDTO> listOfEmployees =s.createNativeQuery("from Employee where Visitor_Name LIKE '%"+visitor.getName()+"%'").getResultList();
			configureOut();
			return listOfEmployees;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return null;
		}
	}
} 
