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
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.ScannerUtil;

/**
 * Singleton class to control items.
 * 
 *
 */
public class ItemController {

	// Singleton.
	private static ItemController instance = null;
	
	// Fields.
	private ArrayList<Item> itemList;
	private Scanner sc = ScannerUtil.scanner;
	
	private ItemController() {
		itemList = new ArrayList<Item>();
		loadData();
	}
	
	public ArrayList<Item> getItemList() {
		return itemList;
	}
	
	/**
	 * Get shared singleton instance.
	 * 
	 * @return
	 */
	public static ItemController sharedInstance() {
		if (instance == null) {
			instance = new ItemController();
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
	 * Get new item id which is the last item id plus 1.
	 * 
	 * @return
	 */
	private String getNewItemId() {
		int count = itemList.size();
		if (count >= 0) {
			return String.valueOf(Integer.parseInt(itemList.get(itemList.size()-1).getItemID()) + 1);
		} else {
			return "1";
		}
	}
	
	/**
	 * Get item by its id.
	 * 
	 * @param itemId
	 * @return
	 */
	public Item getItemById(String itemId) {
		Item obj = null;
		for(Item i:itemList)
			if(i.getItemID()!=null && i.getItemID().equals(itemId))
				obj = i;
		return obj;
	}
	
	/**
	 * Print all items.
	 */
	public void printItemList() {
		System.out.print("ItemID" + "       " + "Item Name" + "       " + "Item Price" + "       " + "Item Description"
				+ "                  " + "Item Type" + "       " + "Discounted Price");
		for (int i = 0; i < itemList.size(); i++) {

			Item itm = itemList.get(i);
			System.out.print("\n" + itm.getItemID() + "            " + itm.getName() + "            " + itm.getPrice()
					+ "            " + itm.getDescription() + "            " + itm.getType() + "            "
					+ itm.getDiscountedPrice());

		}
		System.out.println();
	}
	
	/**
	 * Create a new item.
	 * 
	 * @param sc
	 * @return
	 */
	public boolean createItem()
	{
		boolean result = false;
		char check;
		Item item = new Item();
		System.out.println("Please enter the name of the item:");
	    item.setName(sc.nextLine());

		do{
			System.out.println("Please enter the type of item:");
			System.out.println("a. Appetisers");
			System.out.println("m. Main course");
			System.out.println("d. Desserts");
			System.out.println("b. Beverages");
			check = sc.nextLine().charAt(0);
		  }while(check!='a'&&check!='m'&&check!='d'&&check!='b');
		
		item.setType(check);
		System.out.println("Please enter price of item");
		item.setPrice(sc.nextLine());
		System.out.println("Please enter description of the item");
		item.setDescription(sc.nextLine());
		System.out.println("Please enter the discounted price of the item");
		item.setDiscountedPrice(sc.nextLine());
		item.setItemID(getNewItemId());
		boolean exist = false;
	    for(Item i:itemList)
	    {
	    	if(i.getName().equals(item.getName()))
	    		exist = true;
	    }
	    
	    if(!exist)
	    {
	    	itemList.add(item);
	    	result = saveData();
	    }
		return result;
	}
	
	public void updateItem() {
		printItemList();
		
		int choice;
		String itemID;
		System.out.println("Please enter itemID of item to be updated");
		itemID = sc.nextLine();
		Item item = getItemById(itemID);
		System.out.println("1. Change Name");
		System.out.println("2. Change Price");
		System.out.println("3. Change Description");
		System.out.println("4. Change Type");
		System.out.println("5. Change Discounted Price");
		choice = sc.nextInt();
		sc.skip(System.lineSeparator());
		switch (choice) {
		case 1:
			System.out.print("Enter new name of item:");
			item.setName(sc.nextLine());
			break;
		case 2:
			System.out.print("Enter new price of item:");
			item.setPrice(sc.nextLine());
			break;
		case 3:
			System.out.print("Enter new description of item:");
			item.setDescription(sc.nextLine());
			break;
		case 4:
			char check;
			do {
				System.out.println("Enter new type of item");
				System.out.println("Please enter the type of item:");
				System.out.println("a. Appetisers");
				System.out.println("m. Main course");
				System.out.println("d. Desserts");
				System.out.println("b. Beverages");
				check = sc.next().charAt(0);
			} while (check != 'a' && check != 'm' && check != 'd' && check != 'b');
			item.setType(check);
			break;
		case 5:
			System.out.println("Enter new discounted price of item:");
			item.setDiscountedPrice(sc.nextLine());
			break;
		}
		
		saveData();
	}
	
	/**
	 * Delete item.
	 * 
	 */
	public void deleteItem() {
		printItemList();
		String itemID;
		System.out.print("Please enter itemID of item to be deleted: ");
		itemID = sc.nextLine();
		System.out.println("itemList size: "+ itemList.size());
		System.out.println("itemID: "+ itemID);
		Item item = getItemById(itemID);
		itemList.remove(item);
		System.out.println("itemList size: "+ itemList.size());
		
		saveData();
	}
}
