package core.dataBase;
import java.sql.SQLException;

import core.config.ConfigService;
import core.dataBase.ConnectionPool.PooledConnection;

public class DBManager {

	private static PooledConnection conn;
	private static ConnectionPool connectionPool;
	private static DBManager inst;
	static String s_DataBaseURL;
	static String s_UserName;
	static String s_Pwd;

	public void close() 
	{
		try {
			connectionPool.closeConnectionPool();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DBManager() 
	{
		if (inst != null)
			return;
//		String connStr = String.format("jdbc:mysql://%s:%d/%s", Config.getInstance().mysqlHost, Config.getInstance().mysqlPort,
//				Config.getInstance().mysqlDB);
		
		s_DataBaseURL = ConfigService.GetString("DataBase", "DataBaseURL", "localhost");
		s_UserName = ConfigService.GetString("DataBase", "UserName", "root");
		s_Pwd = ConfigService.GetString("DataBase", "Pwd", "123456");
		
		connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", s_DataBaseURL, s_UserName, s_Pwd);
		try {
			connectionPool.createPool();
			inst = this;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PooledConnection getConnection() {
		if (inst == null)
			new DBManager();

		try {
			
			conn = connectionPool.getConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

}