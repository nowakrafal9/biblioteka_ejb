package jsf.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
