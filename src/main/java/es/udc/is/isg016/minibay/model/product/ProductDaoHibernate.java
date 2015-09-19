package es.udc.is.isg016.minibay.model.product;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("productDao")
public class ProductDaoHibernate extends GenericDaoHibernate<Product,Long> 
	implements ProductDao {

	public List<Product> findByUser(long user, int start, int size){
	  	return getSession().createQuery(
    			"SELECT p FROM Product p WHERE " +
    			"p.owner.userProfileId = :user ORDER BY p.endDate DESC")
    			.setParameter("user", user)
    			.setFirstResult(start)
    			.setMaxResults(size)
    			.list();
	}

	
	public int getNumberOfUser(long user) {
		long number = (Long)getSession().createQuery(
				"SELECT COUNT(p) FROM Product p WHERE " +
		    	"p.owner.userProfileId = :user ORDER BY endDate DESC")
		    	.setParameter("user", user)
    			.uniqueResult();
		return (int) number;
	}
	
	public List<Product> findByNameByCategory(String keywords, Long categoryId, int start,
			int size){
		
		 String query1 =
				 "SELECT p " +
				 "FROM Product p " +
				 "WHERE ";
		 // parte final de la query
		 String query2 = 
				"p.endDate >= now() " +
		 		"ORDER BY p.endDate";
		 String pattern = "UPPER(p.productName) LIKE UPPER(CONCAT('%s', :arg%d, '%s')) AND ";
		 String pattern_category = "";
		 
		 // Si hay alguna categoria se anade para buscar por ella
		 if (categoryId != null)
			 pattern_category = "p.category.categoryId = :categoryId AND ";
		 
		 // patron a anadir en la query
		  
		 String aux = "";
		 String [] strs = null;;
		 int i;
		 // Si hay palabras clave, se ajusta la query para que tenga la opcion
		 if (keywords != null) {
			 keywords = keywords.trim();
			 if (!keywords.isEmpty()) {
				 strs = keywords.split("[ \t]"); 
				 for (i = 0; i < strs.length; i++)
					 if (!strs[i].isEmpty())
						 aux = aux + String.format(pattern, "%",i, "%");
			 }
		 }
		 //Query completa
		 Query query = getSession().createQuery(query1 + aux + pattern_category + query2);
		 
		 // Se anaden las keywords por las que se quiere buscar
		 if (keywords != null) {
			 if (!keywords.isEmpty()) {
				 for (i = 0; i < strs.length; i++) {
					 if (!strs[i].isEmpty()) {
						 query = query.setParameter("arg"+i, strs[i]);
					 }
				 }
			 }
		 }
		 
		 // Si se quiere buscar por categoria se indica por cual
		 if (categoryId != null)
			 query = query.setParameter("categoryId", categoryId);
		 	 
		 
		 return query
				 .setFirstResult(start)
				 .setMaxResults(size)
				 .list();
	}

	
	public int getNumberOfNameByCategory(String keywords, Long categoryId) {
		String query1 =
				 "SELECT COUNT(p) " +
				 "FROM Product p " +
				 "WHERE ";
		 // parte final de la query
		 String query2 = 
				"p.endDate >= now() " +
		 		"ORDER BY p.endDate";
		 String pattern = "UPPER(p.productName) LIKE UPPER(CONCAT('%s', :arg%d, '%s')) AND ";
		 String pattern_category = "";
		 
		 // Si hay alguna categoria se anade para buscar por ella
		 if (categoryId != null)
			 pattern_category = "p.category.categoryId = :categoryId AND ";
		 
		 // patron a anadir en la query
		  
		 String aux = "";
		 String [] strs = null;;
		 int i;
		 // Si hay palabras clave, se ajusta la query para que tenga la opcion
		 if (keywords != null) {
			 keywords = keywords.trim();
			 if (!keywords.isEmpty()) {
				 strs = keywords.split("[ \t]"); 
				 for (i = 0; i < strs.length; i++)
					 if (!strs[i].isEmpty())
						 aux = aux + String.format(pattern, "%",i, "%");
			 }
		 }
		 //Query completa
		 Query query = getSession().createQuery(query1 + aux + pattern_category + query2);
		 
		 // Se anaden las keywords por las que se quiere buscar
		 if (keywords != null) {
			 if (!keywords.isEmpty()) {
				 for (i = 0; i < strs.length; i++) {
					 if (!strs[i].isEmpty()) {
						 query = query.setParameter("arg"+i, strs[i]);
					 }
				 }
			 }
		 }
		 
		 // Si se quiere buscar por categoria se indica por cual
		 if (categoryId != null)
			 query = query.setParameter("categoryId", categoryId);
		 	 
		  
		
		 Long number = (Long) query.uniqueResult();
		 
		 return number != null? number.intValue(): 0;
	}
	
}
