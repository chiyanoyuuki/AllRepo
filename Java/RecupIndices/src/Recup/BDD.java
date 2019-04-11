package Recup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class BDD extends TimerTask
{
	private String ojd,hier;
	private Statement st;
	
	@Override
	public void run()
	{ 
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		ojd = f.format(c.getTime()) + " 00:00:00";
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)-1);
		hier = f.format(c.getTime()) + " 00:00:00";		
		
		try 
		{
			Class.forName("org.mariadb.jdbc.Driver");
			java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/trading?user=root&password=Chiyanoyuuki1512.");
			st = connection.createStatement();
		} catch (SQLException | ClassNotFoundException e2) {show("Erreur lors de la connection à la base de données");}
		
		try {write("cfd");write("futures");} catch (SQLException | IOException e) {show("Impossible de récupérer le cache");}
	}
	
	private void write(String type) throws SQLException, IOException
	{
		show("Lancement du cache "+type);
		ResultSet rs = st.executeQuery("SELECT V.ID, V.VAL AS VALEUR, V.DATE FROM INDICES I, "+type+" V WHERE I.ID=V.ID AND DATE >= '"+hier+"' AND DATE < '"+ojd+"' ORDER BY V.ID, DATE");
		
		File tmpDir = new File("./cache/"+type+"/"+hier.substring(0,10)+".csv");
		if(tmpDir.exists()) {System.out.println("LE FICHIER EXISTE DEJA");}
		else
		{
			FileWriter fw = new FileWriter(tmpDir);
			
			ResultSetMetaData meta = rs.getMetaData();
			for(int x=1;x<meta.getColumnCount()+1;x++)
			{
				fw.write(meta.getColumnLabel(x)+";");
			}
			fw.write("\r\n");
			
			int cpt = 0;
			while(rs.next())
			{
				cpt++;
				for(int x=1;x<meta.getColumnCount()+1;x++)
				{
					fw.write(rs.getString(x)+";");
				}
				fw.write("\r\n");
				if(cpt%10000==0)System.out.println(cpt);
			}
			
			fw.close();
			System.out.println("WRITING END");
			
			rs = st.executeQuery("DELETE FROM VALEURS_INDICES WHERE DATE >= '"+hier+"' AND DATE < '"+ojd+"'");
		}
	}
	
	private void show(String s)
	{
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(String.format("%-30s", t) + " | " + s);
	}
}
