package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUser;

	private String login;

	private String name;

	private String password;

	private byte status;

	private String surname;

	//bi-directional many-to-one association to Borrowed
	@OneToMany(mappedBy="user")
	private List<Borrowed> borroweds;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="idRole")
	private Role role;

	public User() {
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Borrowed> getBorroweds() {
		return this.borroweds;
	}

	public void setBorroweds(List<Borrowed> borroweds) {
		this.borroweds = borroweds;
	}

	public Borrowed addBorrowed(Borrowed borrowed) {
		getBorroweds().add(borrowed);
		borrowed.setUser(this);

		return borrowed;
	}

	public Borrowed removeBorrowed(Borrowed borrowed) {
		getBorroweds().remove(borrowed);
		borrowed.setUser(null);

		return borrowed;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}