package v1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;

public class TestConnect {
	
	private final String SITE = "https://fr.investing.com/indices/major-indices";
	//private final String SITE = "file:///C:/Users/ASC%20Arma/Desktop/testpage.html";
	private ArrayList<String> tab,lignesNonFormat;
	private HashMap<String,Indice> indices;
	private String doc;
	
	public TestConnect() throws MalformedURLException, IOException, InterruptedException
	{
		URLConnection oc;
		oc = new URL(SITE).openConnection();
		oc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
	 	
		InputStream is = (InputStream) oc.getContent();
		BufferedInputStream bis = new BufferedInputStream(is);
		is.mark(0);
		doc = IOUtils.toString(bis,"UTF-8");
		System.out.println("doc:"+doc);
		System.out.println("Taille:"+oc.getContentLength());
		is.reset();
		
		for(int i=0;i<5;i++){System.out.print(i);Thread.sleep(1000);}
		
		InputStream is2 = (InputStream) oc.getContent();
		BufferedInputStream bis2 = new BufferedInputStream(is2);
		doc = IOUtils.toString(bis2,"UTF-8");
		System.out.println("doc:"+doc);
		
		getTab();
		getLignes();
		formatLignes();
		
		//System.out.print(".");Thread.sleep(200);System.out.print(".");Thread.sleep(200);System.out.println(".");Thread.sleep(200);
		
		//InputStream is2 = (InputStream) oc.getContent();
		doc = IOUtils.toString(is,"UTF-8");
		System.out.println(doc);
		
		getTab();
		getLignes();
		formatLignes2();
		
		afficher();
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
	
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {TestConnect t = new TestConnect();}
}
