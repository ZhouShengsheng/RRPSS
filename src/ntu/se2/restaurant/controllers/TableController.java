package ntu.se2.restaurant.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ntu.se2.restaurant.models.Reservation;
import ntu.se2.restaurant.models.Table;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.DateUtil;
import ntu.se2.restaurant.utils.ScannerUtil;

public class TableController {
	
	// Singleton.
	private static TableController instance = null;
	
	// Fields.
	private ArrayList<Table> tableList;
	private Scanner sc = ScannerUtil.scanner;
	
	private TableController() {
		tableList = new ArrayList<Table>();
		loadData();
	}
	
	public ArrayList<Table> getTableList() {
		return tableList;
	}
	
	/**
	 * Get shared singleton instance.
	 * 
	 * @return
	 */
	public static TableController sharedInstance() {
		if (instance == null) {
			instance = new TableController();
		}
		return instance;
	}
	
	/**
	 * Load data from file.
	 * 
	 */
	private void loadData() {
		try {
			Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.TABLE_PATH)));
			sc.nextLine();
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Table table = new Table(Integer.parseInt(current[0]),Integer.parseInt(current[1]),current[2]);
				tableList.add(table);
			}
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Save data to file.
	 * 
	 * String itemID, String name, char type, String description,String price,String discountedPrice
	 */
	public boolean saveData() {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DataFilePath.TABLE_PATH, false)))) 
		{
			out.println("itemID, name, type, desc, price, discounted price");
			for (int i = 0; i < tableList.size(); i++) {
				Table table = tableList.get(i);
				out.println(table.getTableNo()+","+table.getSize()+","+table.getStatus());
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
	 * Get table by table number.
	 * 
	 * @param number
	 * @return
	 */
	public Table getTableByNumber(int number) {
		for (int i = 0; i < tableList.size(); i++) {
			Table table = tableList.get(i);
			if (table.getTableNo() == number) {
				return table;
			}
		}
		return null;
	}
	
	/**
	 * Check table availability cur walk-in customers at current time.
	 */
	public void checkTableAvailability() {
		System.out.println("Enter people number: ");
		int peopleNum = sc.nextInt();
		sc.skip(System.lineSeparator());
		for (int i = 0; i < tableList.size(); i++) {
			Table table = tableList.get(i);
			if (table.getSize() >= peopleNum && 
				!table.getStatus().equals("occupied")) {
				
				ArrayList<Reservation> reservationList = ReservationController.sharedInstance().getReservationList();
				Date now = new Date();
				
				Runnable successRunnable = new Runnable() {
					@Override
					public void run() {
						System.out.println(String.format("Table %d is suitable for you.", table.getTableNo()));
					}
				};
				
				boolean foundInReservationList = false;
				for (int j = 0; j < reservationList.size(); j++) {
					Reservation reservation = reservationList.get(j);
					if (reservation.getTableNo() == table.getTableNo()) {
						foundInReservationList = true;
						Date today = DateUtil.formatStringToDate(DateUtil.getDate(now));
						Date nowTime = DateUtil.formatStringTimetoDate(DateUtil.getTime(now));
						Date todayStart = DateUtil.formatStringTimetoDate(DateUtil.getTime(reservation.getStart()));
						Date todayEnd = DateUtil.formatStringTimetoDate(DateUtil.getTime(reservation.getEnd()));
						if (table.getTableNo() == reservation.getTableNo()) {
							if (today.equals(reservation.getDate()) && 
									(nowTime.before(todayEnd) &&
											nowTime.after(todayStart))) {
								continue;
							}
						}
						
						successRunnable.run();
						return ;
					}
				}
				
				if (!foundInReservationList) {
					successRunnable.run();
					return ;
				}
			}
		}
	}

}
