package clienteServidor;

import java.io.*;
import java.net.*;

import com.github.cliftonlabs.json_simple.JsonException;


public class EchoClient {
	public String auxiliar_IP;
	public int auxiliar_Port;
	
	public static void main(String[] args) throws IOException, JsonException {
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
         while(switchCase != 6) {
        	 System.out.println("----- Qual ação vai fazer? -----");
        	 System.out.println("1 - Login");
        	 System.out.println("2 - Resistrar-se");
        	 System.out.println("3 - Editar dados");
        	 System.out.println("4 - Excluir conta");
        	 System.out.println("5 - Logout");
        	 System.out.println("6 - Ver dados");
        	 
        	 int opcao;
        	 opcao = Integer.parseInt(reader.readLine());
        	 
        	 switch(opcao) {
        	 case 1:
        		 CRUDClient loginClient = new CRUDClient();
        		 loginClient.login(reader, out, in);
        		 break;
        	 case 2:
        		 CRUDClient registrarClient = new CRUDClient();
        		 registrarClient.registarCliente(reader, out, in);
        		 break;
        	 case 3:
        		 //editar
        		 break;
        	 case 4:
        		 	CRUDClient deletarClient = new CRUDClient();
        		 	deletarClient.deleteClient(reader, out, in);
        		 break;
        	 case 5:
        		 CRUDClient logoutClient = new CRUDClient();
        		 logoutClient.logoutCliente(in, out);
        		 //socket.close();
        		 break;
        	 case 6:
        		 //Verificar dados
        		 CRUDClient verificarDadosClient = new CRUDClient();
        		 verificarDadosClient.verificarDadosClient(in, out);
        		 break;
        	 default:
        		 System.out.println("Opção não reconhecida, escolha uma opção válida!");
        		 break;
        	 }
         }
         socket.close();

	}catch (IOException e) {
               System.err.println(e.getMessage());
           }
        
        
	
	}
	
}