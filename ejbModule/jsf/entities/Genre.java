package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the genre database table.
 * 
 */
@Entity
@NamedQuery(name="Genre.findAll", query="SELECT g FROM Genre g")
public class Genre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idGenre;

	private String name;

	//bi-directional many-to-one association to Bookinfo
	@OneToMany(mappedBy="genre")
	private List<Bookinfo> bookinfos;

	public Genre() {
	}

	public int getIdGenre() {
		return this.idGenre;
	}

	public void setIdGenre(int idGenre) {
		this.idGenre = idGenre;
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
		bookinfo.setGenre(this);

		return bookinfo;
	}

	public Bookinfo removeBookinfo(Bookinfo bookinfo) {
		getBookinfos().remove(bookinfo);
		bookinfo.setGenre(null);

		return bookinfo;
	}

}