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
import ntu.se2.restaurant.models.Promo;
import ntu.se2.restaurant.utils.DataFilePath;
import ntu.se2.restaurant.utils.ScannerUtil;
import ntu.se2.restaurant.controllers.ItemController;

/**
 * Singleton class to control promotions.
 * 
 *
 */
public class PromotionController{

		// Singleton.
		private static PromotionController instance = null;
		
		// Fields.
		private ArrayList<Promo> promoList;
		private Scanner sc = ScannerUtil.scanner;
		
		private PromotionController() {
			promoList = new ArrayList<Promo>();
			loadData();
		}
		
		/**
		 * Get shared singleton instance.
		 * 
		 * @return
		 */
		public static PromotionController sharedInstance() {
			if (instance == null) {
				instance = new PromotionController();
			}
			return instance;
		}
		private void loadData(){
			
				try
				{
					Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.PROMO_PATH)));
					sc.nextLine();
					while(sc.hasNext())
					{
						String current[] = sc.nextLine().split(",");
						Promo p = new Promo();
						ArrayList<Item> tempList = new ArrayList<Item>();
						p.setPromoID(current[0]);
						p.setName(current[1]);
						p.setPrice(Double.parseDouble(current[2]));
						ArrayList<Item> itemList= ItemController.sharedInstance().getItemList();
						for(int i=3;i<current.length;i++)
						{
							for (int j = 0; j <itemList.size(); j++)
							{
								Item check = itemList.get(j);
								if(current[i].equals(check.getItemID()))
								{
									tempList.add(check);
								}
							}
						}
						p.setItemList(tempList);
						promoList.add(p);
					}
					sc.close();
				    
				} catch (FileNotFoundException ex1)
				{
					  ex1.printStackTrace();
				}
		}
		private boolean saveData(){
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DataFilePath.PROMO_PATH, false)))) 
			{
				out.print("promoID, promoName, item1, item2, item3, item4....");
				for (int i = 0; i < promoList.size(); i++) {
					Promo promo = promoList.get(i);
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
		private String getNewPromoId() {
			int count = promoList.size();
			if (count >= 0) {
				return ("P"+String.valueOf(Integer.parseInt(promoList.get(promoList.size()-1).getPromoID()) + 1));
			} else {
				return "P1";
			}
		}
		public Promo getPromoById(String promoId) {
			Promo obj = null;
			for(Promo i:promoList)
				if(i.getPromoID()!=null && i.getPromoID().equals(promoId))
					obj = i;
			return obj;
		}
		public void printPromotionList()
		{
			System.out.println("--------Promotions--------");
			for (int i = 0; i < promoList.size(); i++) {
			    Promo prm = promoList.get(i);
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
		public boolean createPromo()
		{

		    
		    // Promotion info.
			String promoName, promoId;
			double promoPrice;
			ArrayList<Item> newItemList = new ArrayList<>();
			ArrayList<Item> itemList= ItemController.sharedInstance().getItemList();
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
			return saveData();
		}
		public void updatePromo(){
			printPromotionList();
			
			String promoID;
			Promo promo;
			ArrayList<Item> itemList= ItemController.sharedInstance().getItemList();
			System.out.println("Enter the promo ID of the promotion you want to update:");
			promoID = sc.next();
			promo = getPromoById(promoID);
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
		
			saveData();
		}
		public void deletePromo(){
			printPromotionList();
			String promoID;
			System.out.print("Please enter promoID of item to be deleted: ");
			promoID = sc.nextLine();
			System.out.println("promoList size: "+ promoList.size());
			System.out.println("promoID: "+ promoID);
			Promo promo = getPromoById(promoID);
		    promoList.remove(promo);
			System.out.println("promoList size: "+ promoList.size());
			saveData();
		}

		
		
}
