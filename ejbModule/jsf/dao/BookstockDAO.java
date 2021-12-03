package jsf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Bookstock;

@Stateless
public class BookstockDAO {
	private final static String UNIT_NAME = "libraryPU";
	
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Bookstock bookstock) {
		em.persist(bookstock);
	}
	
	public Bookstock merge(Bookstock bookstock) {
		return em.merge(bookstock);
	}

	public void remove(Bookstock bookstock) {
		em.remove(em.merge(bookstock));
	}

	public Bookstock find(Object id) {
		return em.find(Bookstock.class, id);
	}
	
	public List<Bookstock> getFullList() {
		List<Bookstock> list = null;

		Query query = em.createQuery("SELECT b FROM Bookstock b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}