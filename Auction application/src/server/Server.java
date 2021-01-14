package server;

//instantiates and starts threads for both the buyer and the seller
public class Server {
	public static void main(String args[])
	{
		System.out.println("Main auction SERVER !");
		SellerServerThread sellerServerThread = new SellerServerThread();
		sellerServerThread.start();
		BuyerServerThread buyerServerThread = new BuyerServerThread();
		buyerServerThread.start();
	}
}
