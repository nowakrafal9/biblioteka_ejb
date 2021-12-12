package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Bookinfo;
import jsf.entities.Bookstock;

@Stateless
public class BookstockDAO {
	private final static String UNIT_NAME = "libraryPU";
	private Query query;

	Bookstock queryFilter = new Bookstock();

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

	public Bookstock getQueryFilter() {
		return queryFilter;
	}

	public void setQueryFilter(Bookstock queryFilter) {
		this.queryFilter = queryFilter;
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

	public List<Bookstock> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Bookstock> list = null;

		String where = this.setFilter(filterParams);
		query = em.createQuery("SELECT b FROM Bookstock b INNER JOIN b.bookinfo bi " + where).setFirstResult(offset)
				.setMaxResults(pageSize);
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
		query = em.createQuery("SELECT COUNT(b) FROM Bookstock b INNER JOIN b.bookinfo bi " + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public Boolean checkExist(String code) {
		long count = 0;
		
		String where = "WHERE b.code =:code ";
		query = em.createQuery("SELECT COUNT(b) FROM Bookstock b " + where);
		query.setParameter("code", code);
		
		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(count == 0) {
			return false;
		}
		return true;
	}
	
	private String setFilter(Map<String, Object> filterParams) {
		String where = "";

		Bookinfo b = new Bookinfo();
		b.setTitle((String) filterParams.get("title"));

		queryFilter.setBookinfo(b);
		queryFilter.setCode((String) filterParams.get("code"));
		queryFilter.setStatus((byte) filterParams.get("status"));

		if (queryFilter.getStatus() == 0) {
			where = "where b.status !=:status ";
		} else {
			where = "where b.status =:status ";
		}
		where = this.createWhere("code", queryFilter.getCode(), where);
		where = this.createWhere("title", queryFilter.getBookinfo().getTitle(), where);
		
		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {
		if (queryFilter.getStatus() == 0 || queryFilter.getStatus() == 1 || queryFilter.getStatus() == 2) {
			query.setParameter("status", (byte) queryFilter.getStatus());
		} else if (queryFilter.getStatus() == 3) {
			query.setParameter("status", (byte) 0);
		}
		if (queryFilter.getCode() != null) {
			query.setParameter("code", "%" + queryFilter.getCode() + "%");
		}
		if (queryFilter.getBookinfo().getTitle() != null) {
			query.setParameter("title", "%" + queryFilter.getBookinfo().getTitle() + "%");
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
			if (paramName.equals("code") || paramName.equals("status")) {
				where += "b." + paramName + " like :" + paramName + " ";
			}
			if (paramName.equals("title")) {
				where += "bi." + paramName + " like :" + paramName + " ";
			}
		}

		return where;
	}
}