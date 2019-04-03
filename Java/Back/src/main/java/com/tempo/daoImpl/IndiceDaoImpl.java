package com.tempo.daoImpl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;

import com.tempo.bean.Indice;

@Repository ("indiceDao")
public class IndiceDaoImpl 
{
	private JdbcTemplate jdbcTemplate;
	 
	@Autowired
	public void setDataSource(DataSource dataSource) { this.jdbcTemplate = new JdbcTemplate(dataSource);}
    
	public List<Indice> getIndices()
	{
		String SQL = "SELECT * FROM INDICES";
		List<Indice> indices = jdbcTemplate.query(SQL,new BeanPropertyRowMapper<Indice>(Indice.class));   
		return indices;
	}
}
