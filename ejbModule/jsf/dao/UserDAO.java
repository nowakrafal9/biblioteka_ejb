package jsf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jsf.entities.Role;
import jsf.entities.User;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "libraryPU";
	
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public User getUserFromDatabase(String login, String pass) {
		User u = null;
		
		Query query = em.createQuery("FROM User u where u.login=:login");
		query.setParameter("login", login);
		
		try {
			User user = (User)query.getSingleResult();
		
			if(login.equals(user.getLogin()) && pass.equals(user.getPassword()) && (user.getStatus() == 1)) {
				u = new User();
				u.setLogin(login);
				u.setPassword(pass);
			} 
		}catch (Exception e) {
			e.printStackTrace();
		}
				
		return u;
	}
	
	public List<String> getUserRolesFromDatabase(User user) {
		ArrayList<String> roles = new ArrayList<String>();
		
		Query query = em.createQuery("SELECT r FROM User u JOIN u.role r where u.login=:login");
		query.setParameter("login", user.getLogin());
		
		try {
			Role role = (Role)query.getSingleResult();
			roles.add(role.getName());	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return roles;
	}
}