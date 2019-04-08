package com.tempo.test;

import java.util.List;

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
	
	public List<IndiceVal> getIndicesVals(int ID)
	{
		String SQL = "SELECT VAL, DATE FROM VALEURS_INDICES WHERE ID="+ID+" ORDER BY DATE";
		System.out.println(SQL);
		List<IndiceVal> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<IndiceVal>(IndiceVal.class));   
		return indices;
	}
	
	public List<IndiceVal> getIndicesNewVals(int ID, String DATE)
	{
		String SQL = "SELECT VAL, DATE FROM VALEURS_INDICES WHERE ID="+ID+" AND DATE>'"+DATE+"' ORDER BY DATE";
		System.out.println(SQL);
		List<IndiceVal> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<IndiceVal>(IndiceVal.class));   
		return indices;
	}
}
