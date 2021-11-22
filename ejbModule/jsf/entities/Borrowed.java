package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the borrowed database table.
 * 
 */
@Entity
@NamedQuery(name="Borrowed.findAll", query="SELECT b FROM Borrowed b")
public class Borrowed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idBorrowed;

	@Temporal(TemporalType.DATE)
	private Date borrowDate;

	@Temporal(TemporalType.DATE)
	private Date returnDate;

	@Temporal(TemporalType.DATE)
	private Date returnDue;

	private byte status;

	//bi-directional many-to-one association to Bookstock
	@ManyToOne
	@JoinColumn(name="idBook")
	private Bookstock bookstock;

	//bi-directional many-to-one association to Borrower
	@ManyToOne
	@JoinColumn(name="idBorrower")
	private Borrower borrower;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;

	public Borrowed() {
	}

	public int getIdBorrowed() {
		return this.idBorrowed;
	}

	public void setIdBorrowed(int idBorrowed) {
		this.idBorrowed = idBorrowed;
	}

	public Date getBorrowDate() {
		return this.borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getReturnDue() {
		return this.returnDue;
	}

	public void setReturnDue(Date returnDue) {
		this.returnDue = returnDue;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Bookstock getBookstock() {
		return this.bookstock;
	}

	public void setBookstock(Bookstock bookstock) {
		this.bookstock = bookstock;
	}

	public Borrower getBorrower() {
		return this.borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}