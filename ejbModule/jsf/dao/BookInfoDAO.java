package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Author;
import jsf.entities.Bookinfo;
import jsf.entities.Publisher;

@Stateless
public class BookInfoDAO /* TitleInfoDAO */ {
	private final static String UNIT_NAME = "libraryPU";
	private Query query;

	Bookinfo queryFilter = new Bookinfo();

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Bookinfo bookinfo) {
		em.persist(bookinfo);
	}

	public Bookinfo merge(Bookinfo bookinfo) {
		return em.merge(bookinfo);
	}

	public void remove(Bookinfo bookinfo) {
		em.remove(em.merge(bookinfo));
	}

	public Bookinfo find(Object id) {
		return em.find(Bookinfo.class, id);
	}

	public Bookinfo getQueryFilter() {
		return queryFilter;
	}

	public void setQueryFilter(Bookinfo queryFilter) {
		this.queryFilter = queryFilter;
	}

	public List<Bookinfo> getFullList() {
		List<Bookinfo> list = null;

		query = em.createQuery("SELECT b FROM Bookinfo b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Bookinfo> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Bookinfo> list = null;

		String join = "";
		join += "INNER JOIN b.author a ";
		join += "INNER JOIN b.publisher p ";
		String where = this.setFilter(filterParams);
		String orderBy = this.setOrder(filterParams);
		query = em.createQuery("SELECT b FROM Bookinfo b " + join + where + orderBy).setFirstResult(offset)
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

		String join = "";
		join += "INNER JOIN b.author a ";
		join += "INNER JOIN b.publisher p ";
		String where = this.setFilter(filterParams);
		query = em.createQuery("SELECT COUNT(b) FROM Bookinfo b " + join + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public long countBooks(Bookinfo book, String mode) {
		long count = 0;
		String where = "";

		if (mode.equals("all")) {
			where = "WHERE b.bookinfo =:title AND b.status !=:status";
			query = em.createQuery("SELECT COUNT(b) FROM Bookstock b " + where);
			query.setParameter("title", book);
			query.setParameter("status", (byte) 0);
		}
		if (mode.equals("free")) {
			where = "WHERE b.bookinfo =:title AND b.status =:status";
			query = em.createQuery("SELECT COUNT(b) FROM Bookstock b " + where);
			query.setParameter("title", book);
			query.setParameter("status", (byte) 1);
		}

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public boolean checkExist(String code) {
		long count = 0;
		String where = "";

		where = this.createWhere("code", code, where);
		query = em.createQuery("SELECT COUNT(b) FROM Bookinfo b " + where);
		query.setParameter("code", code);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (count == 1) {
			return true;
		}

		return false;
	}

	public int getTitleID(String code) {
		int id = 0;
		String where = "";

		where = createWhere("code", code, where);
		query = em.createQuery("SELECT b.idTitle FROM Bookinfo b " + where);
		query.setParameter("code", code);

		try {
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	private String setFilter(Map<String, Object> filterParams) {
		String where = "";

		Author a = new Author();
		a.setName((String) filterParams.get("name"));
		a.setSurname((String) filterParams.get("surname"));

		Publisher p = new Publisher();
		p.setName((String) filterParams.get("publisher"));

		queryFilter.setCode((String) filterParams.get("code"));
		queryFilter.setTitle((String) filterParams.get("title"));
		queryFilter.setAuthor(a);
		queryFilter.setPublisher(p);

		where = this.createWhere("code", queryFilter.getCode(), where);
		where = this.createWhere("title", queryFilter.getTitle(), where);
		where = this.createWhere("name", queryFilter.getAuthor().getName(), where);
		where = this.createWhere("surname", queryFilter.getAuthor().getSurname(), where);
		where = this.createWhere("publisher", queryFilter.getPublisher().getName(), where);

		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {
		if (queryFilter.getCode() != null) {
			query.setParameter("code", "%" + queryFilter.getCode() + "%");
		}
		if (queryFilter.getTitle() != null) {
			query.setParameter("title", "%" + queryFilter.getTitle() + "%");
		}
		if (queryFilter.getAuthor().getName() != null) {
			query.setParameter("name", "%" + queryFilter.getAuthor().getName() + "%");
		}
		if (queryFilter.getAuthor().getSurname() != null) {
			query.setParameter("surname", "%" + queryFilter.getAuthor().getSurname() + "%");
		}
		if (queryFilter.getPublisher().getName() != null) {
			query.setParameter("publisher", "%" + queryFilter.getPublisher().getName() + "%");
		}
	}

	private String setOrder(Map<String, Object> filterParams) {
		String orderBy = "";
		String order = (String) filterParams.get("orderBy");
		
		if (order != null) {
			if(order.equals("code") || order.equals("title")) {
				orderBy = "ORDER BY b." + order;
			}
			if(order.equals("name") || order.equals("surname")) {
				orderBy = "ORDER BY a." + order;
			}
			if(order.equals("publisher")) {
				orderBy = "ORDER BY p.name";
			}
			
			if (!order.equals("code")) {
				orderBy += ", b.code";
			}
		}

		return orderBy;
	}

	private String createWhere(String paramName, String param, String currentWhere) {
		String where = currentWhere;

		if (param != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}

			if (paramName.equals("code") || paramName.equals("title")) {
				where += "b." + paramName + " like :" + paramName + " ";
			}
			if (paramName.equals("name") || paramName.equals("surname")) {
				where += "a." + paramName + " like :" + paramName + " ";
			}
			if (paramName.equals("publisher")) {
				where += "p.name like:publisher ";
			}
		}

		return where;
	}

}
