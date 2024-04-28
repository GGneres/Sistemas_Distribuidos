package clienteServidor;

import java.io.*;
import java.net.*;


public class EchoClient {
	public String auxiliar_IP;
	public int auxiliar_Port;
	
	public static void main(String[] args) throws IOException {
		try {
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
 
        //CreateJson createJson = new CreateJson("LOGIN_CANDIDATE", email, password);
        //String jsonInfo = createJson.toJsonString();
        
        Socket socket = new Socket(serverIp, 21234);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));         
                
         int switchCase = 0;       
         while(switchCase != 5) {
        	 System.out.println("----- Qual ação vai fazer? -----");
        	 System.out.println("1 - Login");
        	 System.out.println("2 - Resistrar-se");
        	 System.out.println("3 - Editar dados");
        	 System.out.println("4 - Excluir conta");
        	 System.out.println("5 - Encerrar");
        	 
        	 int opcao;
        	 opcao = Integer.parseInt(reader.readLine());
        	 
        	 switch(opcao) {
        	 case 1:
        		 //login
        		 break;
        	 case 2:
        		 //registro
        		 break;
        	 case 3:
        		 //editar
        		 break;
        	 case 4:
        		 //excluir
        		 break;
        	 case 5:
        		 //Encerrar
        		 socket.close();
        		 return;
        	 default:
        		 System.out.println("Opção não reconhecida, escolha uma opção válida!");
        		 break;
        	 }
         }

	}catch (IOException e) {
               System.err.println(e.getMessage());
           }
        
        
	
	}
	
}