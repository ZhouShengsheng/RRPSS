package ntu.se2.restaurant.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import ntu.se2.restaurant.utils.DateFormat;


public class Table {
	public int size, tableNo;
	public int isoccupied;
	private ArrayList<ReservationEntity> reserveList;
	public Order order = new Order();
	DateFormat DATE_FORMAT = new DateFormat();
	SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	public Table() {}
	public Table(int size, int tableNo, int isoccupied) {
		this.size = size;
		this.tableNo = tableNo;
		this.isoccupied = isoccupied;
	}
	
	public Order getOrder() { return order; }
	public void setOrder(Order order) { this.order = order; }
	
	public int getSize() { return size; }
	public void setSize(int size) { this.size = size; }
	
	public int getTableNo() { return tableNo; }
	public void setTableNo(int tableNo) { this.tableNo = tableNo; }
	
	public int getisOccupied() { return isoccupied; }
	public void setOccupied(int occupied) { this.isoccupied = occupied; }
	
	public ArrayList<ReservationEntity> getReservationAll() throws ParseException{
		try{
			reserveList=new ArrayList<ReservationEntity>();
			Scanner sc=new Scanner(new BufferedReader(new FileReader("reservation.txt")));
			sc.nextLine();
			
			while(sc.hasNext()){
				String temp[]=sc.nextLine().split(",");
				ReservationEntity r=new ReservationEntity();
				r.setDate(temp[0]);
				r.setName(temp[1]);
				r.setContact(temp[2]);
				r.setPeople(temp[3]);
				r.setStart(temp[4]);
				r.setTableNo(temp[5]);
				isoccupied = Integer.parseInt(temp[6]);
				reserveList.add(r);
			}
			sc.close();
				
				
			}catch (FileNotFoundException err1) {
		
		          err1.printStackTrace();
	        }
	        return reserveList;
     }
  public void reservationRemove() throws ParseException{
	  int index;
	  Scanner sc=new Scanner(System.in);
	  SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
	  Calendar calendar=Calendar.getInstance();
	  Date now=calendar.getTime();
	  
	  do{
		  int i=1;
		  reserveList=getReservationAll();
		  for(Iterator<ReservationEntity> j=reserveList.iterator();j.hasNext();){
			  ReservationEntity temp=j.next();
			  temp.setEnd();
			  System.out.println( i +" " +temp.getDate()+ "          " + temp.getName()+"            "+temp.getContact()+"         " +temp.getPeople()+"        "+sdf.format(temp.getStart())+ "                " + sdf.format(temp.getEnd()) + "          " + temp.getTableNo());
			  i++;
		  }
		  System.out.println("Which one to cancel.If no press 0:");
		  index=sc.nextInt();
		  if(index>0 && index<=reserveList.size()){
			  reserveList.remove(index-1);
			  try(PrintWriter updated = new PrintWriter(new BufferedWriter(new FileWriter("reservation.txt", false)))){
			     updated.println("date,name,contact,people,starttime,tableNo,occcupied");
					for(Iterator<ReservationEntity> j = reserveList.iterator(); j.hasNext();){
						ReservationEntity temp = j.next();
						updated.println(temp.getDate()+","+ temp.getName()+","+temp.getContact()+"," +temp.getPeople()+","+FORMAT.format(temp.getStart())+ "," + temp.getTableNo()+",");
						if(now.after(temp.getStart())&&now.before(temp.getEnd())){
							updated.print("1\n");
						}
						else
							updated.print("0\n");
					}
					System.out.println("Reservation Successful removed");
		     }catch(IOException e){
		    	 
		    	 System.err.println("IOEXception:"+e.getMessage());
		     }
				  
			  
		  }
		  else 
			  if(index==0)
			  break;
		  else
			  System.out.println("The reservation is not exist");
	  }while(index!=0);
	  
  }
  public ArrayList<ReservationEntity> getReservationThisDay(String Date) throws ParseException{
	  
	 reserveList=new ArrayList<ReservationEntity>();
	  try{		
		  
		    Scanner sc=new Scanner(new BufferedReader(new FileReader("reservation.txt")));
			sc.nextLine();
		    while(sc.hasNext()){
				String temp[]=sc.next().split(",");
				ReservationEntity r=new ReservationEntity();
				r.setDate(temp[0]);
				r.setName(temp[1]);
				r.setContact(temp[2]);
				r.setPeople(temp[3]);
				r.setStart(temp[4]);
				r.setTableNo(temp[5]);
				isoccupied = Integer.parseInt(temp[6]);
				if(r.getDate().equals(Date)){
				     reserveList.add(r);
				}
			}
			sc.close();
			
			}catch (FileNotFoundException err1) {
		
		          err1.printStackTrace();
	        }
	        return reserveList;
	        
   }
  public ArrayList<ReservationEntity> getReservationCurrent() throws ParseException{
	  
		reserveList=new ArrayList<ReservationEntity>();
		try{
			Scanner sc=new Scanner(new BufferedReader(new FileReader("data/reservation.txt")));
			
			while(sc.hasNext()){
				String temp[]=sc.next().split(",");
//				for (int i = 0; i < temp.length; i++) {
//					System.out.println(String.format("temp[%d]: %s", i, temp[i]));
//				}
//				System.out.println();
				ReservationEntity r=new ReservationEntity();
				if(temp[6].equals("1")){
					r.setDate(temp[0]);
					r.setName(temp[1]);
					r.setContact(temp[2]);
					r.setPeople(temp[3]);
					r.setStart(temp[4]);
					r.setTableNo(temp[5]);
				isoccupied = Integer.parseInt(temp[6]);
				reserveList.add(r);
				}
			}
			sc.close();
				
				
			}catch (FileNotFoundException err1) {
		
		          err1.printStackTrace();
	        }
	        return reserveList;
	        
   }  
   public boolean reserveTable(String date,Date Start,String People,String name,String Contact,String tableNum)throws ParseException{
	   boolean reservesucceessfully =false;
	   getReservationThisDay(date);
	   String pattern="HH:mm";
	   SimpleDateFormat sdf=new SimpleDateFormat(pattern);
	   Calendar calendar=Calendar.getInstance();
	   if(reserveList.size()>0){
		   for(Iterator<ReservationEntity> j=reserveList.iterator();j.hasNext();){
			   ReservationEntity temp=j.next();
			   if(temp.getPeople().equals(People)){
				   temp.setEnd();
				   calendar.setTime(Start);
				   calendar.add(Calendar.HOUR_OF_DAY, 1);
				   Date End=calendar.getTime();
				   if(!(Start.after(temp.getStart()) && Start.before(temp.getEnd())) || !(End.after(temp.getStart()) && End.before(temp.getEnd()))){
					   if(!((Start.getTime() == temp.getStart().getTime()) || (End.getTime() == temp.getEnd().getTime()))){
						  if(!(Start.getTime()<=(temp.getEnd().getTime()+4))){
						   if((Start.getTime() - End.getTime())/(60 * 1000) % 60 <= 60*60*1000){
							 try{  
							   String fileName= "reservation.txt";
							   FileWriter filewriter = new FileWriter(fileName,true); //the true will append the new data
							   filewriter.write("\n"+date + "," + name + "," + Contact +","+ People + "," +   sdf.format(Start) + ","+ tableNum +","+ "0");//appends the string to the file
							   filewriter.close();
							   reservesucceessfully = true;
							 }catch(IOException e){
								 System.err.println("IOEXception:"+e.getMessage());
							 }
							   
						   
						  }
					   }
						
				   }
				   
			   }
		   }
	   }
	   }
	   else{
		  try{
		   String fileName= "reservation.txt";
		   FileWriter filewriter = new FileWriter(fileName,true); //the true will append the new data
		   filewriter.write("\n"+date + "," + name + "," + Contact +","+ People + "," +   sdf.format(Start) + ","+ tableNo +","+ "0");//appends the string to the file
		   filewriter.close();
		   reservesucceessfully = true;
		  }catch(IOException e){
				 System.err.println("IOEXception:"+e.getMessage());
		  }
	   }
	   return reservesucceessfully;
	   
   }
   public ReservationEntity Reserveandallocate(String date,Date Start,String People,String tableNum) throws ParseException, IOException{//For today the reservation made before will be conducted so need change occupied to 1
	    Scanner sc=new Scanner(System.in);
	    reserveList=new ArrayList<ReservationEntity>();
	    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    Date End=DATE_FORMAT.addAnHour(Start);
	    for(Iterator<ReservationEntity> j= reserveList.iterator(); j.hasNext();){
	    	ReservationEntity temp = j.next();
	    	if(Integer.parseInt(temp.getTableNo())<Integer.parseInt(tableNum)){
	    			j.remove();
	    		}
	    }
	    if(reserveList.size()>0){
	    
	    		for(ReservationEntity i: reserveList){
	    			if(Integer.parseInt(i.getPeople())<=size && Integer.parseInt(i.getPeople())>(size-2)){
	    					Date iStart = sdf.parse(i.getDate()+ " " + DATE_FORMAT.getTime(i.getStart()));
	    					Date iEnd =DATE_FORMAT.addAnHour(iStart);
	    					i.setEnd();
	    			
	    					if(!((Start.after(iStart) && Start.before(iEnd) && tableNum.toString().equals(i.getTableNo()))|| End.after(iStart) && End.before(iEnd) && tableNum.toString().equals(i.getTableNo()) )){
	    	
	    						if(!((Start.equals(iStart)) || End.equals(iEnd))){
	    	
	    		    				if((Start.getTime() - End.getTime())/(60 * 1000) % 60 <= 60*60*1000){
	    		    					System.out.println("Enter Customer Name");
	    		    					String name = sc.nextLine();
	    		    					System.out.println("Enter Customer Contact");
	    		    					String contact = sc.nextLine();
	    								String filename= "reservation.txt";
	    								FileWriter filewriter = new FileWriter(filename,true); 
	    								filewriter.write("\r"+date + "," + "," + name + "," + contact + "," + People + "," + tableNum+","+ DATE_FORMAT.getTime(Start)+","+ "1");
	    								filewriter.close();
	    								this.isoccupied = 1;
	    								return i;
	    		    				}else{
	    		    					System.out.println("The time may overlapped with others");
	    		    					return null;
	    		    				}
	    						}else{
	    							System.out.println("This time already overlapped with others");
	    							return null;
	    						}
	    	  			}else{ 
	    						System.out.println("This time is reserved already");
	    						return null;
	    					}
	    				}
	    			}
	    		}else{
	    		System.out.println("Enter Customer Name");
	    			String name = sc.nextLine();
	    			System.out.println("Enter Customer Contact");
	    			String contact = sc.nextLine();
	    			String filename= "reservation.txt";
	    			FileWriter filewriter = new FileWriter(filename,true); 
	    			filewriter.write("\r"+date + "," + "," + name + "," + contact + "," + People + "," + tableNum+","+ DATE_FORMAT.getTime(Start)+","+ "1");
					filewriter.close();
	    			this.isoccupied = 1;
	    			return new ReservationEntity(date, DATE_FORMAT.getTime(Start),People,name,contact,tableNum);
	    		}
	    		return null;
	    	
	    
	    		}	  
   public void removeReservationOnPayment(int tableNum) throws ParseException{
		//tableNum, occupied bit=1
		//writing to file
		Calendar cal = Calendar.getInstance();
		Date currentTime = cal.getTime();
		reserveList = getReservationAll();
		for(Iterator <ReservationEntity> r = reserveList.iterator(); r.hasNext(); ){
			ReservationEntity reservation = r.next(); 
			if(Integer.parseInt(reservation.getTableNo())==tableNum){
				r.remove();
			}
		}
		//tableNum, occupied bit=1
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("reservation.txt", false)))) {
		out.println("date,name,contact,people,start,tableNo,occupied");
		for(Iterator<ReservationEntity> r = reserveList.iterator(); r.hasNext();){
			ReservationEntity reservation = r.next();
			reservation.setEnd();
		out.print(reservation.getDate()+","+ DATE_FORMAT.getTime(reservation.getStart())+","+reservation.getName()+","+reservation.getPeople()+","+
	    			reservation.getContact()+","+ reservation.getTableNo());
			if(reservation.getStart().before(currentTime)&&currentTime.before(reservation.getEnd())){
				out.print(",1\n");
			}
			else
				out.print(",0\n");
		}
		System.out.println("Reservation Successfully removed");
		}catch (IOException e) {
	    //exception handling left as an exercise for the reader
		}
   }
                
	   
   }

   
