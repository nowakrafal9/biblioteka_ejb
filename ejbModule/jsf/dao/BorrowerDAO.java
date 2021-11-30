package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Borrower;

@Stateless
public class BorrowerDAO {
	private final static String UNIT_NAME = "libraryPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Borrower borrower) {
		em.persist(borrower);
	}

	public Borrower merge(Borrower borrower) {
		return em.merge(borrower);
	}

	public void remove(Borrower borrower) {
		em.remove(em.merge(borrower));
	}

	public Borrower find(Object id) {
		return em.find(Borrower.class, id);
	}

	public List<Borrower> getFullList() {
		List<Borrower> list = null;

		Query query = em.createQuery("SELECT b FROM Borrower b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Borrower> getList(Map<String, Object> filterParams) {
		List<Borrower> list = null;

		String where = "";
		String name = (String) filterParams.get("name");
		String surname = (String) filterParams.get("surname");
		byte status = (byte) filterParams.get("status");

		if (status == 0 || status == 1) {
			where = "where b.status =:status ";
		}
		where = this.createWhere("name", name, where);
		where = this.createWhere("surname", surname, where);

		Query query = em.createQuery("SELECT b FROM Borrower b " + where);
		
		if(status == 0 || status == 1) {
			query.setParameter("status", status);
		}
		if (name != null) {
			query.setParameter("name", name + "%");
		}
		if (surname != null) {
			query.setParameter("surname", surname + "%");
		}

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private String createWhere(String paramName, String param, String currentWhere) {
		String where = currentWhere;

		if (param != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "b." + paramName + " like :" + paramName + " ";
		}

		return where;
	}

}
