package Recup;

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
	
	public Launcher()
	{
		show("Programme lancé");
					
		Statement st = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=Chiyanoyuuki1512.");
			//java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=chiyanoyuuki1512.");
			st = connection.createStatement();
		} catch (SQLException | ClassNotFoundException e2) {show("Erreur lors de la connection à la base de données");}
			
		
		
		
		indices = new HashMap<String,Indice>();
		org.jsoup.Connection c = (org.jsoup.Connection) Jsoup.connect(SITE).ignoreHttpErrors(true);
		
		Document d;
		Elements tables = null;
		Elements lignes = null;
		try {
			d = c.get();
			tables = d.select("table");
			lignes = tables.get(0).select("tr");
		} catch (IOException e1) {show("Probleme de recuperation de la page html");}
		
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
		
		int nb = 0;
		int i=0;
		while(true)
		{
			
			try {Thread.sleep(1000);} catch (InterruptedException e) {show("Erreur lors de la pause");}
			try {d = c.get();tables = d.select("table");} catch (IOException e1) {show("Probleme de recuperation de la page html");}
				
			if(tables != null && tables.size()>0)
			{
				if(nb%1000==0)show("Nombre : " + nb);
				
				lignes = tables.get(0).select("tr");
				
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
			else
			{
				show("ERREUR : " + String.format("%05d", i++)+ " ("+nb+")");
			}
			nb++;
		}
	}
	
	private void show(String s)
	{
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(String.format("%-30s", t) + " | " + s);
	}
	
	public static void main(String[] args){@SuppressWarnings("unused")Launcher launcher = new Launcher();}
}