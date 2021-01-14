package server;

import java.util.ArrayList;
import java.util.List;

public class ItemCollection {
	//static list that holds all the current seller's items
	static List<Item> itemList = new ArrayList<Item>();
	
	//it doesn't contain any other non-static attributes so there is no need for a constructor
	//all the methods are static to be used without the need to instantiate the class
	
	//function for adding a new item to the list
	public static void addItem(Item item) {
		itemList.add(item);
	}
	
	//function for deleting a certain item from the list
	public static void removeItem(Item item) {
		itemList.remove(item);
	}
	
	//function used to return the text displayed list of items, if it exists
	public static String displayList() {
		String result = "";
		int size = itemList.size();
		if(size > 0) {
			for(Item item : itemList) {
				result += item.toString();
			}
		}else {
			result += "The list does NOT contain any item !";
		}
		return result;
	}

	public static List<Item> getItemList() {
		return itemList;
	}

	public static void setItemList(List<Item> itemList) {
		ItemCollection.itemList = itemList;
	}
	
	
}
