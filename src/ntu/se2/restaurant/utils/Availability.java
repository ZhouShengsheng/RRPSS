package ntu.se2.restaurant.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ntu.se2.restaurant.models.Reservation;
import ntu.se2.restaurant.models.Table;

public class Availability 
{
	Reservation reservation=new Reservation();
	// All reservations.
	public ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
	public ArrayList<Reservation> Occupied = new ArrayList<Reservation>();
	private static final DateUtil DATE_FORMAT=new DateUtil();
	public Availability() {
		
	}

	public ArrayList<Reservation> getOccupied(){
		return Occupied;
	}
	public void setOccupied(ArrayList<Reservation> Occupied){
		this.Occupied=Occupied;
	}
	
	public void getOccupiedtable() throws ParseException,IOException{
		reservation=new Reservation();
		Occupied=reservation.getCurrentReservation();
		
	}
	public void checkTable(String People)throws IOException, ParseException{
		reservation=new Reservation();
		Date date,Start;
		String date_use;
		Calendar calendar=Calendar.getInstance();
		date=calendar.getTime();
		date_use=DATE_FORMAT.getDate(date);
		Start=calendar.getTime();
		reservation.assignTable(date_use,Start,People);
		if(reservation.getName()!=null){
			Occupied.add(reservation);
			System.out.println(reservation.getTableNo()+" "+"is available");
		}
		else{
			System.out.println("There is no suitable table");
		}
		
	}
	
//    public boolean releaseTable(String tableNo){
//    	boolean unassignsuccessfully=false;
//    	for(int i=0;i<30;i++){
//    		if(table[i].getTableNo()==Integer.parseInt(tableNo)){
//    			if(table[i].getisOccupied()==1){
//    				table[i].setOccupied(0);
//    				unassignsuccessfully=true;
//    			}
//    		}
//    	}
//    	return unassignsuccessfully;
//    }

}
