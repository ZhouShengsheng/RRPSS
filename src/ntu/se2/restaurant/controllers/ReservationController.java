package ntu.se2.restaurant.controllers;

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
import java.util.Scanner;

import ntu.se2.restaurant.models.Reservation;
import ntu.se2.restaurant.models.Table;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.DateUtil;
import ntu.se2.restaurant.utils.ScannerUtil;

public class ReservationController {
	
	// Singleton.
	private static ReservationController instance = null;
	
	// Fields.
	private ArrayList<Reservation> reservationList;

	private Scanner sc = ScannerUtil.scanner;
	private TableController tableController;
	
	private ReservationController() {
		reservationList = new ArrayList<Reservation>();
		tableController = TableController.sharedInstance();
		loadData();
	}
	
	/**
	 * Get shared singleton instance.
	 * 
	 * @return
	 */
	public static ReservationController sharedInstance() {
		if (instance == null) {
			instance = new ReservationController();
		}
		return instance;
	}
	
	public ArrayList<Reservation> getReservationList() {
		return reservationList;
	}

	public void setReservationList(ArrayList<Reservation> reservationList) {
		this.reservationList = reservationList;
	}
	
	/**
	 * Load data from file.
	 * 
	 * @return
	 */
	public void loadData() {
		try{
			Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.RESERVATION_PATH)));
			sc.nextLine();
			
			while(sc.hasNext()) {
				String current[] = sc.nextLine().split(",");
				
				Reservation r=new Reservation();
				r.setDate(current[0]);
				r.setName(current[1]);
				r.setContact(current[2]);
				r.setPeople(current[3]);
				r.setStart(current[4]);
				r.setTableNo(Integer.parseInt(current[5]));
				r.setOrderId(current[6]);
				
				reservationList.add(r);
			}
			sc.close();
		}catch (FileNotFoundException e) {
	
	          e.printStackTrace();
        }
     }
	
	/**
	 * Save data to file.
	 * 
	 */
	public boolean saveData() {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DataFilePath.RESERVATION_PATH, false)))) 
		{
			out.println("date,name,contact,peoplenumber,starttime,tablenumber,orderId");
			for (int i = 0; i < reservationList.size(); i++) {
				Reservation r = reservationList.get(i);
				out.println(r.getDate()+","+r.getName()+","+r.getContact()+","
						+r.getPeople()+","+DateUtil.getTime(r.getStart())+","+r.getTableNo() + ","+r.getOrderId());
			}
			out.close();
			return true;
	    } 
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Get reservation by contact number.
	 * 
	 * @param contact
	 * @return
	 */
	public Reservation getByContact(String contact) {
		for (int i = 0; i < reservationList.size(); i++) {
			Reservation r = reservationList.get(i);
			if (r.getContact().equals(contact)) {
				return r;
			}
		}
		return null;
	}
	
	/**
	 * Remove reservation by order id.
	 * 
	 * @param orderId
	 */
	public void removeByOrderId(String orderId) {
		for (int i = 0; i < reservationList.size(); i++) {
			Reservation r = reservationList.get(i);
			if (r.getOrderId().equals(orderId)) {
				reservationList.remove(i);
				return ;
			}
		}
	}
	
	/**
	 * Check whether the reservation can be made or not.
	 * 
	 * @param r
	 * @return
	 */
	private boolean checkCanReserve(Reservation r) {
		if (r.getStart().before(DateUtil.formatStringTimetoDate("11:00")) ||
				r.getStart().after(DateUtil.formatStringTimetoDate("22:00")) ||
				( r.getStart().after(DateUtil.formatStringTimetoDate("15:00")) &&
				  r.getStart().before(DateUtil.formatStringTimetoDate("18:00"))
						)) {
			return false;
		}
		ArrayList<Table> tableList = tableController.getTableList();
		for (int i = 0; i < tableList.size(); i++) {
			Table table = tableList.get(i);
			if (table.getSize() >= Integer.parseInt(r.getPeople())) {	// Size condition.
				for (int j = 0; j < reservationList.size(); j++) {
					Reservation reservation = reservationList.get(j);
					if (table.getTableNo() == reservation.getTableNo()) {
						if (r.getDate().equals(reservation.getDate()) && 
								((r.getStart().before(DateUtil.formatStringTimetoDate("15:00")) &&
										reservation.getStart().before(DateUtil.formatStringTimetoDate("15:00")))
								||
								(r.getStart().after(DateUtil.formatStringTimetoDate("18:00")) &&
										reservation.getStart().after(DateUtil.formatStringTimetoDate("18:00"))))
								
								) {
							continue;
						}
						
						r.setTableNo(table.getTableNo());
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Create a new reservation.
	 * 
	 */
	public void createReservation() {
		Reservation r = new Reservation();
		
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
		
		if (checkCanReserve(r)) {
			r.setOrderId("0");
			reservationList.add(r);
			saveData();
			System.out.println("Reservation made successfully.");
		} else {
			System.out.println("Cannot make this reservation.");
		}
	}
	
	/**
	 * Check reservation of a contact.
	 * 
	 */
	public void checkReservation()
	{
		String contact;
		System.out.print("Enter contact: ");
		contact = sc.nextLine();
		
		Reservation reservation = getByContact(contact);
		
		System.out.println("Date: " + reservation.getDate());
		System.out.println("Name: " + reservation.getName());
		System.out.println("Contact: " + reservation.getContact());
		System.out.println("People Number: " + reservation.getPeople());
		System.out.println("Start Time: " + DateUtil.getTime(reservation.getStart()));
		System.out.println("Table No: " + reservation.getTableNo());
		System.out.println("Order Id: " + reservation.getOrderId());
	}
	
	/**
	 * Delete reservation of a contact.
	 * 
	 */
	public void deleteReservation()
	{
		String contact;
		System.out.print("Enter contact: ");
		contact = sc.nextLine();
		
		Reservation reservation = getByContact(contact);
		if (reservation == null) {
			System.out.println("Reservation not found.");
		} else {
			reservationList.remove(reservation);
			saveData();
			System.out.println("Reservation removed.");
		}
	}
//
//	private static boolean createreservation() throws ParseException, IOException
//	{
//		Scanner sc = new Scanner(System.in);
//		r = new Reservation();
//		System.out.println("Please enter the date:");
//		r.setDate(sc.next());
//		System.out.println("Please enter the start time:");
//		r.setStart(sc.next());
//		System.out.println("Please enter your name:");
//		r.setName(sc.next());
//		System.out.println("Please enter your Contact No.:");
//		r.setContact(sc.next());
//		System.out.println("Please enter the number of people:");
//		r.setPeople(sc.next());
//		sc.close();
//		return (r.canReserve());
//	}

}
