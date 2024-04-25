package clienteServidor;

import java.net.*; 
import java.io.*; 


public class EchoServer extends Thread{
	
	 protected Socket clientSocket;

	 public static void main(String[] args) throws IOException 
	   { 
	    ServerSocket serverSocket = null; 
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Qual porta devo iniciar?");
		  br = new BufferedReader(new InputStreamReader(System.in));
		  int serverP = Integer.parseInt(br.readLine());
		 
    	
	    try { 
	    	 serverSocket = new ServerSocket(serverP);
	         System.out.println ("Connection Socket Created");
	         try { 
	              while (true)
	                 {
	            	  new EchoServer (serverSocket.accept());
	                  System.out.println ("Waiting for Connection");
	                  
	                 }
	             } 
	         catch (IOException e) 
	             { 
	              System.err.println("Accept failed."); 
	              System.exit(1); 
	             } 
	        } 
	    catch (IOException e) 
	        { 
	         System.err.println("Could not listen on port: 10008."); 
	         System.exit(1); 
	        } 
	    finally
	        {
	         try {
	              serverSocket.close(); 
	             }
	         catch (IOException e)
	             { 
	              System.err.println("Could not close port: 10008."); 
	              System.exit(1); 
	             } 
	        }
	   }

	 private EchoServer (Socket clientSoc)
	   {
	    clientSocket = clientSoc;
	    start();
	   }

	 @Override
	public void run()
	   {
	    System.out.println ("New Communication Thread Started");

	    try { 
	         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
	                                      true); 
	         BufferedReader in = new BufferedReader( 
	                 new InputStreamReader( clientSocket.getInputStream())); 

	         String inputLine; 

	         while ((inputLine = in.readLine()) != null) 
	             { 
	        	 System.out.println("Cliente enviou: " + inputLine);
	              System.out.println ("Server enviou: " + inputLine.toUpperCase()); 
	              out.println(inputLine.toUpperCase()); 

	              if (inputLine.toUpperCase().equals("Bye.")) 
	                  break; 
	             } 

	         out.close(); 
	         in.close(); 
	         clientSocket.close(); 
	        } 
	    catch (IOException e) 
	        { 
	         System.err.println("Problem with Communication Server");
	         System.exit(1); 
	        } 
	    }

}