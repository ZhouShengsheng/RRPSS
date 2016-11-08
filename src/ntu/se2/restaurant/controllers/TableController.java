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

import ntu.se2.restaurant.models.Table;
import ntu.se2.restaurant.utils.DataFilePath;
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
	
//	public static void main(String[] args) throws IOException {
//		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DataFilePath.TABLE_PATH, false)));
//		out.println("tableNo, size, status");
//		int size=2;
//		for(int i=0;i<30;i++){
//			Table table = new Table();
//			table.setTableNo(i+1);
//			table.setStatus("vacated");
//			table.setSize(size);
//			if(i>9 && i<=19)
//				size=4;
//			if(i>19 &&i<=24)
//				size=8;
//			if(i>24 && i<=29)
//				size=10;
//			
//			out.println(table.getTableNo()+","+table.getSize()+","+table.getStatus());
//		}
//		out.close();
//		ScannerUtil.close();
//	}
	
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

}
