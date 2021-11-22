package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the publisher database table.
 * 
 */
@Entity
@NamedQuery(name="Publisher.findAll", query="SELECT p FROM Publisher p")
public class Publisher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idPublisher;

	private String name;

	//bi-directional many-to-one association to Bookinfo
	@OneToMany(mappedBy="publisher")
	private List<Bookinfo> bookinfos;

	public Publisher() {
	}

	public int getIdPublisher() {
		return this.idPublisher;
	}

	public void setIdPublisher(int idPublisher) {
		this.idPublisher = idPublisher;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Bookinfo> getBookinfos() {
		return this.bookinfos;
	}

	public void setBookinfos(List<Bookinfo> bookinfos) {
		this.bookinfos = bookinfos;
	}

	public Bookinfo addBookinfo(Bookinfo bookinfo) {
		getBookinfos().add(bookinfo);
		bookinfo.setPublisher(this);

		return bookinfo;
	}

	public Bookinfo removeBookinfo(Bookinfo bookinfo) {
		getBookinfos().remove(bookinfo);
		bookinfo.setPublisher(null);

		return bookinfo;
	}

}