package ntu.se2.restaurant.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import ntu.se2.restaurant.models.Item;
import ntu.se2.restaurant.models.ReservationEntity;
import ntu.se2.restaurant.utils.Availability;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.ScannerUtil;

public class OrderController {

	// Singleton.
	private static OrderController instance = null;
	
	// Fields.
	private ArrayList<Item> itemList;
	private Scanner sc = ScannerUtil.scanner;
	private Availability seatMan;
	
	private OrderController() {
		itemList = new ArrayList<Item>();
		seatMan = new Availability();
		loadData();
	}
	
	/**
	 * Get shared singleton instance.
	 * 
	 * @return
	 */
	public static OrderController sharedInstance() {
		if (instance == null) {
			instance = new OrderController();
		}
		return instance;
	}
	
	/**
	 * Load data from file.
	 * 
	 */
	private void loadData() {
		try {
			Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.ITEM_PATH)));
			sc.nextLine();
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Item item = new Item(current[0],current[1],current[2].charAt(0),current[3],current[4],current[5]);
				itemList.add(item);
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
	private boolean saveData() {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DataFilePath.ITEM_PATH, false)))) 
		{
			out.println("itemID, name, type, desc, price, discounted price");
			for (int i = 0; i < itemList.size(); i++) {
				Item item = itemList.get(i);
				out.println(item.getItemID()+","+item.getName()+","+item.getType()+","
						+item.getDescription()+","+item.getPrice()+","+item.getDiscountedPrice());
			}
			return true;
	    } 
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Create or add item to order.
	 * 
	 */
	public void createOrAddItem() {
		boolean result = false;
		 int tableNo;
		  System.out.println("Enter a table number:");
		  tableNo = sc.nextInt();
		  sc.skip("\n");
		  
		  System.out.println("seatMan.Occupied.size(): " + seatMan.Occupied.size());
		  for (int i = 0; i < seatMan.Occupied.size(); i++) {
			  ReservationEntity r = seatMan.Occupied.get(i);
			  System.out.println("name: " + r.getName());
		  }
		  
		  for(ReservationEntity r:seatMan.Occupied)
		  {
			  if(Integer.parseInt(r.getTableNo())==tableNo)
			  {
				  r.tables[tableNo].order.takeOrder();
				  result = true;
				  break;
			  }
		  }
		  if(result)
		    System.out.println("Successfully Added!");
		  else 
		    System.out.println("Process Failed!");
	}
	
	/**
	 * View an existing order.
	 */
	public void viewOrder() {
		int tableNo;
        boolean result = false;
        System.out.println("Enter the table number:");
        tableNo = sc.nextInt();
        sc.skip("\n");
        
        for(ReservationEntity r:seatMan.Occupied)
        {
      	  if(Integer.parseInt(r.getTableNo())==tableNo)
      	  {
      		  r.tables[tableNo].order.viewOrder();
      		  result = true;
      		  break;
      	  }
        }
        if(!result)
            System.out.println("No order placed!");
	}
	
}
