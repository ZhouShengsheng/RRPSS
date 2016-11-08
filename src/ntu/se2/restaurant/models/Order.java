package ntu.se2.restaurant.models;

import java.util.ArrayList;
import java.util.Date;

public class Order {
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Promo> pList = new ArrayList<Promo>();
	private String id;
	private Date date;
	private Table table;
	private Staff staff;
	private String status;
	private double bill = 0; // bill before tax
	private double billWithTax = 0; // bill after tax

	private static final double TAX_RATE = 1.07;

	public Order() {

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
		for (int i = 0; i < pList.size(); i++) {
			Promo prm = pList.get(i);
			bill += prm.getPrice();
		}
		billWithTax = bill * TAX_RATE;
	}

}
