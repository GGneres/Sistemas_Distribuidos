package clienteServidor;

import java.io.*;
import java.net.*;

import org.xml.sax.helpers.ParserAdapter;

import com.github.cliftonlabs.json_simple.JsonException;


public class EchoClient {
	public String auxiliar_IP;
	public int auxiliar_Port;
	
	public static void main(String[] args) throws IOException, JsonException, InterruptedException {
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
        
         int auxParaMudarClienteEmpresa = 1;
         int switchCase = 0;       
         ClienteEmpresa defClieEmp = new ClienteEmpresa();
         int auxClientEmpresaDef = 0;
         
         while(auxParaMudarClienteEmpresa == 1) {
        	 switchCase = 0;
        	 auxClientEmpresaDef = 0;
        	 
        	 
        	 //ligar tela de escolha
        	 defClieEmp.setVisible(true);
        	 while(defClieEmp.isVisible() == true) {
        		 auxClientEmpresaDef = defClieEmp.getClienteEmpresa();
        		 //System.out.println("OI LANA" + auxClientEmpresaDef);
        		 Thread.sleep(10);
        	 }
        	 
        	 
        	 if(auxClientEmpresaDef == 1) {
        		 while(switchCase != 7) {
        			 System.out.println("----- Qual ação vai fazer? -----");
        			 System.out.println("1 - Login");
        			 System.out.println("2 - Registrar-se");
        			 System.out.println("3 - Editar dados");
        			 System.out.println("4 - Excluir conta");
        			 System.out.println("5 - Logout");
        			 System.out.println("6 - Ver dados");
        			 System.out.println("7 - Sair");
        			 System.out.println("8 - Criar skill");
        			 System.out.println("9 - Mostrar skill especifica");
        			 System.out.println("10 - Mostrar lista de skills");
        			 System.out.println("11 - Update skill");
        			 System.out.println("12 - Deletar skill");
        			 System.out.println("13 - Procurar job");
        			 
        			 int opcao;
        			 opcao = Integer.parseInt(reader.readLine());
        			 
        			 switch(opcao) {
        			 case 1:
        				 CRUDClient loginClient = new CRUDClient();
        				 loginClient.loginClient(reader, out, in);
        				 break;
        			 case 2:
        				 CRUDClient registrarClient = new CRUDClient();
        				 registrarClient.registarCliente(reader, out, in);
        				 break;
        			 case 3:
        				 CRUDClient atualizarClient = new CRUDClient();
        				 atualizarClient.atualizarClient(reader, out, in);
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
        			 case 7:
        				 //sair cliente
        				 System.out.println("Saindo...");
        				 defClieEmp.setClienteEmpresa(0);
        				 switchCase = 7;
        				 break;	 
        			 case 8:
        				 CRUDCompetenciasCliente criarSkill = new CRUDCompetenciasCliente();
        				 criarSkill.include_skill(reader, out, in);
        				 break;
        			 case 9:
        				 CRUDCompetenciasCliente verUmaSkill = new CRUDCompetenciasCliente();
        				 verUmaSkill.lookupSkill(reader, out, in);
        				 break;
        			 case 10:
        				 CRUDCompetenciasCliente verTodasSkill = new CRUDCompetenciasCliente();
        				 verTodasSkill.lookupSkillSet(in, out);
        				 break;
        			 case 11:
        				 CRUDCompetenciasCliente updateSkill = new CRUDCompetenciasCliente();
        				 updateSkill.updateSkill(reader, out, in);
        				 break;
        			 case 12:
        				 CRUDCompetenciasCliente deletarSkill = new CRUDCompetenciasCliente();
        				 deletarSkill.deleteSkill(reader, out, in);
        				 break;
        			 case 13:
        				 SearchJobClient searchJobCliente = new SearchJobClient();
        				 searchJobCliente.SearchJob(reader, out, in);
        				 break;
        			 default:
        				 System.out.println("Opção não reconhecida, escolha uma opção válida!");
        				 break;
        			 } //fim do swtich case
        		 } // fim do laço de cliente
        		 
        	 }else if(auxClientEmpresaDef == 2){
        		 
        		 while(switchCase != 7) {
        			 System.out.println("----- Qual ação vai fazer? -----");
        			 System.out.println("1 - Login");
        			 System.out.println("2 - Registrar-se");
        			 System.out.println("3 - Editar dados");
        			 System.out.println("4 - Excluir conta");
        			 System.out.println("5 - Logout");
        			 System.out.println("6 - Ver dados");
        			 System.out.println("7 - Sair");
        			 System.out.println("8 - Criar job");
        			 System.out.println("9 - Mostrar job especifica");
        			 System.out.println("10 - Mostrar lista de jobs");
        			 System.out.println("11 - Update job");
        			 System.out.println("12 - Deletar job");
        			 
        			 int opcao;
        			 opcao = Integer.parseInt(reader.readLine());
        			 
        			 switch(opcao) {
        			 case 1:
        				 CRUDEmpresa loginEmpresa = new CRUDEmpresa();
        				 loginEmpresa.loginEmpresa(reader, out, in);
        				 break;
        			 case 2:
        				 CRUDEmpresa registrarEmpresa = new CRUDEmpresa();
        				 registrarEmpresa.registarEmpresa(reader, out, in);
        				 break;
        			 case 3:
        				 CRUDEmpresa atualizarEmpresa = new CRUDEmpresa();
        				 atualizarEmpresa.atualizarEmpresa(reader, out, in);
        				 break;
        			 case 4:
        				 CRUDEmpresa deletarEmpresa = new CRUDEmpresa();
        				 deletarEmpresa.deleteEmpresa(reader, out, in);
        				 break;
        			 case 5:
        				 CRUDEmpresa logoutEmpresa = new CRUDEmpresa();
        				 logoutEmpresa.logoutEmpresa(in, out);
        				 //socket.close();
        				 break;
        			 case 6:
        				 //Verificar dados
        				 CRUDEmpresa verificarEmpresa = new CRUDEmpresa();
        				 verificarEmpresa.verificarDadosEmpresa(in, out);
        				 break;
        			 case 7:
        				 //sair cliente
        				 System.out.println("Saindo...");
        				 defClieEmp.setClienteEmpresa(0);
        				 switchCase = 7;
        				 break;
        			 case 8:
        				 CRUDVagasCliente criarVagaEmpresa = new CRUDVagasCliente();
        				 criarVagaEmpresa.include_job(reader, out, in);
        				 break;
        			 case 9:
        				 CRUDVagasCliente lookupVagasEmpresa = new CRUDVagasCliente();
        				 lookupVagasEmpresa.lookupJob(reader, out, in);
        				 break;
        			 case 10:
        				 CRUDVagasCliente lookupSetVagasEmpresa = new CRUDVagasCliente();
        				 lookupSetVagasEmpresa.lookupJobSet(in, out);
        				 break;
        			 case 11:
        				 CRUDVagasCliente updateVagasEmpresa = new CRUDVagasCliente();
        				 updateVagasEmpresa.updateJob(reader, out, in);
        				 break;
        			 case 12:
        				 CRUDVagasCliente deleteVagasEmpresa = new CRUDVagasCliente();
        				 deleteVagasEmpresa.deleteJob(reader, out, in);
        				 break;
        			 default:
        				 System.out.println("Opção não reconhecida, escolha uma opção válida!");
        				 break;
        			 } //fim do swtich case
        		 } // fim do laço de empresa
    			 
        		 
        		 defClieEmp.setClienteEmpresa(0);
        	 }
        	 
         }
        
         socket.close();

	}catch (IOException e) {
               System.err.println(e.getMessage());
           }
        
        
	
	}
	
}