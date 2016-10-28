/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ninja.Result;
import ninja.Results;
import ninja.utils.NinjaProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;



@Singleton
public class ApplicationController {

	@Inject 
	Provider<EntityManager> entitiyManagerProvider;
	
	@Inject
	NinjaProperties props;
	
	private Logger logger;
	
    public Result index() {

    	logger = LoggerFactory.getLogger(this.getClass());
    	
    	EntityManager em = entitiyManagerProvider.get();
    	
    	// if prop is not set, default to salesforce
		String schema = props.get("heroku.connect.schema.name") != null ? props.get("heroku.connect.schema.name") : "salesforce";
    	Query q = em.createNativeQuery("select table_name from information_schema.tables where table_schema = '"+schema+"' and ");
		List<String> resultList = (List<String>)q.getResultList();

		List<String> tables = new ArrayList<String>();
		for(String s : resultList) {
			logger.info("Table [{}]", s);
			if(!s.startsWith("_")) {
				tables.add(s);
			}
		}
		Result r = Results.html();
		r.render("tables", resultList);
        return r;

    }
    
    public Result helloWorldJson() {
        
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";

        return Results.json().render(simplePojo);

    }
    
    public static class SimplePojo {

        public String content;
        
    }
}
