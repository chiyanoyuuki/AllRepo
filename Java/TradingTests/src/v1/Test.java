package v1;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{	
		Class.forName("org.mariadb.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=chiyanoyuuki1512.");
		Statement st = connection.createStatement();
		
		try
		{
		st.executeQuery("INSERT INTO INDICES VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM INDICES I2),'TEST')");
		}
		catch(Exception e) {}
		
		try
		{
		st.executeQuery("INSERT INTO VALEURS_INDICES VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM INDICES I2),'TEST')");
		}
		catch(Exception e) {}
		
		System.out.println("test");
	}
}
