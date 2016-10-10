package com.frameWork.dataBase;
import java.sql.SQLException;

import com.frameWork.dataBase.ConnectionPool.PooledConnection;
import com.frameWork.service.config.configs.DataBaseConfig;

public class DBManager {

	private static PooledConnection conn;
	private static ConnectionPool connectionPool;
	private static DBManager inst;

	public void close() {
		try {
			connectionPool.closeConnectionPool();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DBManager() 
	{
		if (inst != null)
			return;

		// TODO Auto-generated constructor stub
//		String connStr = String.format("jdbc:mysql://%s:%d/%s", Config.getInstance().mysqlHost, Config.getInstance().mysqlPort,
//				Config.getInstance().mysqlDB);
		
		String connStr = DataBaseConfig.s_DataBaseURL;
		connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connStr, DataBaseConfig.s_UserName, DataBaseConfig.s_Pwd);
		try {
			connectionPool.createPool();
			inst = this;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static PooledConnection getConnection() {
		if (inst == null)
			new DBManager();

		try {
			
			conn = connectionPool.getConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

}