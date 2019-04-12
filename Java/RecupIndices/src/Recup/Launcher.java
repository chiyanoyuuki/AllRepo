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
	private final long VINGT_QUATRE_HEURES = 1000 * 60 * 60 * 24;
	private String[][] SITES;
	private ArrayList<HashMap<String,Indice>> indices;
	private ArrayList<org.jsoup.Connection> conn;
	private java.sql.Connection sql;
	private Statement st;
	private Timer timer;
	
	public Launcher()
	{
		show("Programme lancé");
		init();
		recupIndices();
		loopVals();
	}
	
	private void loopVals()
	{
		int nb = 0;
		int i=0;
		while(true)
		{
			for(int x=0;x<SITES.length;x++)
			{
				try 
				{
					Thread.sleep(500);
					org.jsoup.Connection c = conn.get(x);
					Elements lignes = c.get().select("table").get(Integer.parseInt(SITES[x][2])).select("tr");
					
					for(Element ligne:lignes)
					{
						Elements colonnes = ligne.select("td");
						List<String> tmp = colonnes.eachText();
						if(tmp.size()>0)
						{
							String nom = tmp.get(Integer.parseInt(SITES[x][4]));
							double val = Double.parseDouble(tmp.get(Integer.parseInt(SITES[x][5])).replaceAll("\\.", "").replaceAll(",","."));
							indices.get(x).get(nom).addVal(val,st,SITES[x][1]);
						}
					}
				} 
				catch 	(IOException e1) 		{show("ERREUR " + SITES[x][1] + " RECUPERATION HTML");} 
				catch 	(InterruptedException e){show("ERREUR " + SITES[x][1] + " PAUSE");}
			}
			if(nb%1000==0)show("Nombre : " + nb);
			nb++;
		}
	}
	
	private void recupIndices()
	{
		for(String[] SITE : SITES)
		{
			try 
			{
				show("Connection " + SITE[1]);
				Thread.sleep(2000);
				org.jsoup.Connection c = Jsoup.connect(SITE[0]).ignoreHttpErrors(true);
				conn.add(c);
				
				HashMap<String,Indice> indicestmp = new HashMap<String,Indice>();
				Elements lignes = c.get().select("table").get(Integer.parseInt(SITE[2])).select("tr");
				
				for(Element ligne:lignes)
				{
					Elements colonnes = ligne.select("td");
					List<String> tmp = colonnes.eachText();
					if(tmp.size()>0)
					{
						String pays = colonnes.get(Integer.parseInt(SITE[3])).selectFirst("span").attr("title");
						String SQL = "INSERT INTO PAYS VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM PAYS P2),'"+pays+"')";
						
						try{st.executeQuery(SQL);}catch(SQLException e) {}
						
						String nom = tmp.get(Integer.parseInt(SITE[4]));
						double val = Double.parseDouble(tmp.get(Integer.parseInt(SITE[5])).replaceAll("\\.", "").replaceAll(",","."));
						indicestmp.put(nom,new Indice(val,nom,pays));
						
						try{st.executeQuery("INSERT INTO INDICES VALUES ((SELECT COALESCE(MAX(ID)+1,0) FROM INDICES I2),(SELECT P.ID FROM PAYS P WHERE P.NOM='"+pays+"'),'"+nom+"')");}catch(SQLException e) {}
					}
				}
				indices.add(indicestmp);
			} 
			catch 	(IOException e) 		{show("ERREUR " + SITE[1] + " RECUPERATION HTML");} 
			catch 	(InterruptedException e){show("ERREUR " + SITE[1] + " PAUSE");}
		}
	}
	
	private void init()
	{
		SITES = new String[][] 
		{
			//SITE												NOM BDD		TABLE,	PAYS,	NOM,	VAL
			{"https://fr.investing.com/indices/indices-cfds"	,"cfd"		,"1",	"0",	"1",	"2"},
			{"https://fr.investing.com/indices/indices-futures"	,"futures"	,"0",	"0",	"1",	"3"}
		};
			
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 1, 5);
		Date dat = cal.getTime();
		timer = new Timer();
			
		
		try 
		{
			Class.forName("org.mariadb.jdbc.Driver");
			sql = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=Chiyanoyuuki1512.");
			st = sql.createStatement();
			timer.schedule(new BDD(sql), dat, VINGT_QUATRE_HEURES);
		} 
		catch (SQLException e) 				{show("Erreur lors de la connection à la base de données");} 
		catch (ClassNotFoundException e) 	{show("Erreur driver BDD");}
		
		indices = new ArrayList<HashMap<String,Indice>>();
		conn = new ArrayList<org.jsoup.Connection>();
	}
	
	private void show(String s)
	{
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(String.format("%-30s", t) + " | " + s);
	}
	
	public static void main(String[] args) {Launcher l = new Launcher();}
}