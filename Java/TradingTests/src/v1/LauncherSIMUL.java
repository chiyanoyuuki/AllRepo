package v1;

import java.io.*;
import java.net.*;
import java.util.*;

public class LauncherSIMUL 
{
	private final String SITE = "https://fr.investing.com/indices/major-indices";
	
	private ArrayList<String> tab,lignesNonFormat;
	private HashMap<String,Indice> indices;
	private String doc;
	//----------------------------------------
	private URLConnection openConnection;
	
	public LauncherSIMUL() throws IOException, InterruptedException
	{
		read();
		getTab();
		getLignes();
		formatLignes();
		alimenter();
	}
	
	private void read() throws IOException
	{
		StringBuilder sbDoc = new StringBuilder();String tmp;
		try 
		{ 
			openConnection = new URL(SITE).openConnection();
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			
			InputStream is		 	= openConnection.getInputStream();
			InputStreamReader isr 	= new InputStreamReader(is,"UTF-8");
			BufferedReader in 		= new BufferedReader(isr); 
		 	
		 	while ((tmp=in.readLine())!=null) {sbDoc.append(tmp);} 
		 	is.close();
		} 
		catch (MalformedURLException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		doc = sbDoc.toString();
		doc = doc.substring(doc.indexOf("<table"));
	}
	private void read2() throws IOException
	{
		StringBuilder sbDoc = new StringBuilder();String tmp;
		try 
		{ 
			InputStream is 			= openConnection.getInputStream();
			InputStreamReader isr	= new InputStreamReader(is,"UTF-8");
			BufferedReader in 		= new BufferedReader(isr); 
			
		 	while ((tmp=in.readLine())!=null) {sbDoc.append(tmp);}   
		 	is.close();
		} 
		catch (MalformedURLException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		doc = sbDoc.toString();
		System.out.println(doc);
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
			read2();
			getTab();
			getLignes();
			formatLignes2();
		}
		afficher();
	}
	public static void main(String[] args) throws IOException, InterruptedException{LauncherSIMUL l = new LauncherSIMUL();}
}
