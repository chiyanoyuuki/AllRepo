package com.tempo.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;

@Repository
public class IndiceDaoImpl 
{
	private JdbcTemplate jdbcTemplate;
	 
	@Autowired
	public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource);}
    
	public List<Indice> getIndices()
	{
		String SQL = "SELECT * FROM INDICES I WHERE (SELECT COUNT(*) FROM VALEURS_INDICES V WHERE V.ID=I.ID > 0) ORDER BY NOM";
		System.out.println(SQL);
		List<Indice> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<Indice>(Indice.class));   
		return indices;
	}
	
	public List<IndiceVal> getIndicesVals(int ID, String TIME)
	{
		List<IndiceVal> indices = new ArrayList<IndiceVal>();
		if(!TIME.equals("Today"))
		{
			try
			{
				String files = "*.csv";
				if(TIME.equals("Year")) 
				{
					int curYear = Calendar.getInstance().get(Calendar.YEAR);
					files = curYear+"-*.csv";
				}
				else if(TIME.equals("Month"))
				{
					Calendar c = Calendar.getInstance();
					int curYear = c.get(Calendar.YEAR);
					int curMonth = c.get(Calendar.MONTH)+1;
					files = curYear+"-"+String.format("%02d", curMonth)+"-*.csv";
				}
				String commande = "sed -n \"/^"+ID+";.*/"+getParam(ID, files)+"\" "+files;
		        indices.addAll(getIndices(commande));
			} catch (IOException | ClassNotFoundException | SQLException | InterruptedException e) 
			{System.out.println("PROBLEME LORS DE LA RECUPERATION DANS LES FICHIERS");}
		}
		int modulo = getModulo(ID);
		String SQL = "SELECT LIGNES.VAL, LIGNES.DATE FROM(SELECT ROW_NUMBER() OVER(ORDER BY DATE) AS NUM, VAL, DATE FROM VALEURS_INDICES WHERE ID="+ID+") LIGNES WHERE MOD(LIGNES.NUM,"+modulo+")=0";
		System.out.println(SQL);
		indices.addAll(jdbcTemplate.query(SQL,new BeanPropertyRowMapper<IndiceVal>(IndiceVal.class)));   
		return indices;
	}
	
	private int getModulo(int ID)
	{
		String SQL = "SELECT COUNT(*) AS NB FROM VALEURS_INDICES WHERE ID="+ID;
		List<Map<String,Object>> result = jdbcTemplate.queryForList(SQL);
		int nb = Integer.parseInt(""+result.get(0).get("NB"));
		
		int modulo = 1;
        int tmp = nb;
		while(tmp>2000)
		{
			modulo++;
			tmp = nb/modulo;
		}
		
		return modulo;
	}
	
	private String getParam(int ID, String files) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		String commande = "sed -n \"/^"+ID+";.*/p\" "+files+" | wc -l";
		BufferedReader reader = run(commande);
		int nbLines = Integer.parseInt(reader.readLine());
		int cpt = 1;
		String param = "{p;";
		int tmp = nbLines;
		while(tmp>2000)
		{
			cpt++;
			param += "n;";
			tmp = nbLines/cpt;
		}
		
		param +="}";
		reader.close();
		return param;
	}
	
	private List<IndiceVal> getIndices(String commande) throws IOException, ClassNotFoundException, SQLException, InterruptedException
	{
        List<IndiceVal> indices = new ArrayList<IndiceVal>();
		BufferedReader reader = run(commande);
        String line;
        while ((line = reader.readLine()) != null) {indices.add(new IndiceVal(Double.parseDouble(line.substring(line.indexOf(";")+1,line.lastIndexOf(";"))),line.substring(line.lastIndexOf(";")+1)));}
		reader.close();
		return indices;
	}
	
	private BufferedReader run(String s) throws SQLException, ClassNotFoundException, IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",s);
		pb.directory(new File("C:/Users/ASC Arma/Desktop/Trading/Java/BDD/cache/"));
        Process process = pb.start();
        List<IndiceVal> indices = new ArrayList<IndiceVal>();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader;
	}
	
	public List<IndiceVal> getIndicesNewVals(int ID, String DATE)
	{
		String SQL = "SELECT VAL, DATE FROM VALEURS_INDICES WHERE ID="+ID+" AND DATE>'"+DATE+"' ORDER BY DATE";
		System.out.println(SQL);
		List<IndiceVal> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<IndiceVal>(IndiceVal.class));   
		return indices;
	}

	public List<String> getIndicesTotal(int ID) 
	{
		List<String> totaux = new ArrayList<String>();
		
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH)+1;
		try
		{
			String allTime = getCount(ID,"*.csv");
			String year = getCount(ID,curYear+"-*.csv");
			String month = getCount(ID,curYear+"-"+String.format("%02d", curMonth)+"-*.csv");
			totaux.add(allTime);totaux.add(year);totaux.add(month);
		} catch (IOException | ClassNotFoundException | SQLException | InterruptedException e) 
		{System.out.println("PROBLEME LORS DE LA RECUPERATION DANS LES FICHIERS");}
		
		String SQL = "SELECT COUNT(*) AS NB FROM VALEURS_INDICES WHERE ID="+ID;
		List<Map<String,Object>> result = jdbcTemplate.queryForList(SQL);
		String day = ""+result.get(0).get("NB");
		totaux.add(day);
		
		return totaux;
	}
	
	private String getCount(int ID, String files) throws ClassNotFoundException, SQLException, IOException, InterruptedException
	{
		String commande = "sed -n \"/^"+ID+";.*/p\" "+files+" | wc -l";
		BufferedReader reader = run(commande);
		String nbLines = reader.readLine();
		return nbLines;
	}
}
