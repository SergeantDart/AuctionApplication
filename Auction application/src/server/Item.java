package server;

import java.io.PrintWriter;

//data class used to represent an auctioned item
public class Item {
	static int numberGenerator = 0;
	//unique item id
	int itemId;
	//item name
	String itemName;
	//highest bidder name
	String highestBidderName;
	//highest bid ( money amount )
	int highestBid;
	//custom printwriter to send a message to the highest bid buyer client
	PrintWriter printWriter;
	
	public Item() {
		this.itemId = numberGenerator++;
		this.itemName = "N/A";
		this.highestBidderName = "N/A";
		this.highestBid = 0;
		this.printWriter = null;
	}
	public Item(String itemName) {
		this.itemId = numberGenerator++;
		this.itemName = itemName;
		this.highestBidderName = "N/A";
		this.highestBid = 0;
		this.printWriter = null;
	}
	
	public Item(int itemId, String itemName, String highestBidderName, int highestBid, PrintWriter printWriter) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.highestBidderName = highestBidderName;
		this.highestBid = highestBid;
		this.printWriter = printWriter;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nItem id: " + getItemId() + ", " 
		+ "item name: " + getItemName() + ", " 
		+ "highest bidder: " + getHighestBidderName() + ", " 
		+ "highest bid: " + getHighestBid());
		return builder.toString();
	}


	//getters and setters for each attribute
	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getHighestBidderName() {
		return highestBidderName;
	}
	
	public void setHighestBidderName(String highestBidderName) {
		this.highestBidderName = highestBidderName;
	}
	
	public int getHighestBid() {
		return highestBid;
	}
	
	public void setHighestBid(int highestBid) {
		this.highestBid = highestBid;
	}
	
	public PrintWriter getPrintWriter() {
		return printWriter;
	}
	
	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	
	
	
}
