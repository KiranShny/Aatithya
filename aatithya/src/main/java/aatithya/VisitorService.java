package aatithya;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This is a RESTful Web Service.
 * This class implements Spring framework for mapping the url's.
 * Creates both GET and POST API end points, where the user can interact through JSON format.
 * 
 * @author Kiran.Shenoy
 *
 */
@EnableWebMvc
@RestController
@RequestMapping(value="/visitor",produces = "application/json")
public class VisitorService {
	private VisitorDAO vis=new VisitorDAO();

	//method to check-in a visitor
	@RequestMapping(value = "/checkin" ,method = RequestMethod.POST, consumes="application/json")
	public String makeCheckIn(@RequestBody VisitorDTO visitor) {
		return "{ \"status\" : \""+vis.checkIn(visitor)+"\" }";
	}

	//method to check-out a user
	@RequestMapping(value = "/checkout" ,method = RequestMethod.POST, consumes="application/json")
	public String makeCheckOut(@RequestBody VisitorDTO co) {
		return "{ \"status\" : \""+vis.checkOut(co.getPhoneNumber())+"\" }";
	}

	//method to display all the  visitors  ever visiteed.
	@RequestMapping(value = "/getall" ,method = RequestMethod.GET)
	public ResponseEntity<List<VisitorDTO>> getAllVisitorList() {
		List<VisitorDTO> list = vis.getAllVisitors();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	//method to get all the visitors of current date
	@RequestMapping(value = "/getallThisDay" ,method = RequestMethod.GET)
	public ResponseEntity<List<VisitorDTO>> getAllVisitorListOfThisDay() {
		List<VisitorDTO> list = vis.getTodaysVisitors();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	//method to get all the visitors of current month
	@RequestMapping(value = "/getallThisMonth" ,method = RequestMethod.GET)
	public ResponseEntity<List<VisitorDTO>> getAllVisitorListOfThisMonth() {
		List<VisitorDTO> list = vis.getAllVisitorsOfThisMonth();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	//method to log-in
	@RequestMapping(value = "/login" ,method = RequestMethod.POST, consumes="application/json")
	public String makeLogIn(@RequestBody Login login) {
		return "{ \"status\" : \""+vis.checkLoginObject(login)+"\" }";
	}

	//method to add a new set of  credentials
	@RequestMapping(value = "/loginNew" ,method = RequestMethod.POST, consumes="application/json")
	public String storeLogIn(@RequestBody Login login) {
		return "{ \"status\" : \""+vis.createLogin(login)+"\" }";
	}

	//method to return all list of purposes
	@RequestMapping(value = "/getPurpose" ,method = RequestMethod.GET)
	public String getPurposeList() {
		return "[ "+vis.getpurpose()+" ]";
	}

	//method to get the list of hostnames
	@RequestMapping(value = "/getHostName" ,method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> getHostNameList() {
		List<Employee> list = vis.getHostName();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	//a method to add hosts to db
	@RequestMapping(value = "/addHost" ,method = RequestMethod.POST, consumes="application/json")
	public String addEmployee(@RequestBody Employee employee) {
		return "{ \"status\" : \""+vis.addHosts(employee)+"\" }";
	}
	
	//a method to search a visitor by name
	@RequestMapping(value = "/search" ,method = RequestMethod.GET, consumes="application/json")
	public ResponseEntity<List<VisitorDTO>> getSearch(@RequestBody VisitorDTO visitor) {
		List<VisitorDTO> list = vis.searchVisitor(visitor);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
}
