package ntu.se2.restaurant.controllers;

import java.io.*;
import java.text.ParseException;
import java.util.Scanner;

import ntu.se2.restaurant.models.Item;
import ntu.se2.restaurant.models.Promo;
import ntu.se2.restaurant.models.Staff;

import java.util.ArrayList;

public class SystemManagement 
{
    boolean exist = false;
	private Item itm;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Promo> pList = new ArrayList<Promo>();
	Staff s=new Staff();
	Menu m=new Menu();
	static int itemID=0;
	
	public SystemManagement()
	{
		try {
			getItems();
			getPromotion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// TODO: To be removed.
	public void getItems()throws IOException // reading from the file and updating it into the array list
	{
		try{
			Scanner sc=new Scanner(new BufferedReader(new FileReader("data/item.txt")));
			sc.nextLine();
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Item item = new Item(current[0],current[1],current[2].charAt(0),current[3],current[4],current[5]);
				itemList.add(item);
			}
			sc.close();
		}
		catch(FileNotFoundException err1)
		{
			err1.printStackTrace();
		}
	}
	
	// TODO: To be removed.
	public void getPromotion()throws IOException
	{
		try
		{
			Scanner sc=new Scanner(new BufferedReader(new FileReader("data/promotion.txt")));
			sc.nextLine();
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Promo p = new Promo();
				ArrayList<Item> tempPList = new ArrayList<Item>();
				p.setPromoID(current[0]);
				p.setName(current[1]);
				p.setPrice(Double.parseDouble(current[2]));
				for(int i=3;i<current.length;i++)
				{
					for (int j = 0; j < itemList.size(); j++)
					{
						Item check = itemList.get(j);
						if(current[i].equals(check.getItemID()))
						{
							tempPList.add(check);
						}
					}
				}
				p.setItemList(tempPList);
				pList.add(p);
			}
			sc.close();
		    
		} catch (FileNotFoundException ex1)
		{
			  ex1.printStackTrace();
		}
   }
	
	// TODO: To be removed.
	public Item getItem(String obj_ID)
	{
		Item obj = null;
		for(Item i:itemList)
			if(i.getItemID()!=null && i.getItemID().equals(obj_ID))
				obj = i;
		return obj;
	}
	
	// TODO: To be removed.
	public boolean createItem(Scanner sc)
	{
		boolean result = false;
		char check;
		itm = new Item();
		System.out.println("Please enter the name of the item:");
	    itm.setName(sc.next());
	    itemID++;
		do{
			System.out.println("Please enter the type of item:");
			System.out.println("a. Appetisers");
			System.out.println("m. Main course");
			System.out.println("d. Desserts");
			System.out.println("b. Beverages");
			check = sc.next().charAt(0);
		  }while(check!='a'&&check!='m'&&check!='d'&&check!='b');
		itm.setType(check);
		System.out.println("Please enter price of item");
		itm.setPrice(sc.next());
		System.out.println("Please enter description of the item");
		itm.setDescription(sc.next());
		System.out.println("Please enter the discounted price of the item");
		itm.setDiscountedPrice(sc.next());
		itm.setItemID(Integer.toString(itemID));
	    for(Item i:itemList)
	    {
	    	if(i.getName().equals(itm.getName()))
	    		exist = true;
	    }
	    
	    if(!exist)
	    {
	   
	    	try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/item.txt",true))))
	    	{
	    		out.println(itm.getItemID()+","+itm.getType()+","+itm.getName()+","+itm.getPrice()+","+itm.getDescription()+","+itm.getDiscountedPrice());	    				
	            result = true;    	
	    	}
	    	catch(IOException ex)
	    	{
	    		ex.printStackTrace();
	    	}
	    }
		return result;
	}
	
	// TODO: To be removed.
	public void updateItem(Scanner sc)
	{
		int choice;
		String itemID;
		System.out.print("ItemID"+ "       " + "Item Name" + "       " + "Item Price" + "       " + "Item Description" + "                  " + "Item Type" + "       " + "Discounted Price");
		for (int i = 0; i < itemList.size(); i++) {

			Item itm=itemList.get(i);
        	System.out.print("\n"+itm.getItemID() + "            "+ itm.getName() + "            "+ itm.getPrice() + "            "+ itm.getDescription()+ "            "+ itm.getType() + "            "+ itm.getDiscountedPrice());

		}
		System.out.println();
		System.out.println("Please enter itemID of item to be updated");
		itemID = sc.next();
		itm = getItem(itemID);
		System.out.println("1. Change Name");
		System.out.println("2. Change Price");
		System.out.println("3. Change Description");
		System.out.println("4. Change Type");
		System.out.println("5. Change Discounted Price");
		choice = sc.nextInt();
		switch(choice){
			case 1: System.out.print("Enter new name of item:");
				    itm.setName(sc.nextLine());
				    writeArrayToItemFile();
				    break;
			case 2: System.out.print("Enter new price of item:");
				    itm.setPrice(sc.nextLine());
				    writeArrayToItemFile();
				    break;
			case 3: System.out.print("Enter new description of item:");
				    itm.setDescription(sc.nextLine());
				    writeArrayToItemFile();
				    break;
			case 4: char check;
				    do
				    {
				      System.out.println("Enter new type of item");
					  System.out.println("Please enter the type of item:");
				      System.out.println("a. Appetisers");
					  System.out.println("m. Main course");
					  System.out.println("d. Desserts");
					  System.out.println("b. Beverages");
				      check = sc.next().charAt(0);
					}while(check!='a'&&check!='m'&&check!='d'&&check!='b');
				    itm.setType(check);
				    writeArrayToItemFile();
				    break;
			case 5: System.out.println("Enter new discounted price of item");
				    itm.setDiscountedPrice(sc.nextLine());
				    writeArrayToItemFile();
				    break;
			}
		}
	
	// TODO: To be removed.
	public void deleteItem(Scanner sc) {
		String itemID;
		itm = new Item();
		System.out.print("ItemID"+ "       " + "Item Name" + "       " + "Item Price" + "       " + "Item Description" + "                  " + 
				"Item Type" + "       " + "Discounted Price");
		
		for (int i = 0; i < itemList.size(); i++) {

			Item item=itemList.get(i);
			System.out.print("\n"+item.getItemID() + "            "+ item.getName() + "            "+ item.getPrice() + "            "+ item.getDescription()
					+ "            "+ item.getType() + "            "+ item.getDiscountedPrice());
		}
		System.out.println("Please enter itemID of item to be deleted");
		itemID = sc.nextLine();
		System.out.println("itemList size: "+ itemList.size());
		System.out.println("itemID: "+ itemID);
		itm = m.selectItem(itemID);
		itemList.remove(itm);
		System.out.println("itemList size: "+ itemList.size());
		writeArrayToItemFile();
	}
	
	// TODO: To be removed.
	private void writeArrayToItemFile()
	{	
		try 
		{
			String path = "data/item.txt";
			// Use relative path for Unix systems
			File f = new File(path);

			f.getParentFile().mkdirs(); 
			if (!f.exists()) 
				f.createNewFile();
			Scanner sc = new Scanner(f);
			sc.nextLine();
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write("itemID, type, name, price, desc, discounted price");
			out.newLine();
			for (int i = 0; i < itemList.size(); i++) {

				Item item=itemList.get(i);
				out.write("\n"+item.getItemID()+","+item.getType()+","+item.getName()+","+item.getPrice()+","+item.getDescription()+","+item.getDiscountedPrice());
			}
			System.out.println("Update Successful!");
			out.close();
			sc.close();
		}
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	 }
	
	public boolean createPromo(Scanner sc)
	{
	    boolean result = false;
	    
	    // Promotion info.
		String promoName, promoId;
		double promoPrice;
		ArrayList<Item> newItemList = new ArrayList<>();
		
		// Input basic info.
		System.out.print("Promotion ID: ");
		promoId = sc.next();
		System.out.print("Promotion name: ");
		promoName = sc.next();
		System.out.print("Promotion price: ");
		promoPrice = sc.nextDouble();
		
		// Display all items for choosing.
		System.out.print("ItemID\t\tItem Name\tItem Price\tItem Description\tItem Type\tDiscounted Price");
	    for (int j = 0; j < itemList.size(); j++)
		{
			Item item = itemList.get(j);
			System.out.print("\n"+item.getItemID() + "\t\t" + item.getName() + "\t\t" + item.getPrice() + "\t\t" 
					+ item.getDescription() + "\t\t" + item.getType() + "\t\t" + item.getDiscountedPrice());
		}
	    System.out.println();
		
		// Add items to current promotion.
		String itemId;
		do{
			System.out.print("Please enter itemID of item to add to this promotion (press 0 to exit): ");
			itemId = sc.next();
			if(!itemId.equals("0"))
			{
				boolean added = false;
				for (int i = 0; i < itemList.size(); i++) {
					Item item = itemList.get(i);
					if (itemId.equals(item.getItemID())) {
						added = true;
						newItemList.add(item);
						break;
					}
				}
				if (added) {
					System.out.println(String.format("Add item %s to current promotion package.", itemId));
				} else {
					System.out.println(String.format("Item %s not found.", itemId));
				}
			}
		  }while(!itemId.equals("0"));

		//return savePromo(promoId, promoName, promoPrice, newItemList);
		return saveAllPromos();
	}
	
	private boolean saveAllPromos()
	{
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/promotion.txt", false)))) 
		{
			out.print("promoID, promoName, item1, item2, item3, item4....");
			for (int i = 0; i < pList.size(); i++) {
				Promo promo = pList.get(i);
				out.print("\n"+ promo.getPromoID()+","+promo.getName()+","+String.valueOf(promo.getPrice()));
				ArrayList<Item> itemList = promo.getItemList();
				for(int j=0; j<itemList.size(); j++)
				{
					Item item = itemList.get(j);
				    out.print(","+item.getItemID());
				}
			}
            
			return true;
	    } 
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public Promo getPromo(String promo_ID)
	{
		Promo obj = null;
		for(Promo p:pList)
			if(p.getPromoID()!=null && p.getPromoID().equals(promo_ID))
				obj = p;
		return obj;
	}
	
//	private boolean savePromo(String promoId, String promoName, Double promoPrice, ArrayList<Item> itemList)
//	{
//		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("promotion.txt", true)))) 
//		{
//            out.print("\n"+ "P"+ promoId+","+promoName+","+String.valueOf(promoPrice));
//			for(int j=0; j<itemList.size(); j++)
//			{
//				Item item = itemList.get(j);
//			    out.print(","+item.getItemID());
//			}
//			return true;
//	    } 
//		catch (IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		return false;
//	}
	
	/**
	 * print all promos.
	 * 
	 */
	private void printAllPromo()
	{
		System.out.println("--------Promotions--------");
		for (int i = 0; i < pList.size(); i++) {
		    Promo prm = pList.get(i);
		    System.out.println("   ID: " + prm.getPromoID());
		    System.out.println(" Name: " + prm.getName());
		    System.out.println("Price: " + prm.getPrice());
		    System.out.println("Items: ");
		    
		    //System.out.print("ItemID"+ "       " + "Item Name" + "       " + "Item Price" + "       " + "Item Description" + "                  " + "Item Type" + "       " + "Discounted Price");
		    System.out.print("ItemID\t\tItem Name\tItem Price\tItem Description\tItem Type\tDiscounted Price");
		    for (int j = 0; j < prm.getItemList().size(); j++)
			{
				Item item = prm.getItemList().get(j);
				System.out.print("\n"+item.getItemID() + "\t\t" + item.getName() + "\t\t" + item.getPrice() + "\t\t" 
						+ item.getDescription() + "\t\t" + item.getType() + "\t\t" + item.getDiscountedPrice());
			}
			System.out.println();
			System.out.println("-----------------");
		}
		System.out.println();
	}
	
	public void updatePromo(Scanner sc)
	{
		printAllPromo();
		
		String promoID;
		Promo promo;
		
		System.out.println("Enter the promo ID of the promotion you want to update:");
		promoID = sc.next();
		promo = getPromo(promoID);
		System.out.println("promo: " + promo);
		ArrayList<Item> promoItemList = promo.getItemList();
		
		int select;
		do
		{
			System.out.println("1.Add Item");
			System.out.println("2.Remove Item");
			select = sc.nextInt();
			if(select == 1){	// Add item.
				System.out.println("Enter the ID of the item to be inserted: ");
				String itemId = sc.next();
				boolean added = false;
				for (int i = 0; i < itemList.size(); i++) {
					Item item = itemList.get(i);
					if (itemId.equals(item.getItemID())) {
						added = true;
						promoItemList.add(item);
						break;
					}
				}
				if (added) {
					System.out.println(String.format("Add item %s to current promotion package.", itemId));
				} else {
					System.out.println(String.format("Item %s not found.", itemId));
				}
			}
			if(select==2){	// Remove item.
				System.out.println("Enter the ID of the item to be deleted: ");
				String itemId = sc.next();
				boolean removed = false;
				for (int i = 0; i < promoItemList.size(); i++) {
					Item item = promoItemList.get(i);
					if (itemId.equals(item.getItemID())) {
						removed = true;
						promoItemList.remove(item);
						break;
					}
				}
				if (removed) {
					System.out.println(String.format("Removed item %s from current promotion package.", itemId));
				} else {
					System.out.println(String.format("Item %s not found in current promotion package.", itemId));
				}
			}
		}while(select ==1 || select==2);
	
		saveAllPromos();
  }

	public boolean deletePromo()
	{
		boolean result = false;
		
		return result;
	}
	
	public void printRevenue(int choice, Scanner sc)
	{
		try{
			double total = 0;
			FileReader     frStream = new FileReader("data/order.txt");
			BufferedReader brStream = new BufferedReader(frStream);
			String inputLine ;
			ArrayList<String> history = new ArrayList<String>();
			
			// read file
			do {
				inputLine = brStream.readLine(); // read in a line
				history.add(inputLine);
			}while (inputLine != null);
			brStream.close();
			// choose by date(1)
			
			if (choice == 1){
				
				System.out.println("Enter the date: (dd/MM/yyyy)");
				String date = sc.next();
				for (int i = 0; i < history.size(); i++){
					String[] parts = history.get(i).split(",");
					if (parts[0] == date){
						total += Double.parseDouble(parts[1]);
					}
				}
				if (total != 0){
					System.out.println("Revenue for " + date + " : " + total);
					return;
				}
			}
			// choose by month (2)
			else {
				System.out.println("Enter month: (MM)");
				String month = sc.next();
				for (int i = 0; i < history.size(); i++){
					String[] parts = history.get(i).split("/|\\,");
					if (parts[1] == month){
						total += Double.parseDouble(parts[3]);
					}
				}
				if (total != 0){
					System.out.println("Revenue for " + month + " : " + total);
					return;
				}
			}
			System.out.println("No revenue! / Revenue not found! ");
			
			


		}
		catch ( FileNotFoundException e ) {
			System.out.println( "Error opening the input file!"
					+ e.getMessage() );
			System.exit( 0 );
		}
		catch ( IOException e ) {
			System.out.println( "IO Error!" + e.getMessage() );
			e.printStackTrace();
			System.exit( 0 );
		}


	}
	
	public ArrayList<Staff> getStaffID(Scanner sc) throws FileNotFoundException,IOException{
		ArrayList<Staff> staffIDList = new ArrayList<Staff>();
		String path = "data/staff.txt";
		// Use relative path for Unix systems
		File f = new File(path);

		f.getParentFile().mkdirs(); 
		if (!f.exists()) 
			f.createNewFile();
		sc.nextLine();
		while (sc.hasNext()) {
			
			String temp[] = sc.nextLine().split(",");
			Staff staff = new Staff();
			staff.setStaffID(temp[0]);
			staff.setName(temp[1]);
			staff.setGender(temp[2]);
			staff.setjobTitle(temp[3]);;
			staffIDList.add(staff);
			sc.close();
			
		}
		return staffIDList;

}
}
