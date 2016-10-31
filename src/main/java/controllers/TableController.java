package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class TableController {

	private Logger logger;
	
	@Inject
	NinjaProperties props;
	
	@Inject
	Provider<EntityManager> entityManagaerProvider;
	
	public Result tableDetails(@PathParam("tableName") String tableName) {
		
		logger = LoggerFactory.getLogger(this.getClass());
		
		EntityManager em = entityManagaerProvider.get();
		String schema = props.get("heroku.connect.schema.name") != null ? props.get("heroku.connect.schema.name") : "salesforce";
		String schemaAndTable = schema + "." + tableName;
		
		Query nameQuery = em.createNativeQuery("select column_name, data_type from information_schema.columns where table_schema = '"+schema+"' and table_name = '"+tableName+"'");
		logger.info("Executing query [{}]", "select column_name from information_schema.columns where table_schema = '"+schema+"' and table_name = '"+tableName+"'");
		
		List<String> columns = (List<String>)nameQuery.getResultList();
		
		if(logger.isInfoEnabled()) {
			for(String s : columns) {
				logger.info("Col [{}]",s);
			}
		}
		
		Query q = em.createNativeQuery("select * from "+schemaAndTable);
		logger.info("Executing query [{}]", "select * from "+schemaAndTable);
		List<Object[]> values = (List<Object[]>)q.getResultList();
		List<List<Object>> tuples = new ArrayList<List<Object>>();
		for(Object[] objArray : values) {
			
			for(Object obj : objArray) {
				logger.info("We have an obj [{}]", obj);
			}
			
		}
		
		Result r = Results.html();
		r.render("columns", columns);
		r.render("values", values);
		
		return r;
	}
}
