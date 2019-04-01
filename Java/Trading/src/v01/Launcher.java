package v01;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Launcher 
{
	private final String SITE = "https://fr.investing.com/indices/major-indices";
	//-----------------------------------
	private HashMap<String,Indice> indices;
	
	public Launcher() throws IOException, InterruptedException, ClassNotFoundException, SQLException
	{
		Class.forName("org.mariadb.jdbc.Driver");
		java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=chiyanoyuuki1512.");
		Statement st = connection.createStatement();
		
		indices = new HashMap<String,Indice>();
		org.jsoup.Connection c = (org.jsoup.Connection) Jsoup.connect(SITE).ignoreHttpErrors(true);
		Document d = c.get();
		
		Elements tables = d.select("table");
		Elements lignes = tables.get(0).select("tr");
		for(Element ligne:lignes)
		{
			Elements colonnes = ligne.select("td");
			List<String> tmp = colonnes.eachText();
			if(tmp.size()>0)
			{
				String nom = tmp.get(1);
				double val = Double.parseDouble(tmp.get(2).replaceAll("\\.", "").replaceAll(",","."));
				indices.put(nom,new Indice(val,nom));
				
				try{st.executeQuery("INSERT INTO INDICES VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM INDICES I2),'"+nom+"')");}
				catch(Exception e) {}
			}
		}
		
		int i=0;
		while(true)
		{
			System.out.print(i++);
			Thread.sleep(1000);
			d = c.get();
				
			tables = d.select("table");
			lignes = tables.get(0).select("tr");
			for(Element ligne:lignes)
			{
				Elements colonnes = ligne.select("td");
				List<String> tmp = colonnes.eachText();
				if(tmp.size()>0)
				{
					String nom = tmp.get(1);
					double val = Double.parseDouble(tmp.get(2).replaceAll("\\.", "").replaceAll(",","."));
					indices.get(nom).addVal(val,st);
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException{@SuppressWarnings("unused")Launcher launcher = new Launcher();}
}
