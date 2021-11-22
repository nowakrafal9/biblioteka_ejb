package jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the bookinfo database table.
 * 
 */
@Entity
@NamedQuery(name="Bookinfo.findAll", query="SELECT b FROM Bookinfo b")
public class Bookinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idTitle;

	private String code;

	private int pages;

	private String title;

	//bi-directional many-to-one association to Author
	@ManyToOne
	@JoinColumn(name="idAuthor")
	private Author author;

	//bi-directional many-to-one association to Genre
	@ManyToOne
	@JoinColumn(name="idGenre")
	private Genre genre;

	//bi-directional many-to-one association to Publisher
	@ManyToOne
	@JoinColumn(name="idPublisher")
	private Publisher publisher;

	//bi-directional many-to-one association to Bookstock
	@OneToMany(mappedBy="bookinfo")
	private List<Bookstock> bookstocks;

	public Bookinfo() {
	}

	public int getIdTitle() {
		return this.idTitle;
	}

	public void setIdTitle(int idTitle) {
		this.idTitle = idTitle;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getPages() {
		return this.pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getAuthor() {
		return this.author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public List<Bookstock> getBookstocks() {
		return this.bookstocks;
	}

	public void setBookstocks(List<Bookstock> bookstocks) {
		this.bookstocks = bookstocks;
	}

	public Bookstock addBookstock(Bookstock bookstock) {
		getBookstocks().add(bookstock);
		bookstock.setBookinfo(this);

		return bookstock;
	}

	public Bookstock removeBookstock(Bookstock bookstock) {
		getBookstocks().remove(bookstock);
		bookstock.setBookinfo(null);

		return bookstock;
	}

}