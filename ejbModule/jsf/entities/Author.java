package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the author database table.
 * 
 */
@Entity
@NamedQuery(name="Author.findAll", query="SELECT a FROM Author a")
public class Author implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idAuthor;

	private String name;

	private String surname;

	//bi-directional many-to-one association to Bookinfo
	@OneToMany(mappedBy="author")
	private List<Bookinfo> bookinfos;

	public Author() {
	}

	public int getIdAuthor() {
		return this.idAuthor;
	}

	public void setIdAuthor(int idAuthor) {
		this.idAuthor = idAuthor;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Bookinfo> getBookinfos() {
		return this.bookinfos;
	}

	public void setBookinfos(List<Bookinfo> bookinfos) {
		this.bookinfos = bookinfos;
	}

	public Bookinfo addBookinfo(Bookinfo bookinfo) {
		getBookinfos().add(bookinfo);
		bookinfo.setAuthor(this);

		return bookinfo;
	}

	public Bookinfo removeBookinfo(Bookinfo bookinfo) {
		getBookinfos().remove(bookinfo);
		bookinfo.setAuthor(null);

		return bookinfo;
	}

}