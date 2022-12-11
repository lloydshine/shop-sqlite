package app;

public class Item {
	private int itemID;
	private String itemName;
	private float itemPrice;
	
	public Item(int id, String name, float price) {
		this.itemID = id;
		this.itemName = name;
		this.itemPrice = price;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public float getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(float itemPrice) {
		this.itemPrice = itemPrice;
	}
}
