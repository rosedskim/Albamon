package javaproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ParseURL
{
	static public String getData(String abc)
	{
		// Make a URL to the web page
		String line ="null2";
		String[] lines;
		URL url;
		try
		{
			url = new URL(abc);

			// Get the input stream through URL Connection
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			// read each line and write to System.out
			while ((line = br.readLine()) != null)
			{

				if (line.indexOf(" <i class=\"icon marker\"></i>") != -1)
				{
					line = br.readLine();
					int index1 = line.indexOf("<span>");
					int index2 = line.indexOf("</span>");
					
					line = line.substring(index1 + 6, index2); // 모집내용
					if(line.indexOf("(")!=-1)
					{
						index1 = line.indexOf("(");
						line = line.substring(0, index1);
					}
					lines = line.split("\\s");
					if(lines.length>4)
					{
						line = lines[0]+ " " +lines[1]+ " " +lines[2]+ " " +lines[3]+ " " +lines[4];
					}
					else
					{
						line = lines[0]+ " " +lines[1]+ " " +lines[2]+ " " +lines[3];
						
					}
					break;
				}
			}
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}
}
