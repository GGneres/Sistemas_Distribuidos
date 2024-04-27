package clienteServidor;

import java.io.*;
import java.net.*;


public class EchoClient {
	public String auxiliar_IP;
	public int auxiliar_Port;
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		ClientLogin login = new ClientLogin();
		login.setVisible(true);
		
		while(login.isVisible() == true){			
			String auxiliar_IP = login.getTextField_1().getText();
			//int  auxiliar_Port= login.getPortNumber();
			
			login.setAuxiliar_IP(auxiliar_IP);
			//login.setAuxiliar_Port(auxiliar_Port);
			
		}
		String auxi_serverIp = login.getAuxiliar_IP();
		//int auxi_serverPort = login.getAuxiliar_Port();

        //System.out.println("Qual o IP:");
		String serverIp = auxi_serverIp;
        
        //System.out.println("Qual a porta");
        //int serverPort = auxi_serverPort;
		
        System.out.println("Qual o email:");
        String email = reader.readLine();

        System.out.println("Qual a senha:");
        String password = reader.readLine();
        
        CreateJson createJson = new CreateJson("LOGIN_CANDIDATE", email, password);
        String jsonInfo = createJson.toJsonString();
        
        try (Socket socket = new Socket(serverIp, 21234);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

               out.println(jsonInfo);

               String response = in.readLine();
               System.out.println("Resposta do servidor: " + response);

           } catch (IOException e) {
               System.err.println(e.getMessage());
           }
        
        
	
	}
	
}