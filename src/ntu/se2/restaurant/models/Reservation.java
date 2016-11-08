package ntu.se2.restaurant.models;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import ntu.se2.restaurant.utils.DateUtil;

public class Reservation {

	private String name, people, contact, date;
	private int tableNo;
	private Date start, end;
	private Table table;
	private String orderId;

	public Reservation() {
	}

	public Reservation(String date, String startTime, String People, String name, String contact, int tableNo)
			throws ParseException {
		this.date = date;
		this.start = DateUtil.formatStringTimetoDate(startTime);
		this.people = People;
		this.name = name;
		this.contact = contact;
		this.tableNo = tableNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String inName) {
		this.name = inName;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String inPeople) {
		this.people = inPeople;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String inContact) {
		this.contact = inContact;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String inDate) {
		this.date = inDate;
	}

	public int getTableNo() {
		return tableNo;
	}

	public void setTableNo(int inTableNo) {
		this.tableNo = inTableNo;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(String startTime) {
		try {
			this.start = DateUtil.formatStringTimetoDate(startTime);
			setEnd();
		} catch (ParseException e1) {
			System.out.println("Invalid ");
		}
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		this.end = calendar.getTime();
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
