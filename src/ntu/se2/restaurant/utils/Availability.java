package ntu.se2.restaurant.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ntu.se2.restaurant.models.ReservationEntity;
import ntu.se2.restaurant.models.Table;

public class Availability 
{
	ReservationEntity reservation=new ReservationEntity();
	// All reservations.
	public ArrayList<ReservationEntity> reservationList = new ArrayList<ReservationEntity>();
	public ArrayList<ReservationEntity> Occupied = new ArrayList<ReservationEntity>();
	private static final DateUtil DATE_FORMAT=new DateUtil();
	public Availability() {
		
	}

	public ArrayList<ReservationEntity> getOccupied(){
		return Occupied;
	}
	public void setOccupied(ArrayList<ReservationEntity> Occupied){
		this.Occupied=Occupied;
	}
	
	public void getOccupiedtable() throws ParseException,IOException{
		reservation=new ReservationEntity();
		Occupied=reservation.getCurrentReservation();
		
	}
	public void checkTable(String People)throws IOException, ParseException{
		reservation=new ReservationEntity();
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
