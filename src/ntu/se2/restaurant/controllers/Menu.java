package ntu.se2.restaurant.controllers;

import java.util.Scanner;

import ntu.se2.restaurant.models.Item;
import ntu.se2.restaurant.models.Promo;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;


public class Menu 
{
	ArrayList<Item> itemList = new ArrayList<Item>();
	ArrayList<Item> AList = new ArrayList<Item>(); //Appetizer
	ArrayList<Item> MList = new ArrayList<Item>(); //main course
	ArrayList<Item> DList = new ArrayList<Item>();//dessert
	ArrayList<Item> BList = new ArrayList<Item>();//beverages
	ArrayList<Promo> PList = new ArrayList<Promo>();
	
	Scanner sc;
	Item temp;
	
	public Menu()
	{
		
	}
	
	private void sortItems()
	{
		for (int i = 0; i < itemList.size(); i++) {
			Item a = itemList.get(i);
			if(a.getType()=='a')
				AList.add(a);
			else if(a.getType()=='m')
				MList.add(a);
			else if(a.getType()=='d')
			    DList.add(a);
			else if(a.getType()=='b')
				BList.add(a);
		}
	}
	
	public void getItems()throws IOException // reading from the file and updating it into the array list
	{
		try{
			String path = "data/item.txt";
			// Use relative path for Unix systems
			File f = new File(path);

			f.getParentFile().mkdirs(); 
			if (!f.exists()) 
				f.createNewFile();
			sc = new Scanner(f);
			sc.nextLine();
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Item item = new Item(current[0],current[1],current[2].charAt(0),current[3],current[4],current[5]);
				itemList.add(item);
			}
			sortItems();
			sc.close();
		}
		catch(FileNotFoundException err1)
		{
			err1.printStackTrace();
		}
	}
	public void getPromotion()throws IOException
	{
		try
		{
			String path = "data/promotion.txt";
			// Use relative path for Unix systems
			File f = new File(path);

			f.getParentFile().mkdirs(); 
			if (!f.exists()) 
				f.createNewFile();
			Scanner sc = new Scanner(f);
			sc.nextLine(); //since the first line is the format for the text file
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Promo p = new Promo();
				ArrayList<Item> tempPList = new ArrayList<Item>();
				p.setPromoID(current[0]);
				p.setName(current[1]);
				for(int i=2;i<current.length;i++)
					for(Iterator<Item> it = itemList.iterator(); it.hasNext();)
				{
					Item check = it.next();
					if(current[i].equals(check.getItemID()))
				       tempPList.add(check);
				}
				p.setItemList(tempPList);
				PList.add(p);
			}
			sc.close();
		    
		} catch (FileNotFoundException ex1)
		{
			  ex1.printStackTrace();
		}
   }

	public void printMenu()
	{
		sc = new Scanner(System.in);
		System.out.println("-------------------Menu-------------------");
		System.out.println("--------Appetizers--------");
		
		for (int i = 0; i < AList.size(); i++) 
		{
			Item itm = AList.get(i);
			System.out.println("No."+itm.getItemID()+"  Name:"+itm.getName()+"  ->"+itm.getDescription()+"...................  $"+itm.getPrice());
			System.out.println();
		}
		System.out.println("--------Main Course--------");
		for (int i = 0; i < MList.size(); i++) 
		{
			Item itm = MList.get(i);
			System.out.println("No."+itm.getItemID()+"  Name:"+itm.getName()+"  ->"+itm.getDescription()+"...................  $"+itm.getPrice());
			System.out.println();
		}
		System.out.println("--------Desserts--------");
		for (int i = 0; i < DList.size(); i++) 
		{
			Item itm = DList.get(i);
			System.out.println("No."+itm.getItemID()+"  Name:"+itm.getName()+"  ->"+itm.getDescription()+"...................  $"+itm.getPrice());
			System.out.println();
		}
		System.out.println("--------Beverages--------");
		for (int i = 0; i < BList.size(); i++) 
		{
			Item itm = BList.get(i);
			System.out.println("No."+itm.getItemID()+"  Name:"+itm.getName()+"  ->"+itm.getDescription()+"...................  $"+itm.getPrice());
			System.out.println();
		}
		System.out.println("--------Promotions--------");
		for(int i=0;i<PList.size();i++)
		{
		    Promo prm = PList.get(i);
		    System.out.println(prm.getPromoID()+" ");
		    for (int j = 0; j < prm.getItemList().size(); j++) 
			{
				Item itm = prm.getItemList().get(j);
		    	System.out.println(itm.getName()+"  ");    	
		    }
		    System.out.println("Meal cost: $"+prm.calculatePrice());
		}
		System.out.println();
		//System.out.println("Enter item code to select item: ");
		//return selectItem(sc.next());
	}
	
	public Item selectItem(String obj_ID)
	{
		Item obj = null;
		for(Item i:itemList)
			if(i.getItemID()!=null && i.getItemID().equals(obj_ID))
				obj = i;
		return obj;
	}
	
	public Promo selectPromo(String promo_ID)
	{
		Promo obj = null;
		for(Promo p:PList)
			if(p.getPromoID()!=null && p.getPromoID().equals(promo_ID))
				obj = p;
		return obj;
	}
	
}
