package controllers;

import java.util.List;

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
		
		Query nameQuery = em.createNativeQuery("select * from information_schema.columns where table_schema = '"+schema+"' and table_name = '"+tableName+"'");
		logger.info("Executing query [{}]", "select * from information_schema.columns where table_schema = '"+schema+"' and table_name = '"+tableName+"'");
		
		List<Object> columns = (List<Object>)nameQuery.getResultList();
		
		for(Object obj : columns) {
			logger.info("Col [{}] is a type of [{}]",obj.toString(), obj.getClass());
		}
		
		Query q = em.createNativeQuery("select * from "+schemaAndTable);
		logger.info("Executing query [{}]", "select * from "+schemaAndTable);
		List<String> values = (List<String>)q.getResultList();
		for(String s : values) {
			logger.info("Val [{}]", s);
		}
		
		Result r = Results.html();
		r.render("columns", columns);
		r.render("values", values);
		
		return r;
	}
}
