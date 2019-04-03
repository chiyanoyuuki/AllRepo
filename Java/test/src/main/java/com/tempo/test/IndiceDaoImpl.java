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
		String SQL = "SELECT * FROM INDICES ORDER BY NOM";
		List<Indice> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<Indice>(Indice.class));   
		return indices;
	}
	
	public List<IndiceVal> getIndicesVals(int ID)
	{
		String SQL = "SELECT VAL, DATE FROM VALEURS_INDICES WHERE ID="+ID+" ORDER BY DATE";
		List<IndiceVal> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<IndiceVal>(IndiceVal.class));   
		return indices;
	}
}
