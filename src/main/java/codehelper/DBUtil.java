package codehelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DBUtil {
	private Connection conn = null;
	// private final String url =
	// "jdbc:oracle:thin:@172.21.126.68:1521:developfligh";
	// private final String username = "psn";
	// private final String password = "psn";

	private final String url = "jdbc:oracle:thin:localhost:8090/orcl";
	private final String username = "asis";
	private final String password = "admin";

	private PreparedStatement pstmt = null;

	public DBUtil() {
		conn = connectionDB();
	}

	public String fetchTablePrimaryKey(String tableName) {
		try {
			ResultSet res = conn.getMetaData().getPrimaryKeys(null, null, tableName.toUpperCase());
			if (!res.isAfterLast()) {
				res.next();
				return res.getString("COLUMN_NAME");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

		}
		return "";
	}

	public SortedMap<String, String> fetchTableInfo(String tableName) {
		SortedMap<String, String> results = new TreeMap<String, String>();
		try {
			pstmt = conn.prepareStatement("select * from " + tableName);
			ResultSetMetaData resultSet = pstmt.executeQuery().getMetaData();
			for (int i = 0; i < resultSet.getColumnCount(); i++) {
				results.put(resultSet.getColumnName(i + 1), FunctionUtils.convertJdbcType(resultSet.getColumnClassName(i + 1), resultSet.getScale(i + 1)));
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				pstmt.close();
				pstmt = null;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("finally")
	public Connection connectionDB() {
		try {
			// Class.forName("oracle.jdbc.OracleDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("连接数据库发生错误！");
		} finally {
			return conn;
		}
	}

	public static void main(String[] args) throws SQLException {
		DBUtil db = new DBUtil();
		Map<String, String> map = db.fetchTableInfo("s_user");
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.print(key + "===");
			System.out.println(map.get(key));

		}
		/* db.fetchTablePrimaryKey("s_user"); */

	}

}
