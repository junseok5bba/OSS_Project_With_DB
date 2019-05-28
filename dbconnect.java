import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbconnect {
	private static Connection con;
	private static java.sql.Statement st;
	private ResultSet rs;

	public static Connection dbconn() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// MySQL 서버를 설정합니다.
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:436/vocabulary?serverTimezone=Asia/Seoul", "root",
					"oss206");
			st = con.createStatement();
		} catch (Exception ex) {
			System.out.println("Exception :" + ex);

		}
		return con;
	}
}
