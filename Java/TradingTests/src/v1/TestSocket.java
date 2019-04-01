package v1;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class TestSocket 
{
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException
	{
		String webPage = "https://fr.investing.com/indices/major-indices";

		Connection c = Jsoup.connect(webPage);
		
        String html = c.get().html();
        System.out.println(html.substring(0,100));
        for(int i=0;i<5;i++){System.out.print(i);Thread.sleep(1000);}
        html = c.get().html();
        System.out.println(html.substring(0,100));
	}   
}
