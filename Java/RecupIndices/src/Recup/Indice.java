package Recup;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Indice 
{
	private String nom;
	private double last;
	
	public Indice(double val, String nom, String pays)
	{
		this.nom = nom;
		this.last = val;
	}
	
	public void addVal(double d, Statement st, String type)
	{
		if(last!=d)
		{
			last = d;
			try {
				st.executeQuery("INSERT INTO "+type+" (ID,VAL) VALUES ((SELECT ID FROM INDICES WHERE NOM='"+nom+"'),"+d+")");
			} catch (SQLException e) 
			{
				show();
				e.printStackTrace();
			}
		}
	}
	
	public void show()
	{
		System.out.print(String.format("%-60s", nom));
		System.out.print(String.format("%-20s", "LAST : "+last));
		System.out.println();
	}
}
