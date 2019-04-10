package v1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Test 
{
	public Test() throws SQLException, ClassNotFoundException, IOException, InterruptedException
	{
		Calendar ca = Calendar.getInstance();
		int y = ca.get(Calendar.YEAR);
		int m = ca.get(Calendar.MONTH)+1;
		
		System.out.println(y+"-"+m+"-*.csv");
		
		
		Class.forName("org.mariadb.jdbc.Driver");
		java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://212.227.203.214:3306/trading?user=ADMIN&password=Chiyanoyuuki1512.");
		Statement st = connection.createStatement();
		int ID=20;
		String TIME = "Month";
		//-----------------------------------------------------------
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
			files = curYear+"-"+curMonth+"-*.csv";
		}
		System.out.println(files);
		String commande = "sed -n \"/^"+ID+";.*/"+getParam(ID, files)+"\" "+files;
        List<IndiceVal> indices = new ArrayList<IndiceVal>();
        indices.addAll(getIndices(commande));
        
        //------------------------------------------------------------------------------
        ResultSet rsx = st.executeQuery("SELECT COUNT(*) FROM VALEURS_INDICES WHERE ID="+ID);
        rsx.next();
        int nb = Integer.parseInt(rsx.getString(1));
		int modulo = 1;
        int tmp = nb;
		while(tmp>2000)
		{
			modulo++;
			tmp = nb/modulo;
		}
        //------------------------------------------------------------------------------
        System.out.println(modulo);
        String SQL = "SELECT LIGNES.VAL, LIGNES.DATE FROM(SELECT ROW_NUMBER() OVER(ORDER BY DATE) AS NUM, VAL, DATE FROM VALEURS_INDICES WHERE ID="+ID+") LIGNES WHERE MOD(LIGNES.NUM,"+modulo+")=0";
		System.out.println(SQL);
		//------------------------------------------------------------------------------
		ResultSet rs = st.executeQuery(SQL);
		int cpt=0;
		while(rs.next()){cpt++;indices.add(new IndiceVal(Double.parseDouble(rs.getString(1)),rs.getString(2)));}
		System.out.println(nb + " <==> " + cpt);
        System.out.println(indices.size());
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
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException
	{			
		Test t = new Test();
	}
}
