package aatithya;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Entity class to store Login credentials.
 * implemented using JPA annotations.
 * mapping of this file is done through hibernate.cfg.xml
 * 
 * @author Kiran.Shenoy
 *
 */
@Entity
@Table(name="Login")
public class Login {
	//username as primary key as 
	@Id
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
