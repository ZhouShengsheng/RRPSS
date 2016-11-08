package ntu.se2.restaurant.models;

public class Table {
	public int tableNo;
	public int size;
	private String status;

	public Table() {
	}

	public Table(int tableNo, int size, String status) {
		super();
		this.tableNo = tableNo;
		this.size = size;
		this.status = status;
	}

	public Table(int size, int tableNo, int isoccupied) {
		this.size = size;
		this.tableNo = tableNo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTableNo() {
		return tableNo;
	}

	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
