package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the borrower database table.
 * 
 */
@Entity
@NamedQuery(name="Borrower.findAll", query="SELECT b FROM Borrower b")
public class Borrower implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idBorrower;

	private String address;

	private String city;

	private String email;

	private String name;

	private String phoneNum;

	private String postalCode;

	private byte status;

	private String surname;

	//bi-directional many-to-one association to Borrowed
	@OneToMany(mappedBy="borrower")
	private List<Borrowed> borroweds;

	public Borrower() {
	}

	public int getIdBorrower() {
		return this.idBorrower;
	}

	public void setIdBorrower(int idBorrower) {
		this.idBorrower = idBorrower;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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
		borrowed.setBorrower(this);

		return borrowed;
	}

	public Borrowed removeBorrowed(Borrowed borrowed) {
		getBorroweds().remove(borrowed);
		borrowed.setBorrower(null);

		return borrowed;
	}

}