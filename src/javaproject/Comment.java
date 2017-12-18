package javaproject;

public class Comment
{
	private String company;
	private String userid;
	private String time;
	private String comment;
	private int rating;
	
	public Comment(String a, String b, String c, String d, int e)
	{
		this.company = a;
		this.userid = b;
		this.time = c;
		this.comment = d;  
		this.rating = e;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}
	public int getRating()
	{
		return this.rating;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}
}
