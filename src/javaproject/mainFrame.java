package javaproject;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class mainFrame extends JFrame
{
	public LoginPanel loginpanel = null;
	public MainPanel mainpanel = null;
	public RegisterPanel registerpanel = null;

	private String uID = "";
	private String uName = "";
	private String uAge = "";
	private String uSex = "";
	private String uEdu = "";
	
	public String getuID()
	{
		return uID;
	}
	public void setuID(String uID)
	{
		this.uID = uID;
	}
	public String getuName()
	{
		return uName;
	}
	public void setuName(String uName)
	{
		this.uName = uName;
	}
	public String getuAge()
	{
		return uAge;
	}
	public void setuAge(String uAge)
	{
		this.uAge = uAge;
	}
	public String getuSex()
	{
		if(uSex.equals("1"))
		{
			return "남자";
		}
		else
		{
			return "여자";
		}
//		return uSex;
	}
	public void setuSex(String uSex)
	{
		this.uSex = uSex;
	}
	public String getuEdu()
	{
		if(uEdu.equals("1"))
		{
			return "중졸";
		}
		else if(uEdu.equals("2"))
		{
			return "고졸";
		}
		else if(uEdu.equals("3"))
		{
			return "대졸";
		}
		return "?";
	}
	public void setuEdu(String uEdu)
	{
		this.uEdu = uEdu;
	}

	
	public mainFrame()
	{
		mainFrame win = this;
		win.setTitle("Albamon");
		
		win.loginpanel = new LoginPanel(win);
		win.mainpanel = new MainPanel(win);
		win.registerpanel = new RegisterPanel(win);
		
		uID = new String("null");
		uName = new String("null");
		uAge = new String("null");
		uSex = new String("null");
		uEdu = new String("null");

		win.add(win.loginpanel);

		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		this.setVisible(true);
	}

	public void changePanel(String panelName)
	{
		this.getContentPane().removeAll();
		switch(panelName)
		{
		case "loginpanel":
			this.getContentPane().add(loginpanel);
			registerpanel.initCompo();
			break;
		case "mainpanel":
			this.getContentPane().add(mainpanel);
			loginpanel.initCompo();
			break;
		case "registerpanel":
			this.getContentPane().add(registerpanel);
			break;
		default :
			System.out.println("changePanel Error!!");
			
			
		}
		this.revalidate();
		this.repaint();
	}
	
	public static Connection getConnection() throws Exception
	{
		try
		{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/javadatabase";

			Class.forName(driver);

			Properties properties = new Properties();
			properties.setProperty("user", "root");
			properties.setProperty("password", "aa8197");
			properties.setProperty("useSSL", "false");
			properties.setProperty("autoReconnect", "true");

			Connection conn = DriverManager.getConnection(url, properties);
			System.out.println("Connected");
			return conn;
		} catch (Exception e)
		{
			System.out.println("Connection Failed");
		}
		return null;
	}

	public static void createTable() throws Exception
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = getConnection();
			Statement myStmt = con.createStatement();
			ResultSet myRs = myStmt.executeQuery("select * from user");
			while(myRs.next())
			{
				System.out.println(myRs.getString("id"));
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
}
