package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BuyerActionsThread extends Thread {

	Socket buyerClient = null;
	
	//lists that hold existing credentials info
	List<String> buyerUsernames = new ArrayList<String>();
	
	//constructor which assigns the thread the socket to client
	public BuyerActionsThread(Socket buyerClient) {
		this.buyerClient = buyerClient;
		
		//populating credentials list info with dummy data
		buyerUsernames.add("buyer");
		buyerUsernames.add("catrina");
		buyerUsernames.add("milo");
	}
	
	public void run() {
		String buyerUsername;
		//get the sellers credentials
		try {
			//reader used to read the seller client response
			BufferedReader clientResponseReader = new BufferedReader(new InputStreamReader(buyerClient.getInputStream()));
			//writer used to write data to the client
			PrintWriter printWriter = new PrintWriter(buyerClient.getOutputStream(), true);
			
			//get the buyers credentials
			printWriter.println("Enter your username: ");
			buyerUsername = clientResponseReader.readLine();
			
			boolean loginSuccessful = false;
			
			//validate if username and password match existing records
			for(int i = 0; i < buyerUsernames.size(); i++) {
				if(buyerUsernames.get(i).equals(buyerUsername)){
					loginSuccessful = true;
					break;
				}
			}
			
			//if logged in successful announce the client that logging operation was a success
			if(loginSuccessful) {
				System.out.println("BUYER - " + buyerUsername + " SUCCESSFULLY LOGGED IN !");
				printWriter.println("Login successful !");

				//while true awaits commands from the client
				while(true) {
					//reads the command sent from the client
					String command = clientResponseReader.readLine();
					if(command.contains("bid")){
						boolean found = false;
						//get the item name from the command
						String itemName = command.split(" ")[1];
						//get the bid value from the command
						int bidValue = Integer.parseInt(command.split(" ")[2]);
						//check is bid is valid
						if(bidValue > 0) {
							//find the item in the list by its name
							for(Item item : ItemCollection.getItemList()) {
								if(item.getItemName().equals(itemName)){
									found = true;
									//if bid is not high enough notify the client so
									if(bidValue <= item.getHighestBid()) {
										printWriter.println("Your bid is not high enough ! Current highest bid for item " + item.getItemName() + " : " + item.getHighestBid());		
									}else {
										if(item.getPrintWriter() != null && item.getPrintWriter() != printWriter) {
											//notify the former highest bider that he has been outbiden if he exists on his next interaction with the client console
											item.getPrintWriter().append("You have been outbiden on item " + item.getItemName() + " ! Current highest bid for item " + item.getItemName() + " : " + bidValue + "\n");
										}
										//update the item's bid attributes according to the new highest bidder
										item.setHighestBid(bidValue);
										item.setHighestBidderName(buyerUsername);
										item.setPrintWriter(printWriter);
										//notify the new highest bidder 
										printWriter.println("You now have the highest bid for item " + item.getItemName());
										//notify the server
										System.out.println(item.toString());
									}
									break;
								}
							}
							//if no item was found
							if(found == false) {
								printWriter.println("No item matching the names is on sale !");
							}
						//if bid is invalid
						}else {
							printWriter.println("Your bid is invalid !");
						}						
					}else if(command.contains("list")) {
						//populate a string with the current seller's items list
						String list = ItemCollection.displayList();
						//send the result to the seller client
						printWriter.println(list);
						//print the result to the server
						System.out.println("List: \n" + list);
					}else {
						//notify the seller client of the unrecognized command
						printWriter.println("UNKNOWN COMMAND !");
						//print the message to the server  
						System.out.println("UNKNOWN COMMAND !");
					}
				}	
			}else {
				//send the response to the client
				printWriter.println("Login failed !");
				//record the login attempt on the server
				System.out.println("BUYER - " + buyerUsername + " ATTEMPT TO LOG IN !");
			}
			
			//close the stream and shut down the socket
			printWriter.close();
			clientResponseReader.close();
			buyerClient.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
