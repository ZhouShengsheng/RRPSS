package ntu.se2.restaurant.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ntu.se2.restaurant.models.Item;
import ntu.se2.restaurant.models.Order;
import ntu.se2.restaurant.models.Promo;
import ntu.se2.restaurant.models.ReservationEntity;
import ntu.se2.restaurant.models.Staff;
import ntu.se2.restaurant.models.Table;
import ntu.se2.restaurant.utils.Availability;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.DateUtil;
import ntu.se2.restaurant.utils.ScannerUtil;

public class OrderController {

	// Singleton.
	private static OrderController instance = null;
	
	// Fields.
	private ArrayList<Order> orderList;
	private Scanner sc = ScannerUtil.scanner;
	private Availability seatMan;
	private Scanner scanner;
	private TableController tableController;
	private PromotionController promotionController;
	private ItemController itemController;
	
	private OrderController() {
		orderList = new ArrayList<Order>();
		seatMan = new Availability();
		scanner = ScannerUtil.scanner;
		tableController = TableController.sharedInstance();
		promotionController = PromotionController.sharedInstance();
		itemController = ItemController.sharedInstance();
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
	@SuppressWarnings("deprecation")
	private void loadData() {
		try {
			Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.ORDER_PATH)));
			sc.nextLine();
			while(sc.hasNext()) {
				String current[] = sc.nextLine().split(",");
				
				Order order = new Order();
				ArrayList<Item> itemList = new ArrayList<Item>();
				ArrayList<Promo> pList = new ArrayList<Promo>();
				
				order.setId(current[0]);
				try {
					order.setDate(DateUtil.stringToDateTime(current[1]));
				} catch (Exception e) {
					order.setDate(new Date());
				}
				int tableNo = Integer.parseInt(current[2]);
				order.setTable(TableController.sharedInstance().getTableByNumber(tableNo));
				for(int i=3;i<current.length;i++)
				{
					String id = current[i];
					if (id.startsWith("P")) {
						Promo promo = PromotionController.sharedInstance().getPromoById(id);
						if (promo != null) {
							pList.add(promo);
						}
					} else {
						Item item = ItemController.sharedInstance().getItemById(id);
						if (item != null) {
							itemList.add(item);
						}
					}
				}
				order.setItemList(itemList);
				order.setpList(pList);
				
				orderList.add(order);
			}
			sc.close();
		    
		} catch (FileNotFoundException e)
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
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DataFilePath.ORDER_PATH, false)))) 
		{
			out.print("orderId, date, tableNo, item1, item2, item3....");
			for (int i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				out.print("\n"+order.getId()+","+DateUtil.getDateTime(order.getDate())+","+order.getTable().getTableNo());
				ArrayList<Item> itemList = order.getItemList();
				ArrayList<Promo> pList = order.getpList();
				for(int j=0; j<itemList.size(); j++)
				{
					Item item = itemList.get(j);
				    out.print(","+item.getItemID());
				}
				for(int j=0; j<pList.size(); j++)
				{
					Promo promo = pList.get(j);
				    out.print(","+promo.getPromoID());
				}
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
	
	public Order getOrderById(String orderId) {
		for (int i = 0; i < orderList.size(); i++) {
			Order order = orderList.get(i);
			if (order.getId().equals(orderId)) {
				return order;
			}
		}
		return null;
	}
	
	/**
	 * Create or add item to order.
	 * 
	 */
	public void createOrAddItemToOrder() {
		boolean result = false;
		String orderId;
		System.out.println("Enter order id:");
		orderId = sc.nextLine();

		// System.out.println("seatMan.Occupied.size(): " +
		// seatMan.Occupied.size());
		// for (int i = 0; i < seatMan.Occupied.size(); i++) {
		// ReservationEntity r = seatMan.Occupied.get(i);
		// System.out.println("name: " + r.getName());
		// }

		Order order = getOrderById(orderId);
		int tableNo;
		if (order == null) { // New order.
			order = new Order();
			order.setId(orderId);
			order.setDate(new Date());
			System.out.println("This is a new order.");
			System.out.print("Enter tableNo: ");
			tableNo = scanner.nextInt();
			scanner.skip(System.lineSeparator());
			Table table = tableController.getTableByNumber(tableNo);
			table.setStatus("occupied");
			order.setTable(table);
		}

		// Take order.
		ArrayList<Item> itemList = new ArrayList<Item>();
		ArrayList<Promo> pList = new ArrayList<Promo>();
		int choice;
		promotionController.printPromotionList();
		itemController.printItemList();
		do {
			System.out.println("-----Take Order-----");
			System.out.println("1.Print Menu ");
			System.out.println("2.Select Item ");
			System.out.println("3.Exit ");
			System.out.println("Enter your choice: ");
			choice = sc.nextInt();
			sc.skip(System.lineSeparator());
			if (choice == 1) {
				promotionController.printPromotionList();
				itemController.printItemList();
			} else if (choice == 2) {
				System.out.println("Enter item id to select item: ");
				String id = sc.nextLine();
				
				if (id.startsWith("P")) {
					pList.add(promotionController.getPromoById(id));
				} else {
					itemList.add(itemController.getItemById(id));
				}
			}
		} while (choice == 1 || choice == 2);
		
		order.setItemList(itemList);
		order.setpList(pList);
		orderList.add(order);

		if (saveData()) {
			tableController.saveData();
			System.out.println("Successfully Added!");
		} else {
			System.out.println("Process Failed!");
		}
	}
	
	/**
	 * View an existing order.
	 */
	public void viewOrder() {
		String orderId;
        System.out.println("Enter the order Id:");
        orderId = sc.nextLine();
        
//        System.out.println("orderList.size(): " + orderList.size());
//        for (int i = 0; i < orderList.size(); i++) {
//			Order order = orderList.get(i);
//			System.out.println("order.id: " + order.getId());
//		}
        
        Order order = getOrderById(orderId);
        if (order != null) {
			System.out.println("   ID: " + order.getId());
			System.out.println(" Date: " + DateUtil.getDateTime(order.getDate()));
			System.out.println("Table: " + order.getTable().getTableNo());
			
			ArrayList<Item> itemList = order.getItemList();
			int size = itemList.size();
			if (size > 0) {
				System.out.println("Items:");
			}
			for(int i = 0; i < size; i++) {
				Item item = itemList.get(i);
				System.out.println(String.format("\t%10s%20s", item.getItemID(), item.getName()));
			}
			
			ArrayList<Promo> pList = order.getpList();
			size = pList.size();
			if (size > 0) {
				System.out.println("Promotions:");
			}
			for(int i = 0; i < size; i++) {
				Promo promo = pList.get(i);
				System.out.println(String.format("\t%10s%20s", promo.getPromoID(), promo.getName()));
			}
		}
	}
	
//	public void takeOrder(Promo p)
//	{
//		pList.add(p);
//	}
	
//	public void viewOrder()
//	{
//		for (int i = 0; i < itemList.size(); i++) {
//			Item item = itemList.get(i);
//			 System.out.println("Item: "+ item.getName()+" $"+item.getPrice());	 
//		}
//		for (int i = 0; i < pList.size(); i++) {
//			Promo prm = pList.get(i);
//			System.out.println("Promotion: "+ prm.getName()+" $"+prm.getPrice());
//		}
//	}
	
//	public double calcBill()
//	{
//		for (int i = 0; i < itemList.size(); i++) {
//			Item item = itemList.get(i);
//			bill += Double.parseDouble(item.getPrice());
//		}
//		for (int i = 0; i < pList.size(); i++) 
//		{
//			Promo prm = pList.get(i);
//			bill += Double.parseDouble(prm.calculatePrice());
//		}
//		
//		System.out.println("Order Bill: "+bill);
//		return bill;
//	}
//	
//	public void removeOrder()
//	{
//		Scanner sc = ScannerUtil.scanner;
//		String input;
//		int choice;
//		System.out.println("1. Remove an item");
//		System.out.println("2. Remove a promotion");
//		System.out.println("3.Exit");
//		choice = sc.nextInt();
//		
//		if(choice==1)
//		{
//			System.out.println("Enter the ID of the item you want to remove");
//			input = sc.nextLine();
//			for (int i = 0; i < itemList.size(); i++) {
//				Item itm = itemList.get(i);
//				if(itm.getItemID().equals(input))
//					itemList.remove(i);
//			}
//		}
//		else if(choice == 2)
//		{
//			System.out.println("Enter the ID of the promotion you want to remove");
//			input = sc.nextLine();
//			for (int i = 0; i <pList.size(); i++) {
//				Promo prm = pList.get(i);
//				if(prm.getPromoID().equals(input))
//					pList.remove(i);
//			}
//			
//		}
//	}
//	
//	public void printInvoice(Staff staff, ReservationEntity r) throws ParseException
//	{
//		DateFormat DATE_FORMAT=new DateFormat();
//		r.setEnd();
//		Date d1 = r.getEnd();
//		
//		DecimalFormat df = new DecimalFormat("#.##");
//		
//		System.out.println("================THE NTU RESTAURANT================= ");
//		System.out.println("Staff Name :" +staff.getName());
//		System.out.println("Table ID: "+r.getTableNo());
//		System.out.println("Date(DD/MM/YYYY):" +DATE_FORMAT.getDate(d1));
//		System.out.println("---------------------------------------------------");
//		
//		if(itemList.size()>0)
//		{
//			System.out.println("A La Carte Orders:");
//			for(int i = 0; i<itemList.size();i++)
//				System.out.println(" "+(itemList.get(i)).getName()+ " "+ (itemList.get(i)).getPrice());
//		}
//		calcBill();
//		totalBill = bill*1.07;
//		System.out.println("---------------------------------------------------------");
//		System.out.println("SubTotal : " + df.format(( bill )));
//		System.out.println("Taxes : " + df.format(( bill * 0.07)));
//		System.out.println("--------------------------------------------------------- ");
//		System.out.println("TOTAL : " + df.format((totalBill)));
//		System.out.println("============= Thank you! Please come again! =============");
//		
//		// save to order.txt
//		
//		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("order.txt",true))))
//    	{
//    		out.print("\n"+DATE_FORMAT.getDate(d1)+","+bill);
//    	}
//    	catch(IOException ex)
//    	{
//    		ex.printStackTrace();
//    	}
//	}
	
}
