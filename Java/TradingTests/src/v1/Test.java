package v1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class Test 
{	
	public static final long VINGT_QUATRE_HEURES = 2000;
	
	public Test(String s) throws SQLException, ClassNotFoundException, IOException, InterruptedException
	{ 
		System.out.println("Arg1 : "+s);
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException
	{			
		System.out.println(Arrays.toString(args));
		if(args.length<1)
		{
			System.out.println("Pas assez d'arguments");
		}
		else
		{
			Test t = new Test(args[0]);
		}
	}
}
