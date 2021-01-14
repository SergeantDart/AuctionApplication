package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class BuyerClient {
	
	public final static int BUYER_CLIENT_PORT = 8000;
	
	public static void main(String[] args) throws IOException {
		//welcome message
		System.out.println("This is the buyer client !");
		
		//declaring data
		InetAddress inetAddress;
		
		String command = "";
		
		Socket client = null;
		
		BufferedReader inputReader = null;
		BufferedReader serverResponseReader = null;
		
		PrintWriter printWriter = null;
		
		//get the local IP address
		inetAddress = InetAddress.getLocalHost();
		//establishing connection to the buyer server side
		client = new Socket(inetAddress, BUYER_CLIENT_PORT);
		//reader for getting the data from the user's input
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		//reader for getting the data from the server
		serverResponseReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		//writer for sending data to the server
		printWriter = new PrintWriter(client.getOutputStream(), true);
		
		//read the initial server response
		System.out.println(serverResponseReader.readLine());
		//send to the server the user's login input
		String buyerUsername = inputReader.readLine();
		printWriter.println(buyerUsername);
		
		//read the server response
		String loginMessage = serverResponseReader.readLine();
		//if the server sends back a successful login message, we proceed
		if(loginMessage.contains("success")) {
			System.out.println(loginMessage);
			//as long as the user desires, the console expects commands, sends it to the server and handle back the processed response
			while(true) {
				System.out.println("Awaiting command...");
				//char array used to store the server response
				char[] response = new char[500];
				//user's command parameter choice
				command = inputReader.readLine();
				//check if user has issued an exit command, and if so exit the loop
				if(command.contains("exit")) {
					System.out.println("Have a nice day !");
					break;
				}
				//sends the user's command to the server
				printWriter.println(command);
				//receive the server response and stores it in the response char array and then display it to the user
				serverResponseReader.read(response);
				System.out.println(response);
			}
		}else {
			//print out to the user the failed login message
			System.out.println(loginMessage);
		}
		//close the stream and shutdown the socket
		client.close();
		inputReader.close();
		serverResponseReader.close();
		printWriter.close();
	}		
}

