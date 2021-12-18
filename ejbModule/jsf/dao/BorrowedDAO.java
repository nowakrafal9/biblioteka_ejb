package jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jsf.entities.Bookstock;
import jsf.entities.Borrowed;
import jsf.entities.Borrower;

@Stateless
public class BorrowedDAO {
	private final static String UNIT_NAME = "libraryPU";
	private Query query;

	Borrowed queryFilter = new Borrowed();

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

		Query query = em.createQuery("SELECT b FROM Borrowed b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Borrowed> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<Borrowed> list = null;

		String where = this.setFilter(filterParams);
		String join = "";
		join += "INNER JOIN b.bookstock bs ";
		join += "INNER JOIN b.borrower br ";
		query = em.createQuery("SELECT b FROM Borrowed b " + join + where);
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
		String join = "";
		join += "INNER JOIN b.bookstock bs ";
		join += "INNER JOIN b.borrower br ";
		query = em.createQuery("SELECT COUNT(b) FROM Borrowed b " + join + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public long countBorrowedBooks(Borrower borrower) {
		long count = 0;
		
		String where = "WHERE br.idBorrower =:idBorrower AND b.status = 1";
		String join = "INNER JOIN b.borrower br ";
		query = em.createQuery("SELECT COUNT(b) FROM Borrowed b " + join + where);
		query.setParameter("idBorrower", borrower.getIdBorrower());
		
		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	public List<Borrowed> getBorrowInfo(Bookstock book){
		List<Borrowed> list = null;
		
		String where = "WHERE bs.code =:code ";
		String join = "INNER JOIN b.bookstock bs ";
		query = em.createQuery("SELECT b FROM Borrowed b " + join + where);
		query.setParameter("code", book.getCode());
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private String setFilter(Map<String, Object> filterParams) {
		String where = "";

		Bookstock bs = new Bookstock();
		bs.setCode((String) filterParams.get("bookCode"));
		Borrower br = new Borrower();
		br.setBorrowerCode((String) filterParams.get("borrowerCode"));
		
		queryFilter.setBookstock(bs);
		queryFilter.setBorrower(br);
		queryFilter.setStatus((byte) filterParams.get("status"));
		
		where = "where b.status =:status ";
		if((byte) filterParams.get("returnStatus") == 1) {
			where += "AND b.returnDue >=:todayDate";
		}else if((byte) filterParams.get("returnStatus") == 2) {
			where += "AND b.returnDue <:todayDate";
		}
		where = this.createWhere("bookCode", queryFilter.getBookstock().getCode(), where);
		where = this.createWhere("borrowerCode", queryFilter.getBorrower().getBorrowerCode(), where);
				
		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {
		query.setParameter("status", queryFilter.getStatus());
		if((byte) filterParams.get("returnStatus") != 0) {
			query.setParameter("todayDate", java.sql.Date.valueOf(java.time.LocalDate.now()));
		}
		if(queryFilter.getBookstock().getCode() != null) {
			query.setParameter("code", "%" + queryFilter.getBookstock().getCode() + "%");
		}
		if(queryFilter.getBorrower().getBorrowerCode() != null) {
			query.setParameter("borrowerCode", "%" + queryFilter.getBorrower().getBorrowerCode() + "%");
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
			if(paramName.equals("bookCode")) {
				where += "bs.code like :code";
			}
			if(paramName.equals("borrowerCode")) {
				where += "br.borrowerCode like :borrowerCode";
			}
		}

		return where;
	}
	
	
}
