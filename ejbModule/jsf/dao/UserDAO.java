package jsf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jsf.entities.User;

@Named
@RequestScoped
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
		
			if(login.equals(user.getLogin()) && pass.equals(user.getPassword())) {
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
		
		if (user.getLogin().equals("admin")) {
			roles.add("administrator");
		}
		else {
			roles.add("pracownik");
		}
		
		return roles;
	}
	
	
}