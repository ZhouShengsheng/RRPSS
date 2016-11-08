package ntu.se2.restaurant.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ntu.se2.restaurant.controllers.ItemController;
import ntu.se2.restaurant.controllers.Menu;
import ntu.se2.restaurant.utils.DateUtil;
import ntu.se2.restaurant.utils.ScannerUtil;

public class Order
{
	Menu m;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Promo> pList = new ArrayList<Promo>();
	private String id;
	private Date date;
	private Table table;
	private Staff staff;
	private String status;
	private double bill = 0;		// bill before tax
	private double billWithTax = 0;	// bill after tax
	
	private static final double TAX_RATE = 1.07;
	
	public Order()
	{
		
	}
	
	public Menu getM() {
		return m;
	}

	public void setM(Menu m) {
		this.m = m;
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}

	public ArrayList<Promo> getpList() {
		return pList;
	}

	public void setpList(ArrayList<Promo> pList) {
		this.pList = pList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public double getBill() {
		return bill;
	}

	public void setBill(double bill) {
		this.bill = bill;
	}

	public double getBillWithTax() {
		return billWithTax;
	}

	public void setBillWithTax(double totalBill) {
		this.billWithTax = totalBill;
	}
	
	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Some total bill.
	 */
	public void sumBill() {
		bill = 0;
		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			bill += Double.parseDouble(item.getPrice());
		}
		for (int i = 0; i < pList.size(); i++) 
		{
			Promo prm = pList.get(i);
			bill += prm.getPrice();
		}
		billWithTax = bill * TAX_RATE;
	}

	// TODO: To be removed.
	public void takeOrder()
	{
		m = new Menu();
		int choice;
		m.printMenu();
		Scanner sc = ScannerUtil.scanner;
		do
		{
			System.out.println("-----Take Order-----");
			System.out.println("1.Print Menu ");
			System.out.println("2.Select Item ");
			System.out.println("3.Exit ");
			System.out.println("Enter your choice: ");
	        choice = sc.nextInt();
	        sc.skip(System.lineSeparator());
			if(choice ==1)
				m.printMenu();
			else if(choice ==2)
			{
				System.out.println("Enter item id to select item: ");
				String itemId = sc.nextLine();
				ItemController ic = ItemController.sharedInstance();
				Object object = ic.getItemById(itemId);
				if(object!=null)
				{
					if(object instanceof Item)
						itemList.add((Item)object);
					if(object instanceof Promo)
						pList.add((Promo)object);
				}
			}
		}while(choice==1 || choice==2);	
	}
	
	// TODO: To be removed.
	public void takeOrder(Promo p)
	{
		pList.add(p);
	}
	
	// TODO: To be removed.
	public void viewOrder()
	{
		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			 System.out.println("Item: "+ item.getName()+" $"+item.getPrice());	 
		}
		for (int i = 0; i < pList.size(); i++) {
			Promo prm = pList.get(i);
			System.out.println("Promotion: "+ prm.getName()+" $"+prm.getPrice());
		}
	}
	
	public double calcBill()
	{
		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			bill += Double.parseDouble(item.getPrice());
		}
		for (int i = 0; i < pList.size(); i++) 
		{
			Promo prm = pList.get(i);
			bill += Double.parseDouble(prm.calculatePrice());
		}
		
		return bill;
	}
	
	// TODO: To be removed.
	public void removeOrder()
	{
		Scanner sc = ScannerUtil.scanner;
		String input;
		int choice;
		System.out.println("1. Remove an item");
		System.out.println("2. Remove a promotion");
		System.out.println("3.Exit");
		choice = sc.nextInt();
		
		if(choice==1)
		{
			System.out.println("Enter the ID of the item you want to remove");
			input = sc.nextLine();
			for (int i = 0; i < itemList.size(); i++) {
				Item itm = itemList.get(i);
				if(itm.getItemID().equals(input))
					itemList.remove(i);
			}
		}
		else if(choice == 2)
		{
			System.out.println("Enter the ID of the promotion you want to remove");
			input = sc.nextLine();
			for (int i = 0; i <pList.size(); i++) {
				Promo prm = pList.get(i);
				if(prm.getPromoID().equals(input))
					pList.remove(i);
			}
			
		}
	}
	
	// TODO: To be removed.
	public void printInvoice(Staff staff, Reservation r) throws ParseException
	{
		DateUtil DATE_FORMAT=new DateUtil();
		r.setEnd();
		Date d1 = r.getEnd();
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.println("================THE NTU RESTAURANT================= ");
		System.out.println("Staff ID:" +staff.getStaffID());
		System.out.println("Table ID: "+r.getTableNo());
		System.out.println("Date(DD/MM/YYYY):" +DATE_FORMAT.getDate(d1));
		System.out.println("---------------------------------------------------");
		
		if(itemList.size()>0)
		{
			System.out.println("A La Carte Orders:");
			for(int i = 0; i<itemList.size();i++)
				System.out.println(" "+(itemList.get(i)).getName()+ " "+ (itemList.get(i)).getPrice());
		}
		calcBill();
		billWithTax = bill*1.07;
		System.out.println("---------------------------------------------------------");
		System.out.println("SubTotal : " + df.format(( bill )));
		System.out.println("Taxes : " + df.format(( bill * 0.07)));
		System.out.println("--------------------------------------------------------- ");
		System.out.println("TOTAL : " + df.format((billWithTax)));
		System.out.println("============= Thank you! Please come again! =============");
		
		// save to order.txt
		
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("order.txt",true))))
    	{
    		out.print("\n"+DATE_FORMAT.getDate(d1)+","+bill);
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
	}
	
	
}
