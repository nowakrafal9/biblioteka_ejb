package jsf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Borrowed;

public class BorrowedDAO {
	private final static String UNIT_NAME = "libraryPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Borrowed borrowed) {
		em.persist(borrowed);
	}

	public Borrowed merge(Borrowed borrowed) {
		return em.merge(borrowed);
	}

	public void remove(Borrowed borrowed) {
		em.remove(em.merge(borrowed));
	}

	public Borrowed find(Object id) {
		return em.find(Borrowed.class, id);
	}

	public List<Borrowed> getFullList() {
		List<Borrowed> list = null;

		Query query = em.createQuery("SELECT b FROM Bookstock b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
