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

		String where = this.setFilter(filterParams);
		query = em.createQuery("SELECT b FROM Bookinfo b INNER JOIN b.author a INNER JOIN b.publisher p " + where)
				.setFirstResult(offset).setMaxResults(pageSize);
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
		query = em
				.createQuery("SELECT COUNT(b) FROM Bookinfo b INNER JOIN b.author a INNER JOIN b.publisher p " + where);
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
