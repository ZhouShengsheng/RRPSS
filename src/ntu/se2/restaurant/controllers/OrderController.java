package ntu.se2.restaurant.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import ntu.se2.restaurant.models.Item;
import ntu.se2.restaurant.models.Order;
import ntu.se2.restaurant.models.Promo;
import ntu.se2.restaurant.models.Reservation;
import ntu.se2.restaurant.models.Staff;
import ntu.se2.restaurant.models.Table;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.DateUtil;
import ntu.se2.restaurant.utils.ScannerUtil;

public class OrderController {

	// Singleton.
	private static OrderController instance = null;
	
	// Fields.
	private ArrayList<Order> orderList;
	private Scanner sc = ScannerUtil.scanner;
	private TableController tableController;
	private PromotionController promotionController;
	private ItemController itemController;
	private StaffController staffController;
	private ReservationController reservationController;
	
	private OrderController() {
		orderList = new ArrayList<Order>();
		tableController = TableController.sharedInstance();
		promotionController = PromotionController.sharedInstance();
		itemController = ItemController.sharedInstance();
		staffController = StaffController.sharedInstance();
		reservationController = ReservationController.sharedInstance();
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
				order.setTable(tableController.getTableByNumber(tableNo));
				String staffId = current[3];
				order.setStaff(staffController.getStaffById(staffId));
				order.setStatus(current[4]);
				order.setBill(Double.parseDouble(current[5]));
				order.setBillWithTax(Double.parseDouble(current[6]));
				for(int i=7;i<current.length;i++)
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
			out.print("orderId, date, tableNo, staffId, status, bill, billWithTax, item1, item2, item3....");
			for (int i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				out.print("\n"+order.getId()+","+DateUtil.getDateTime(order.getDate())+","+order.getTable().getTableNo() +
						","+order.getStaff().getStaffID()+","+order.getStatus()+","+order.getBill()+","+order.getBillWithTax());
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
	 * Print given order.
	 * 
	 * @param order
	 */
	public void printOrder(Order order) {
		if (order != null) {
			System.out.println("   ID: " + order.getId());
			System.out.println(" Date: " + DateUtil.getDateTime(order.getDate()));
			System.out.println("Table: " + order.getTable().getTableNo());
			System.out.println("Staff: " + order.getStaff().getStaffID() + " " + order.getStaff().getName());
			System.out.println("Status: " + order.getStatus());
			
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
	
	/**
	 * Create or add item to order.
	 * 
	 */
	public void createOrAddItemToOrder() {
		boolean isNewOrder = false;
		String orderId;
		System.out.println("Enter order id:");
		orderId = sc.nextLine();

		Order order = getOrderById(orderId);
		if (order == null) { // New order.
			order = new Order();
			order.setId(orderId);
			order.setDate(new Date());
			System.out.println("This is a new order.");
			// Enter phone number to find reservation info.
			System.out.print("Enter reservation contact: ");
			String contact = sc.nextLine();
			Reservation reservation = reservationController.getByContact(contact);
			int tableNo;
			if (reservation != null) {
				reservation.setOrderId(orderId);
				tableNo = reservation.getTableNo();
				reservationController.saveData();
			} else {
				System.out.print("Reservation not found.");
				System.out.print("Enter tableNo: ");
				tableNo = sc.nextInt();
				sc.skip(System.lineSeparator());
			}
			Table table = tableController.getTableByNumber(tableNo);
			table.setStatus("occupied");
			order.setTable(table);
			order.setStaff(staffController.getCurrentStaff());
			order.setStatus("not_paid");
			isNewOrder = true;
		}

		// Take order.
		ArrayList<Item> itemList = order.getItemList();
		ArrayList<Promo> pList = order.getpList();
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
				
				order.sumBill();
			}
		} while (choice == 1 || choice == 2);
		
		order.setItemList(itemList);
		order.setpList(pList);
		if (isNewOrder) {
			orderList.add(order);
		}

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
        
        Order order = getOrderById(orderId);
        printOrder(order);
	}
	
	public void removeItemAtOrder() {
		String orderId;
		System.out.println("Enter the order ID:");
		orderId = sc.nextLine();
		
		Order order = getOrderById(orderId);
		if (order == null) {
			System.out.println("Order not found.");
			return ;
		}
		
		printOrder(order);
		
		ArrayList<Item> itemList = order.getItemList();
		ArrayList<Promo> pList = order.getpList();
		
		int choice;
		
		do {
			System.out.println("1. Remove an item or promotion");
			System.out.println("2. Exit");
			choice = sc.nextInt();
			sc.skip(System.lineSeparator());
			
			if(choice==1)
			{
				System.out.print("Enter the ID:");
				String id = sc.nextLine();
				if (id.startsWith("P")) {
					for(int i = 0; i < pList.size(); i++) {
						Promo promo = pList.get(i);
						if (promo.getPromoID().equals(id)) {
							pList.remove(i);
							break;
						}
					}
				} else {
					for(int i = 0; i < itemList.size(); i++) {
						Item item = itemList.get(i);
						if (item.getItemID().equals(id)) {
							itemList.remove(i);
							break;
						}
					}
				}
				
				order.sumBill();
			}
		} while (choice == 1);
		
		 if(saveData()) {
			 System.out.println("Successfully Deleted!");
		 } else {
			 System.out.println("Process Failed!");
		 }
	}
	
	/**
	 * Print invoice and end the order.
	 * 
	 */
	public void printInvoice() {
		String orderId;
        System.out.print("Enter the order ID:");
        orderId = sc.nextLine();
        
        Order order = getOrderById(orderId);
        if (order == null) {
			System.out.println("Order not found.");
			return ;
		}
        Staff staff = staffController.getStaffById(order.getId());
        Table table = order.getTable();
        
        order.setStatus("paid");
        table.setStatus("vacated");
        reservationController.removeByOrderId(orderId);
        
        // Save data.
        
        saveData();
        tableController.saveData();
        reservationController.saveData();
        
        
        // Print invoice.
        
        ArrayList<Item> itemList = order.getItemList();
		ArrayList<Promo> pList = order.getpList();
		
		System.out.println("================THE NTU RESTAURANT================= ");
		System.out.println("Staff ID:" + staff.getStaffID());
		System.out.println("Table NO: " + table.getTableNo());
		System.out.println("Datetime:" + DateUtil.getDateTime(order.getDate()));
		System.out.println("---------------------------------------------------");
		
		if(itemList.size()>0)
		{
			System.out.println("A La Carte Orders:");
			for(int i = 0; i<itemList.size();i++) {
				Item item = itemList.get(i);
				System.out.println(" "+item.getName()+ " "+ item.getPrice());
			}
		}
		if(pList.size()>0)
		{
			System.out.println("Promotion Orders:");
			for(int i = 0; i<pList.size();i++) {
				Promo promo = pList.get(i);
				System.out.println(" "+promo.getName()+ " "+ promo.getPrice());
			}
		}

		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("---------------------------------------------------------");
		System.out.println("SubTotal : " + df.format(( order.getBill() )));
		System.out.println("Taxes : " + df.format(((order.getBillWithTax() - order.getBill()))));
		System.out.println("--------------------------------------------------------- ");
		System.out.println("TOTAL : " + df.format((order.getBillWithTax())));
		System.out.println("============= Thank you! Please come again! =============");
	}
	
	public void printRevenue() {
		System.out.println("Revenue sorted by?");
		System.out.println("1. By date");
		System.out.println("2. By month");
		System.out.println("3. Back to Main Menu");
		int subChoice = sc.nextInt();
        sc.skip(System.lineSeparator());
        double totalRevenue = 0;
		double totalRevenueWithTax = 0;
		List<Order> datedOrderList = null;
		
        switch(subChoice) {
			case 1: {
				System.out.print("Enter date: ");
				String date = sc.nextLine();
				datedOrderList = orderList.stream()
						.filter(order -> date.equals(DateUtil.getDate(order.getDate())) && order.getStatus().equals("paid"))
						.collect(Collectors.toList());
				break;
			}
			case 2: {
				System.out.print("Enter month: ");
				int month = sc.nextInt();
				sc.skip(System.lineSeparator());
				
				datedOrderList = orderList.stream().filter(order -> {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(order.getDate());
					return month == calendar.get(Calendar.MONTH) + 1 && order.getStatus().equals("paid");
				}).collect(Collectors.toList());
				
				break;
			}
		}
        if (datedOrderList != null) {
        	for (int i = 0; i < datedOrderList.size(); i++) {
    			Order order = datedOrderList.get(i);
    			printOrder(order);
    			totalRevenue += order.getBill();
    			totalRevenueWithTax += order.getBillWithTax();
    		}
        	System.out.println(String.format("Total Revenue: $ %.2f", totalRevenue));
        	System.out.println(String.format("Total Revenue (Tax Included): $ %.2f", totalRevenueWithTax));
		}
	}
	
}
