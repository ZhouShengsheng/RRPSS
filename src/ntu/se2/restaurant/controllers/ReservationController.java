package ntu.se2.restaurant.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import ntu.se2.restaurant.models.Item;
import ntu.se2.restaurant.models.ReservationEntity;
import ntu.se2.restaurant.utils.Availability;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.ScannerUtil;

public class ReservationController {
	
	// Singleton.
	private static ReservationController instance = null;
	
	// Fields.
	private ArrayList<ReservationEntity> reservationList;
	private Scanner sc = ScannerUtil.scanner;
	private Availability seatMan;
	
	private ReservationController() {
		reservationList = new ArrayList<ReservationEntity>();
		seatMan = new Availability();
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
	
	/**
	 * Load reservation data.
	 * 
	 * @return
	 * @throws ParseException
	 */
	public void loadData(){
		try{
			Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.RESERVATION_PATH)));
			sc.nextLine();
			
			while(sc.hasNext()) {
				String temp[]=sc.nextLine().split(",");
				ReservationEntity r=new ReservationEntity();
				r.setDate(temp[0]);
				r.setName(temp[1]);
				r.setContact(temp[2]);
				r.setPeople(temp[3]);
				r.setStart(temp[4]);
				r.setTableNo(temp[5]);
				//isoccupied = Integer.parseInt(temp[6]);
				reservationList.add(r);
			}
			sc.close();
		}catch (FileNotFoundException err1) {
	
	          err1.printStackTrace();
        }
     }

}
