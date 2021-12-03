package jsf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Publisher;

public class PublisherDAO {
	private final static String UNIT_NAME = "libraryPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Publisher publisher) {
		em.persist(publisher);
	}

	public Publisher merge(Publisher publisher) {
		return em.merge(publisher);
	}

	public void remove(Publisher publisher) {
		em.remove(em.merge(publisher));
	}

	public Publisher find(Object id) {
		return em.find(Publisher.class, id);
	}

	public List<Publisher> getFullList() {
		List<Publisher> list = null;

		Query query = em.createQuery("SELECT b FROM Bookstock b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
