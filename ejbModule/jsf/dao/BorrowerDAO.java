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
	private Query query;

	Borrower queryFilter = new Borrower();

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

		query = em.createQuery("SELECT b FROM Borrower b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Borrower> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Borrower> list = null;

		String where = this.setFilter(filterParams);
		query = em.createQuery("SELECT b FROM Borrower b " + where).setFirstResult(offset).setMaxResults(pageSize);
		this.setFilterParam(filterParams);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public long countLazyList(Map<String, Object> filterParams) {
		long count = 0;

		String where = this.setFilter(filterParams);
		query = em.createQuery("SELECT COUNT(b) FROM Borrower b " + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	private String setFilter(Map<String, Object> filterParams) {
		String where = "";

		queryFilter.setBorrowerCode((String) filterParams.get("borrowerCode"));
		queryFilter.setName((String) filterParams.get("name"));
		queryFilter.setSurname((String) filterParams.get("surname"));
		queryFilter.setCity((String) filterParams.get("city"));
		queryFilter.setStatus((byte) filterParams.get("status"));

		if (queryFilter.getStatus() == 0 || queryFilter.getStatus() == 1) {
			where = "where b.status =:status ";
		}
		where = this.createWhere("borrowerCode", queryFilter.getBorrowerCode(), where);
		where = this.createWhere("name", queryFilter.getName(), where);
		where = this.createWhere("surname", queryFilter.getSurname(), where);
		where = this.createWhere("city", queryFilter.getCity(), where);

		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {
		if (queryFilter.getStatus() == 0 || queryFilter.getStatus() == 1) {
			query.setParameter("status", queryFilter.getStatus());
		}
		if (queryFilter.getBorrowerCode() != null) {
			query.setParameter("borrowerCode", "%" + queryFilter.getBorrowerCode() + "%");
		}
		if (queryFilter.getName() != null) {
			query.setParameter("name", queryFilter.getName() + "%");
		}
		if (queryFilter.getSurname() != null) {
			query.setParameter("surname", queryFilter.getSurname() + "%");
		}
		if (queryFilter.getCity() != null) {
			query.setParameter("city", queryFilter.getCity() + "%");
		}
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