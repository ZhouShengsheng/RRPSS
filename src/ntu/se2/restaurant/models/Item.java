package ntu.se2.restaurant.models;

public class Item {
	private String name, description, itemID, price, discountedPrice;
	private char type;

	public Item() {

	}

	public Item(String itemID, String name, char type, String description, String price, String discountedPrice) // create
																													// an
																													// new
																													// object
	{
		this.itemID = itemID;
		this.name = name;
		this.type = type;
		this.description = description;
		this.price = price;
		this.discountedPrice = discountedPrice;
	}

	public String getName() // returns name
	{
		return name;
	}

	public void setName(String temp) // modify the name
	{
		this.name = temp;
	}

	public String getDescription()// returns description
	{
		return description;
	}

	public void setDescription(String temp)// modify menu_id
	{
		this.description = temp;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String temp) {
		this.itemID = temp;
	}

	public String getPrice() // return price
	{
		return price;
	}

	public void setPrice(String temp)// modifies price
	{
		this.price = temp;
	}

	public char getType() // returns type
	{
		return this.type;
	}

	public void setType(char temp) {
		this.type = temp;
	}

	public String getDiscountedPrice() // return price
	{
		return discountedPrice;
	}

	public void setDiscountedPrice(String temp)// modifies price
	{
		this.discountedPrice = temp;
	}
}
