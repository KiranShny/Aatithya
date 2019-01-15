package aatithya;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
/**
 * A class to  store Host names as per the requirement
 * implemented using JPA annotations.
 * mapping of this file is done through hibernate.cfg.xml
 * 
 * @author Kiran.Shenoy
 *
 */
@Entity
@Table (name="Employee")
public class Employee {
	@Id
	@Column(name="EmployeeId")
	private int employeeid;
	//only this property will be displayed  in frontend
	@Column(name="EmployeeName")
	private String employeename;
	@Column(name="EmployeePhoneNumber")
	private long mobile;
	@Column(name="EmployeeEmailId")
	private String email;
	public int getEmployeeid() {
		return employeeid;
	}
	public String getEmployeename() {
		return employeename;
	}
	public long getMobile() {
		return mobile;
	}
	public Employee() {
		super();
	}
	public Employee(int employeeid, String employeename, long mobile, String email) {
		super();
		this.employeeid = employeeid;
		this.employeename = employeename;
		this.mobile = mobile;
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}
