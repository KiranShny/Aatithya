package aatithya;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * An entity class that represents a visitor entry in the database.
 * implements Serializable for the sake of multiple primary keys.
 * implemented using JPA annotations.
 * mapping of this file is done through hibernate.cfg.xml
 * 
 * @author Kiran.Shenoy
 *
 */
@Entity
@Table(name="VisitorDTO")
public class VisitorDTO implements Serializable {
	private static final long serialVersionUID = -5957511460369239112L;
	@Id
	@Column(name="Phone_Number")
	private long phoneNumber;
	@Column(name="Visitor_Name")
	private String name;
	@Column(name="Email")
	private String email;
	@Column(name="Purpose")
	private String purpose;
	@Column(name="Host_Name")
	private String hostName;
	@Id
	@Column(name="Checkin_Time")
	private String checkinTime;
	@Column(name="Checkout_Time")
	private String checkoutTime;
	@Column(name="Date")
	private String Date;
	@Column(name="Status")
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return Date;
	}
	public void setCheckinTime(String checkinTime) {
		this.checkinTime = checkinTime;
	}
	public void setDate(String date) {
		Date = date;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPurpose() {
		return purpose;
	}
	public String getHostName() {
		return hostName;
	}
	public String getCheckoutTime() {
		return checkoutTime;
	}
	public void setCheckoutTime(String checkoutTime) {
		this.checkoutTime = checkoutTime;
	}
	public String getCheckinTime() {
		return checkinTime;
	}
	@Override
	public String toString() {
		return "VisitorDTO [phoneNumber=" + phoneNumber + ", name=" + name + ", email=" + email + ", purpose=" + purpose
				+ ", hostName=" + hostName + "]";
	}
}
