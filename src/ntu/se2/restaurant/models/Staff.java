package ntu.se2.restaurant.models;

public class Staff 
{
	private String staffName, gender, jobTitle, staffID;
	
	public Staff()
	{
		
	}
	
	public Staff(String staffID,String staffName, String gender, String jobTitle)
	{
		this.staffID = staffID;
		this.staffName = staffName;
		this.gender = gender;
		this.jobTitle = jobTitle;
	}
		
	public String getStaffID()
	{
		return staffID;
	}
	
	public void setStaffID(String temp)
	{
		this.staffID = temp;
	}
	
	public String getName()
	{
		return staffName;
	}
	
	public void setName(String temp)
	{
		this.staffName = temp;
	}
	
	public String getGender()
	{
		return gender;
	}
	
	public void setGender(String temp)
	{
		this.gender = temp;
	}
	
	public String getjobTitle()
	{
		return jobTitle;
	}
	
	public void setjobTitle(String temp)
	{
		this.jobTitle = temp;
	}
	
	
}
