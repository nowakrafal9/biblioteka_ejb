package jsf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jsf.entities.Role;
import jsf.entities.User;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "libraryPU";
	private Query query;

	User queryFilter = new User();

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public User getUserFromDatabase(String login, String pass) {
		User u = null;

		Query query = em.createQuery("FROM User u where u.login=:login");
		query.setParameter("login", login);

		try {
			User user = (User) query.getSingleResult();

			if (login.equals(user.getLogin()) && pass.equals(user.getPassword()) && (user.getStatus() == 1)) {
				u = new User();
				u.setLogin(login);
				u.setPassword(pass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return u;
	}

	public List<String> getUserRolesFromDatabase(User user) {
		ArrayList<String> roles = new ArrayList<String>();

		Query query = em.createQuery("SELECT r FROM User u JOIN u.role r where u.login=:login");
		query.setParameter("login", user.getLogin());

		try {
			Role role = (Role) query.getSingleResult();
			roles.add(role.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return roles;
	}

	public List<User> getFullList() {
		List<User> list = null;

		query = em.createQuery("SELECT u FROM User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<User> getLazyList(Map<String, Object> filterParams, int offset, int pageSize) {
		List<User> list = null;

		String where = this.setFilter(filterParams);
		String join = "";
		join += "INNER JOIN u.role r ";
		query = em.createQuery("SELECT u FROM User u " + join + where + "ORDER BY u.login").setFirstResult(offset)
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
		String join = "";
		join += "INNER JOIN u.role r ";
		query = em.createQuery("SELECT COUNT(u) FROM User u " + join + where);
		this.setFilterParam(filterParams);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public int getUserID(String login) {
		int id = 0;
		String where = "";

		where = createWhere("login", login, where);
		query = em.createQuery("SELECT u.idUser FROM User u " + where);
		query.setParameter("login", login);

		try {
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	private String setFilter(Map<String, Object> filterParams) {
		String where = "";

		queryFilter.setLogin((String) filterParams.get("login"));
		queryFilter.setStatus((byte) filterParams.get("status"));

		if (queryFilter.getStatus() != 2) {
			where = "WHERE u.status =:status ";
		}
		where = this.createWhere("login", queryFilter.getLogin(), where);
		where = this.createWhere("name", "Pracownik", where);

		return where;
	}

	private void setFilterParam(Map<String, Object> filterParams) {
		if (queryFilter.getStatus() != 2) {
			query.setParameter("status", queryFilter.getStatus());
		}
		if (queryFilter.getLogin() != null) {
			query.setParameter("login", "%" + queryFilter.getLogin() + "%");
		}
		query.setParameter("name", "Pracownik");
	}

	private String createWhere(String paramName, String param, String currentWhere) {
		String where = currentWhere;

		if (param != null) {
			if (where.isEmpty()) {
				where = "WHERE ";
			} else {
				where += "AND ";
			}
			if (paramName.equals("login")) {
				where += "u." + paramName + " like :" + paramName + " ";
			} else if (paramName.equals("name")) {
				where += "r." + paramName + " like:" + paramName + " ";
			}
		}

		return where;
	}

}