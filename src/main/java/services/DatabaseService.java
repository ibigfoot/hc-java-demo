package services;


public interface DatabaseService {

	public String[] getDatabaseTables();
	
	public String[] getFieldsForTable(String tableName);
	
	public void executeQuery(String query);
	
	
}
