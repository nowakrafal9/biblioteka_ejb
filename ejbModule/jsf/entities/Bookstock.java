package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the bookstock database table.
 * 
 */
@Entity
@NamedQuery(name="Bookstock.findAll", query="SELECT b FROM Bookstock b")
public class Bookstock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idBook;

	private String code;

	private byte status;

	//bi-directional many-to-one association to Bookinfo
	@ManyToOne
	@JoinColumn(name="idTitle")
	private Bookinfo bookinfo;

	//bi-directional many-to-one association to Borrowed
	@OneToMany(mappedBy="bookstock")
	private List<Borrowed> borroweds;

	public Bookstock() {
	}

	public int getIdBook() {
		return this.idBook;
	}

	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Bookinfo getBookinfo() {
		return this.bookinfo;
	}

	public void setBookinfo(Bookinfo bookinfo) {
		this.bookinfo = bookinfo;
	}

	public List<Borrowed> getBorroweds() {
		return this.borroweds;
	}

	public void setBorroweds(List<Borrowed> borroweds) {
		this.borroweds = borroweds;
	}

	public Borrowed addBorrowed(Borrowed borrowed) {
		getBorroweds().add(borrowed);
		borrowed.setBookstock(this);

		return borrowed;
	}

	public Borrowed removeBorrowed(Borrowed borrowed) {
		getBorroweds().remove(borrowed);
		borrowed.setBookstock(null);

		return borrowed;
	}

}