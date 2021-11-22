package jsf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jsf.entities.Genre;

@Stateless
public class GenreDAO {
	private final static String UNIT_NAME = "libraryPU";
	
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Genre genre) {
		em.persist(genre);
	}
	
	public Genre merge(Genre genre) {
		return em.merge(genre);
	}
	
	public void remove(Genre genre) {
		em.remove(em.merge(genre));
	}
	
	public Genre find(Object id) {
		return em.find(Genre.class, id);
	}
	
	public List<Genre> getFullList(){
		List<Genre> list = null;
		
		Query query = em.createQuery("SELECT g FROM Genre g");
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
