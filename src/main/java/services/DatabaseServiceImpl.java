package services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DatabaseServiceImpl implements DatabaseService {

	private Logger logger;
	
	@Inject
	Provider<EntityManager> entityManagerProvider;
	
	@Override
	public String[] getDatabaseTables() {
	
		logger = LoggerFactory.getLogger(this.getClass());
		
		EntityManager em = entityManagerProvider.get();
		Query q = em.createNativeQuery("select table_name from information_schema.tables where table_schema = 'salesforce'");
		List<Object[]> resultList = q.getResultList();
		
		for(Object[] oArray: resultList) {
			logger.info("We have a table!");
			for(Object o : oArray) {
				logger.info("Object is [{}]", o);
			}
		}
		
		return null;
	}

	@Override
	public String[] getFieldsForTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeQuery(String query) {
		// TODO Auto-generated method stub
		
	}

	
}
