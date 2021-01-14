package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.BuyerClient;

//waits for connection to be established with the buyer client 
public class BuyerServerThread extends Thread {
	public void run() {
		//declare connection data
		Socket client = null;
		ServerSocket connection = null;
		try {
			connection = new ServerSocket(BuyerClient.BUYER_CLIENT_PORT);
			while(true) {
				//listens for a connection to be made
				client = connection.accept();
				//initialize the buyer actions server processing thread
				new BuyerActionsThread(client).start();
				
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
