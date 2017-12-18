package javaproject;

import java.sql.Connection;
import java.sql.Statement;

public class Job
{
	public Job(String title, String link, String name, String content, String kind, String location, String subway,
			String education, String age_sex, String salary, String end)
	{
		super();
		this.title = title;
		this.link = link;
		this.name = name;
		this.content = content;
		this.kind = kind;
		this.location = location;
		this.subway = subway;
		this.education = education;
		this.age_sex = age_sex;
		this.salary = salary;
		this.end = end;
	}
	public Job()
	{
		
	}

	private String title;
	private String link;
	private String name; // 근무기업명
	private String content; // 모집내용
	private String kind; // 직종
	private String location;// 지역
	private String subway; // 지하철역
	private String education;// 학력
	private String age_sex; // 연령,성별
	private String salary; // 급여
	private String end; // 마감일

	public void printinfo()
	{
		System.out.println(title +  "  \n"  +link +  "  \n"  +name +  "  \n"  +content +  "  \n"  +kind +  "  \n"  +location +  " \n "  +subway
				 +  "  \n"  +education +  "  \n"  +age_sex +  "  \n"  +salary +  "  \n"  +end);
	}
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getKind()
	{
		return kind;
	}

	public void setKind(String kind)
	{
		this.kind = kind;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getSubway()
	{
		return subway;
	}

	public void setSubway(String subway)
	{
		this.subway = subway;
	}

	public String getEducation()
	{
		return education;
	}

	public void setEducation(String education)
	{
		this.education = education;
	}

	public String getAge_sex()
	{
		return age_sex;
	}

	public void setAge_sex(String age_sex)
	{
		this.age_sex = age_sex;
	}

	public String getSalary()
	{
		return salary;
	}

	public void setSalary(String salary)
	{
		this.salary = salary;
	}

	public String getEnd()
	{
		return end;
	}

	public void setEnd(String end)
	{
		this.end = end;
	}

	public void addDB(String userID, Connection con)
	{
		try
		{
			Statement myStmt = con.createStatement();
			String sql = "insert into favorjob "
					+ " (userid, title, link, name, content, kind, location, subway, education, age_sex, salary, end )"
					+ " values ('" + userID + "', '" + this.getTitle() + "', '" + this.getLink() + "', '"
					+ this.getName() + "', '" + this.getContent() + "', '" + this.getKind() + "', '"
					+ this.getLocation() + "', '" + this.subway + "', '" + this.education + "', '" + this.getAge_sex()
					+ "', '" + this.getSalary() + "', '" + this.getEnd() + "')";
			myStmt.executeUpdate(sql);
		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}