package ntu.se2.restaurant.models;

import java.util.Scanner;
import java.util.ArrayList;


public class Promo 
{
	Scanner sc;
	private String name, promoID;
	private double price;
	private ArrayList<Item> ItemList;
	
	public Promo()
	{
		
	}
	
	public Promo(String promoID,String name, double price,ArrayList<Item>ItemList)
	{
		this.promoID = promoID;
		this.name = name;
		this.price = price;
		this.ItemList = ItemList;
	}
	
	public String getPromoID()
	{
		return promoID;
	}
	
	public void setPromoID(String temp)
	{
		this.promoID = temp;
	}

	public String getName()
	{
		return this.name;
	}
	
	public void setName(String temp)
	{
        this.name = temp;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	public void setPrice(double temp)
	{
		this.price = temp;
	}
	
	public ArrayList<Item> getItemList ()
	{
		return ItemList;		
	}
	
	public void setItemList(ArrayList<Item> itemList)
	{
		this.ItemList = itemList;
	}
	
	public String calculatePrice()
	{
		String temp = "0";
		for (int i = 0; i < ItemList.size(); i++) {
			Item itm = ItemList.get(i);

			temp += Double.parseDouble(itm.getPrice());
		}
		return temp;
	}
	
	public void DailyReport()
	{
		
	}
}
