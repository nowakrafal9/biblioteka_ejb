package jsf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jsf.entities.User;

@Named
@RequestScoped
public class UserDAO {

	// simulate finding user in DB
	public User getUserFromDatabase(String login, String pass) {
		
		User u = null;

		if (login.equals("user1") && pass.equals("password")) {
			u = new User();
			u.setLogin(login);
			u.setPassword(pass);
			u.setName("Jan");
			u.setSurname("Kowalski");
		}

		if (login.equals("user2") && pass.equals("password")) {
			u = new User();
			u.setLogin(login);
			u.setPassword(pass);
			u.setName("Anna");
			u.setSurname("Nowak");
		}

		if (login.equals("user3") && pass.equals("password")) {
			u = new User();
			u.setLogin(login);
			u.setPassword(pass);
			u.setName("Micha³");
			u.setSurname("Jaworek");
		}

		return u;
	}

	// simulate retrieving roles of a User from DB
	public List<String> getUserRolesFromDatabase(User user) {
		
		ArrayList<String> roles = new ArrayList<String>();
		
		if (user.getLogin().equals("user1")) {
			roles.add("user");
		}
		if (user.getLogin().equals("user2")) {
			roles.add("manager");
		}
		if (user.getLogin().equals("user3")) {
			roles.add("admin");
		}
		
		return roles;
	}
}