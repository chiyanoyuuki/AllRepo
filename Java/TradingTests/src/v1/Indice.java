package v01;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Indice 
{
	private String nom;
	private double last;
	
	public Indice(double val, String nom)
	{
		this.nom = nom;
		this.last = val;
	}
	
	public void addVal(double d, Statement st) throws SQLException 
	{
		if(last!=d)
		{
			last = d;
			st.executeQuery("INSERT INTO VALEURS_INDICES (ID,VAL) VALUES ((SELECT ID FROM INDICES WHERE NOM='"+nom+"'),"+d+")");
		}
	}
	
	public void show()
	{
		System.out.print(String.format("%-60s", nom));
		System.out.print(String.format("%-20s", "LAST : "+last));
		System.out.println();
	}
}
