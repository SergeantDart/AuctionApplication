package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.SellerClient;

//waits for connection to be established with the seller client 
public class SellerServerThread extends Thread{
	public void run() {
		//declare connection data
		Socket client = null;
		ServerSocket connection = null;
		try {
			connection = new ServerSocket(SellerClient.SELLER_CLIENT_PORT);
			while(true) {
				//listens for a connection to be made
				client = connection.accept();
				//initialize the buyer actions server processing thread
				new SellerActionsThread(client).start();	
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
