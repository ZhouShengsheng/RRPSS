package ntu.se2.restaurant.models;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ntu.se2.restaurant.utils.DateFormat;

public class ReservationEntity {
    //String RULE="HH:mm";
	public static final SimpleDateFormat FORMAT=new SimpleDateFormat("HH:mm");
	private String name,people,contact,date,tableNo;
	private Date start,end;
	Calendar calendar = Calendar.getInstance();
	private static final DateFormat DATE_FORMAT=new DateFormat();
	public Table tables[]=new Table[30];
	Table table;
	// 1. Given time and no of people, find the table with suitable size and check if is available
	// 2. 30 minutes after reservation, check if the customers have arrived the restaurant.
	// If the customers have come, remove the reservation and set the table to "occupied"
	// If not, remove the reservation and set the table to "empty"
	public ReservationEntity(){
		int size=2;
		for(int i=0;i<30;i++){
			tables[i]=new Table(size,i,0);
			if(i>9 && i<=19)
				size=4;
			if(i>19 &&i<=24)
				size=8;
			if(i>24 && i<=29)
				size=10;
		}
		
	}
public ReservationEntity(String date, String startTime,String People,String name,String contact,String tableNo) throws ParseException{
		
		this.date = date;
		this.start = FORMAT.parse(startTime);
		this.people = People;
		this.name = name;
		this.contact = contact;
		this.tableNo = tableNo;
		
		int size=2;
		for(int i=0;i<30;i++){
			tables[i]=new Table(size,i,0);
			if(i>9 && i<=19)
				size=4;
			if(i>19 &&i<=24)
				size=8;
			if(i>24 && i<=29)
				size=10;
		}
	}
	
	public String getName(){
		return name;
	}
	public void setName(String inName){
		this.name=inName;
	}
	public String getPeople(){
		return people;
	}
	public void setPeople(String inPeople){
		this.people=inPeople;
	}
	public String getContact(){
		return contact;
	}
	public void setContact(String inContact){
		this.contact=inContact;
	}
	public String getDate(){
		return date;
	}
	public void setDate(String inDate){
		this.date=inDate;
	}

	public String getTableNo(){
		return tableNo;
	}
	public void setTableNo(String inTableNo){
		this.tableNo=inTableNo;
	}
	public Table[] getTables(){
		return tables;
	}
	public void setTables(Table[] tableL){
		this.tables=tableL;
	}
	public Date getStart(){
		return start;
	}
    public void setStart(String startTime)
    {
    	try{
    	this.start=FORMAT.parse(startTime);
    	}
    	catch (ParseException e1)
    	{
    		System.out.println("Invalid ");
    	}
    }
    public Date getEnd(){
		return end;
	}
    public void setEnd() throws ParseException{
    	calendar.setTime(start);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
    	this.end = calendar.getTime();
    }
    
    public boolean canReserve() throws ParseException, IOException
    {
    	for(int i=0;i<30;i++)
    	{
    		if (Integer.parseInt(people)<=tables[i].getSize() && Integer.parseInt(people)<=(tables[i].getSize()-2))
    		{ tableNo=Integer.toString(tables[i].getTableNo()+1);
    		   return tables[i].reserveTable(date, start, people, name, contact, tableNo);
    		}
    		
    	}
    	return false;
    	
    		
    }
    public boolean checkReasonable(){// reserve for coming day instead of past days
        Date now=new Date();
    	calendar.setTime(start);
    	Date reserve=calendar.getTime();
    	System.out.println("The reservation is reasonable?");
    	if(now.compareTo(reserve)>=0){
    		System.out.println("The reservation is not reasonable");
    		return false;
    	}
    		
    		
    	else{
    		System.out.println("The reservation is reasonable");
    		return true;
    	}
    	
    	
    }
    public ArrayList<ReservationEntity> getAllReservation() throws ParseException,IOException{
    	table=new Table();
    	return table.getReservationAll();
    	
    	
    }
    public ArrayList<ReservationEntity> getThisDayReservation(String date) throws ParseException,IOException{
    	table=new Table();
    	return table.getReservationThisDay(date);
    }
    public ArrayList<ReservationEntity> getCurrentReservation() throws ParseException,IOException{
    	table=new Table();
    	return table.getReservationCurrent();
    }
    public void removeReservation() throws ParseException, IOException{
    	table=new Table();
    	table.reservationRemove();
    }
    public void assignTable(String date,Date start,String people) throws ParseException,IOException{
    	for(int i=0;i<30;i++){
    		if(Integer.parseInt(people)<=tables[i].getSize()){
    			tableNo=Integer.toString(tables[i].getTableNo()+1);
    			ReservationEntity r=tables[i].Reserveandallocate(date,start,people,tableNo);
    			if(r!=null){
    				this.setContact(r.getContact());
    				this.setName(r.getName());
    				this.setDate(r.getDate());
    				this.setPeople(r.getPeople());
    				this.setStart(DATE_FORMAT.getTime(r.getStart()));
    				this.setEnd();
    				break;
    				
    				
    				
    				
    			}
    		}
    	}
    }
    
	
	
	

}
