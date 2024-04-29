package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;



import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDClient {
	
	public void login(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		System.out.println("Insira o Email:");
		String email = reader.readLine();
		
		System.out.println("Insira a Senha:");
		String password = reader.readLine();
		
		CreateJson JsonObject = new CreateJson("LOGIN_CANDIDATE", email, password);
		JsonObject jsonRequest = JsonObject.functLoginClient();	
		
		//System.out.println("AQUI - " + jsonRequest);
		
		out.println(jsonRequest.toJson());
		String response = in.readLine();
		System.out.println("Resposta do servidor: " + response);	
	}
	
	public void registarCliente(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		System.out.println("Cadastre seu email:");
		String email = reader.readLine();
		
		System.out.println("Cadastre a senha:");
		String password = reader.readLine();
		
		System.out.println("Insira seu nome:");
		String name = reader.readLine();
		
		/*JsonObject jsonRequest = CreateJson.functRegistroClient("SIGNUP_CANDIDATE", email, password, name);
		
		String jsonResponse = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println("in - " + in);
		System.out.println("out - " + out);
		System.out.println("JSON REQUEST - " +jsonRequest);
		System.out.println(jsonResponse); */
		
		JsonObject jsonRequest = CreateJson.createRequest("SIGNUP_CANDIDATE");
		JsonObject data = new JsonObject();
		data.put("email", email);
		data.put("password", password);
		data.put("name", name);
		jsonRequest.put("data", data);
		
		
		
		out.println(jsonRequest.toJson());
		String response = in.readLine();
		//System.out.println("AQUI - " + jsonRequest);
		System.out.println(response);
		
	}


}
