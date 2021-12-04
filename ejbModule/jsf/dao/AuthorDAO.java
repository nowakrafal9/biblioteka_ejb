package jsf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Author;

@Stateless
public class AuthorDAO {
	private final static String UNIT_NAME = "libraryPU";;
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Author author) {
		em.persist(author);
	}

	public Author merge(Author author) {
		return em.merge(author);
	}
	
	public void remove(Author author) {
		em.remove(em.merge(author));
	}

	public Author find(Object id) {
		return em.find(Author.class, id);
	}
	
	public List<Author> getFullList() {
		List<Author> list = null;

		Query query = em.createQuery("SELECT a FROM Author a");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
