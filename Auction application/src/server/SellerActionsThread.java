 package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//thread class that processes the seller client commands
public class SellerActionsThread extends Thread{
	
	ItemCollection itemCollection = new ItemCollection();
	Socket sellerClient = null;
	
	//lists that hold existing credentials info
	List<String> sellerUsernames = new ArrayList<String>();
	
	//constructor which assigns the thread the socket to client
	public SellerActionsThread(Socket sellerClient) {
		this.sellerClient = sellerClient;
		
		//populating login data with dummy data
		sellerUsernames.add("otonel");
		sellerUsernames.add("alina");
		sellerUsernames.add("kilo");
	}
	
	public void run() {
		//seler's credentials
		String sellerUsername;		
		try {
			//reader used to read the seller client response
			BufferedReader clientResponseReader = new BufferedReader(new InputStreamReader(sellerClient.getInputStream()));
			//writer used to write data to the client
			PrintWriter printWriter = new PrintWriter(sellerClient.getOutputStream(), true);
			
			//get the sellers credentials
			printWriter.println("Enter your username: ");
			sellerUsername = clientResponseReader.readLine();
			
			boolean loginSuccessful = false;
			
			//validate if username and password match existing records
			for(int i = 0; i < sellerUsernames.size(); i++) {
				if(sellerUsernames.get(i).equals(sellerUsername)) {
					loginSuccessful = true;
					break;
				}
			}
			
			//if logged in successful announce the client that logging operation was a success
			if(loginSuccessful) {
				System.out.println("SELLER - " + sellerUsername + " SUCCESSFULLY LOGGED IN !");
				printWriter.println("Login successful !");
				
				//while true awaits commands from the client
				while(true) {
					String command = clientResponseReader.readLine();
					
					if(command.contains("add")){
						//get only the data from the seller command
						String itemName = command.split(" ")[1];
						//instantiate a new object with the seller provided name
						Item item = new Item(itemName);
						//add the item to the collection
						ItemCollection.addItem(item);
						//notify the server
						System.out.println("Item no. " + item.getItemId() + " added to the seller list !");
						//notify the seller client that operation was succesful
						printWriter.println("Item added successfully !");
					}else if(command.contains("list")) {
						//populate a string with the current seller's items list
						String list = ItemCollection.displayList();
						//send the result to the seller client
						printWriter.println(list);
						//print the result to the server
						System.out.println(list);
					}else if(command.contains("sell")) {
						//get the item name from the received command
						boolean found = false;
						String itemName = command.split(" ")[1];
						for(Item item : ItemCollection.getItemList()) {
							//check if the item exists and if any bid was made upon it
							if(item.getItemName().equals(itemName)) {
								found = true;
								//check if there is any previous bidder
								if(item.getPrintWriter() != null) {
									//notify the highest bid buyer that he won the auction for the item on his next interaction with the client console
									item.getPrintWriter().append("Congratulations ! You won item " + item.getItemName() + " with a bid of " + item.getHighestBid() + "\n");
									//notify the seller client of the item removal process
									printWriter.println("Item " + item.getItemName() + " was selled and removed from the list !");
									//notify the server
									System.out.println("Item " + item.getItemName() + " removed from the seller list !");
									//remove the item from memory (first one matching the name if multiple items with same name are available)
									ItemCollection.removeItem(item);
								}else{
									printWriter.println("Cannot sell item " + item.getItemName() + ". No bid was made yet !");
								}
								break;
							}
						}
						//if the item doesn't exist notify the seller client
						if(found == false) {
							printWriter.println("Item " + itemName + " was not found in the list !");
						}
					}else if(command.contains("exit")) {
						//notify the seller client that he closed his current session and exit the loop
						printWriter.println("Thank you for using this app !");
						break;
					}else {
						//notify the seller client of the unrecognized command
						printWriter.println("UNKNOWN COMMAND !");
						//print the message to the server  
						System.out.println("UNKNOWN COMMAND !");
					}
				}
			//else if the login was not successful notify the seller client
			}else {
				//send the response to the client
				printWriter.println("Login failed !");
				//record the login attempt on the server
				System.out.println("SELLER - " + sellerUsername + " ATTEMPT TO LOG IN !");
			}
			
			//close the stream and shut down the socket
			printWriter.close();
			clientResponseReader.close();
			sellerClient.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
}
