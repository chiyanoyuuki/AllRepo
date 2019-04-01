package v1;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class LauncherSIMUL3 
{
	private final String SITE = "https://www.boursorama.com/bourse/indices/internationaux";
	
	private ArrayList<String> tab,lignesNonFormat;
	private HashMap<String,Indice> indices;
	private String doc;
	//----------------------------------------
	private Connection conn;
	
	public LauncherSIMUL3() throws IOException, InterruptedException
	{
		conn = Jsoup.connect(SITE);
		read();
		getTab();
		getLignes();
		formatLignes();
		alimenter();
	}
	private void read() throws IOException
	{
		doc = conn.get().html();
		doc = doc.substring(doc.indexOf("<table"));
	}
	private void getTab()
	{
		tab = new ArrayList<String>();
		String word = "table";String start = "<"+word;String end = "/"+word+">";int tailleEnd = end.length();
		
		while(doc.contains(start))
		{
			int deb = doc.indexOf(start);
			int fin = doc.indexOf(end);
			tab.add(doc.substring(deb,fin));
			doc = doc.substring(fin+tailleEnd);
		}
	}
	private void getLignes()
	{
		lignesNonFormat = new ArrayList<String>();
		String word = "tr";String start = "<"+word;String end = "/"+word+">";int tailleEnd = end.length();
		
		for(String tab:this.tab)
		{
			while(tab.contains(start))
			{
				int deb = tab.indexOf(start);
				int fin = tab.indexOf(end);
				lignesNonFormat.add(tab.substring(deb,fin).replaceAll("\n", ""));
				tab = tab.substring(fin+tailleEnd);
			}
		}
	}
	private void formatLignes()
	{
		indices = new HashMap<String,Indice>();
		System.out.println(lignesNonFormat.size() + " indices trouv�s");
		for(String ligne : lignesNonFormat)
		{
			ArrayList<String> tmp = new ArrayList<String>();
			String word = "td";String start = "<"+word;String end = "/"+word+">";int tailleEnd = end.length();
			String actuelle = ligne;
			
			while(actuelle.contains(start))
			{
				int deb = actuelle.indexOf(start);
				int fin = actuelle.indexOf(end);
				String data = actuelle.substring(deb,fin).replaceAll("<[^>]+>", "").replaceAll(" {2,}", " ").replaceAll("(^ *| *$| *< *| *> *)","");
				if(!data.matches(" *"))tmp.add(data);
				actuelle = actuelle.substring(fin+tailleEnd);
			}
			if(tmp.size()==9)
			{
				String nom = tmp.get(1);
				if(indices.containsKey(nom))System.out.println("DUPLICATE " + nom);
				else indices.put(nom,new Indice(Double.parseDouble(tmp.get(2).replaceAll("\\.", "").replaceAll(",",".")),nom));
			}
		}
	}
	private void formatLignes2()
	{
		for(String ligne : lignesNonFormat)
		{
			ArrayList<String> tmp = new ArrayList<String>();
			String word = "td";String start = "<"+word;String end = "/"+word+">";int tailleEnd = end.length();
			String actuelle = ligne;
			
			while(actuelle.contains(start))
			{
				int deb = actuelle.indexOf(start);
				int fin = actuelle.indexOf(end);
				String data = actuelle.substring(deb,fin).replaceAll("<[^>]+>", "").replaceAll(" {2,}", " ").replaceAll("(^ *| *$| *< *| *> *)","");
				if(!data.matches(" *"))tmp.add(data);
				actuelle = actuelle.substring(fin+tailleEnd);
			}
			if(tmp.size()==9)
			{
				String nom = tmp.get(1);
				if(indices.containsKey(nom))indices.get(nom).addVal(Double.parseDouble(tmp.get(2).replaceAll("\\.", "").replaceAll(",",".")));
			}
		}
	}
	
	private void afficher()
	{
		Map<String,Indice> s2 = new TreeMap<String,Indice>(indices);
		Iterator<String> it = s2.keySet().iterator();while(it.hasNext()) {s2.get(it.next()).show();}
	}
	private void alimenter() throws InterruptedException, IOException
	{
		int cpt = 0;
		while(cpt++<2)
		{
			System.out.print(cpt+" ");
			Thread.sleep(1000);
			read();
			getTab();
			getLignes();
			formatLignes2();
		}
		afficher();
	}
	public static void main(String[] args) throws IOException, InterruptedException{LauncherSIMUL3 l = new LauncherSIMUL3();}
}
