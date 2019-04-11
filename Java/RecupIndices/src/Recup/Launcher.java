package Recup;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Launcher 
{
	public static final long VINGT_QUATRE_HEURES = 1000 * 60 * 60 * 24;
	public static String type;
	private String SITE;
	//-----------------------------------
	private HashMap<String,Indice> indices;
	
	public Launcher(String typ)
	{
		type = typ;
		show("Programme "+type+" lancé");
		
		if(type.equals("cfd")) 			
		{
			SITE = "https://fr.investing.com/indices/indices-cfds";
			
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+1, 1, 30);
			Date dat = cal.getTime();
			
			Timer timer = new Timer();
			timer.schedule(new BDD(), dat, VINGT_QUATRE_HEURES);	
		}
		else if(type.equals("futures")) 
		{
			SITE = "https://fr.investing.com/indices/indices-futures";
		}
		
		Statement st = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=Chiyanoyuuki1512.");
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
			lignes = tables.get(type.equals("cfd")?1:0).select("tr");
		} catch (IOException e1) {show("Probleme de recuperation de la page html");}
		
		for(Element ligne:lignes)
		{
			Elements colonnes = ligne.select("td");
			List<String> tmp = colonnes.eachText();
			if(tmp.size()>0)
			{
				String pays = colonnes.get(0).selectFirst("span").attr("title");
				
				try{st.executeQuery("INSERT INTO PAYS VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM PAYS P2),'"+pays+"')");}
				catch(Exception e) {}
				
				String nom = tmp.get(1);
				double val = Double.parseDouble(tmp.get(type.equals("cfd")?2:3).replaceAll("\\.", "").replaceAll(",","."));
				indices.put(nom,new Indice(val,nom,pays));
				
				try{st.executeQuery("INSERT INTO INDICES VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM INDICES I2),(SELECT P.ID FROM PAYS P WHERE P.NOM='"+pays+"'),'"+nom+"')");}
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
				
				lignes = tables.get(type.equals("cfd")?1:0).select("tr");
				for(Element ligne:lignes)
				{
					Elements colonnes = ligne.select("td");
					List<String> tmp = colonnes.eachText();
					if(tmp.size()>0)
					{
						String nom = tmp.get(1);
						double val = Double.parseDouble(tmp.get(type.equals("cfd")?2:3).replaceAll("\\.", "").replaceAll(",","."));
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
	
	public static void main(String[] args)
	{
		if(args.length<1)
		{
			System.out.println("Pas assez d'arguments");
		}
		else
		{
			Launcher launcher = new Launcher(args[0]);
		}
	}
}