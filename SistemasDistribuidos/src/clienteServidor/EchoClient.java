package clienteServidor;

import java.io.*;
import java.net.*;

public class EchoClient {
	
	  public static void main(String[] args) throws IOException {

	        //String serverHostname = new String ("127.0.0.1");
		  
		  System.out.println("Qual IP do servidor");
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		  String serverIP = br.readLine();
		  
		  System.out.println("Qual porta do servidor?");
		  br =new BufferedReader(new InputStreamReader(System.in));
		  int serverPort = Integer.parseInt(br.readLine());

	        if (args.length > 0)
	           serverIP = args[0];
	        System.out.println ("Attemping to connect to host " +
	                serverIP + " on port 10008.");

	        Socket echoSocket = null;
	        PrintWriter out = null;
	        BufferedReader in = null;

	        try {
	            echoSocket = new Socket(serverIP, serverPort);
	            out = new PrintWriter(echoSocket.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(
	                                        echoSocket.getInputStream()));
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host: " + serverIP);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for "
	                               + "the connection to: " + serverIP);
	            System.exit(1);
	        }

		BufferedReader stdIn = new BufferedReader(
	                                   new InputStreamReader(System.in));
		String userInput;

	        System.out.println ("Type Message (\"Bye.\" to quit)");
		while ((userInput = stdIn.readLine()) != null) 
	           {
		    out.println(userInput);

	            // end loop
	            if (userInput.toUpperCase().equals("Bye."))
	                break;

		    System.out.println("echo: " + in.readLine());
		   }

		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	    }
		
}
