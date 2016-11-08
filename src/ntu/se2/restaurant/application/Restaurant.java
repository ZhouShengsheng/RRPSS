package ntu.se2.restaurant.application;


import java.util.*;

import ntu.se2.restaurant.controllers.ItemController;
import ntu.se2.restaurant.controllers.OrderController;
import ntu.se2.restaurant.controllers.SystemManagement;
import ntu.se2.restaurant.controllers.PromotionController;
import ntu.se2.restaurant.models.ReservationEntity;
import ntu.se2.restaurant.models.Staff;
import ntu.se2.restaurant.utils.Availability;
import ntu.se2.restaurant.utils.ScannerUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Restaurant 
{

	static ReservationEntity r;
	static ArrayList<ReservationEntity> reserve;
	static SimpleDateFormat FORMAT;
	static SystemManagement sysm;
	static Staff staff;
	Availability seatMan;

	public static void main(String[] args) throws ParseException, IOException
	{
		Scanner sc = ScannerUtil.scanner;
		Availability seatMan = new Availability();
		seatMan.getOccupiedtable();
		staff = new Staff();
		int choice;
		do
		{
			System.out.println("Choose an Option");
			System.out.println("1.Manage Menu");
			System.out.println("2.Manage Promotion");
			System.out.println("3.Manage Order");
			System.out.println("4.Manage Reservation");
			System.out.println("5.Check Table");
			System.out.println("6.Print Revenue");
			//System.out.println("sc.nextLine(): " + sc.nextLine());
			choice = sc.nextInt();
			System.out.println("choice: " + choice);
			
			switch(choice)
			{
				// Manage Menu
				case 1: {
					System.out.println("1.Create a new menu item");
			        System.out.println("2.Update an existing menu item");
			        System.out.println("3.Remove an existing menu item");
			        System.out.println("4.Back to Main Menu");
			        ItemController ic = ItemController.sharedInstance();
			        int subChoice = sc.nextInt();
			        sc.skip(System.lineSeparator());
			        switch(subChoice)
			        {
			        case 1: if(ic.createItem())
			        	        System.out.println("Successfully Added!");
			                else 
			                	System.out.println("Process Failed!");
			                break;
			        case 2: ic.updateItem();
			                break;
			        case 3: ic.deleteItem();
	                        break;
			        case 4: break;
			        }
			        break;
				}
				// Manage Promotion
				case 2: {
					System.out.println("1.Create a new promotion");
	                System.out.println("2.Update an existing promotion");
	                System.out.println("3.Remove an existing promotion");
	                System.out.println("4.Back to Main Menu");
	                PromotionController pc = PromotionController.sharedInstance();
	                int subChoice = sc.nextInt();
			        sc.skip(System.lineSeparator());
			        switch(subChoice)
	                {
	                  case 1: if(pc.createPromo())
	        	                 System.out.println("Successfully Added!");
	                          else 
	                	         System.out.println("Process Failed!");
	                          break;
	                case 2: pc.updatePromo();
	                        break;
	                case 3: pc.deletePromo();
                            break;
	                case 4: break;
	                }
	                break;
				}
	            // Manage Order   
				case 3: {
					System.out.println("1.Create/Add item to Order");
	                System.out.println("2.View an existing Order");
	                System.out.println("3.Remove an item from an Order");
	                System.out.println("4.Print Order Invoice");
	                System.out.println("5.Back to Main Menu");
	                OrderController oc = OrderController.sharedInstance();
	                int subChoice = sc.nextInt();
			        sc.skip(System.lineSeparator());
			        switch(subChoice)
	                {
	                  case 1: {
	                	  oc.createOrAddItemToOrder();
	                	  break;
	                  }
	                 case 2: {
	                	 oc.viewOrder();
                         break;
	                 }
	                 case 3:int tableNo2;
	                        boolean result2 = false;
	                        System.out.println("Enter the table number:");
	                        tableNo2 = sc.nextInt();
	                        for(ReservationEntity r:seatMan.Occupied)
	                        {
	               	           if(Integer.parseInt(r.getTableNo())==tableNo2)
	               	           {
	               		        r.tables[tableNo2].order.removeOrder();
	               		        result2 = true;
	               		        break;
	               	           }
	                        }
	                	     if(result2)
		                        System.out.println("Successfully Deleted!");
	                         else 
	        	                System.out.println("Process Failed!");
	                         break;
	                 case 4: int tableNo3;
	                         System.out.println("Enter the table number:");
	                         tableNo3 = sc.nextInt();
	                         for(ReservationEntity r:seatMan.Occupied)
	                         {
	        	               if(Integer.parseInt(r.getTableNo())==tableNo3)
	        	               {
	        		              r.tables[tableNo3].order.printInvoice(staff,r);
	        		              r.tables[tableNo3].reservationRemove();
	        		              break;
	        	               }
	                         }
	                	     break;
	                	     
	                 case 5: break;
	                }
	                break;
				}
	            // manage reservation
				case 4: {
					System.out.println("1.Create a new Reservation");
	                System.out.println("2.Check Reservation");
	                System.out.println("3.Remove a Reservation");
	                System.out.println("4.Back to Main Menu");
	                int subChoice = sc.nextInt();
			        sc.skip(System.lineSeparator());
			        switch(subChoice)
	                {
	                 case 1: if(createreservation())
	                            System.out.println("Successfully Added!");
	                         else 
		                        System.out.println("Process Failed!");
	                         break;
	                 case 2: checkreservation(sc);
	                         break;
	                 case 3:r = new ReservationEntity();
	                        r.removeReservation();
	                        System.out.println("Successfully Deleted!");
	                        break;
	               case 4:  break;
	               }
	               break;
				}
	            // Check Table       
				case 5: {
					System.out.println("The number of the customers:");
			        String people=sc.next();
			        seatMan.checkTable(people);
			        break;
				}
				// Print Revenue
				case 6: {
					System.out.println("Revenue sorted by?");
					System.out.println("1. By date");
					System.out.println("2. By month");
					System.out.println("3. Back to Main Menu");
					int subChoice = sc.nextInt();
			        sc.skip(System.lineSeparator());
			        switch(subChoice) {
						case 1: sysm = new SystemManagement();
					    		sysm.printRevenue(1, sc);
					    		break;
						case 2: sysm = new SystemManagement();
					    		sysm.printRevenue(2, sc);
					    		break;
						case 3: break;
					}
				
				    break;
				}
				default: {
					System.out.println("Invalid Input!");
				     break; 
				}
			
			}
		}while(choice>=1&& choice <=6);
		sc.close();
	}

	private static void checkreservation(Scanner sc) throws ParseException,IOException
	{
		String name;
		//Scanner sc = new Scanner(System.in);
		System.out.println("Enter the customer name of the reservation");
		name = sc.next();
		r = new ReservationEntity();
		reserve = r.getAllReservation();
		FORMAT = new SimpleDateFormat("HH:mm");
		System.out.println("-------------Reservation--------------");
		System.out.println("Date              Time            Table No.            Customer Name               Contact Number                No. of people      ");
		for (int i = 0; i < reserve.size(); i++) {
			ReservationEntity temp = reserve.get(i);
			if(temp.getName().equals(name))
			{
				temp.setEnd();
				System.out.println(temp.getDate()+"         "+FORMAT.format(temp.getStart())+"-"+FORMAT.format(temp.getEnd())+"                     "+temp.getTableNo()+"                  "+temp.getName()+"                   "+temp.getContact()+"                    "+temp.getPeople());                 
			}
				
		}
		//sc.close();
	}

	private static boolean createreservation() throws ParseException, IOException
	{
		Scanner sc = new Scanner(System.in);
		r = new ReservationEntity();
		System.out.println("Please enter the date:");
		r.setDate(sc.next());
		System.out.println("Please enter the start time:");
		r.setStart(sc.next());
		System.out.println("Please enter your name:");
		r.setName(sc.next());
		System.out.println("Please enter your Contact No.:");
		r.setContact(sc.next());
		System.out.println("Please enter the number of people:");
		r.setPeople(sc.next());
		sc.close();
		return (r.canReserve());
	}


	
}
				