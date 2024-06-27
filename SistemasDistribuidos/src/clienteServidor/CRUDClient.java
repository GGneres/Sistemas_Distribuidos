package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class CRUDClient {
	
	public static String token;
	
	public void loginClient(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException{
		
		/*System.out.println("Insira o Email:");
		String email = reader.readLine();
		
		System.out.println("Insira a Senha:");
		String password = reader.readLine();*/
		
		String email = null, password = null;
		TelaLoginUsuario telaLoginUsuario = new TelaLoginUsuario();
		
		
		telaLoginUsuario.setVisible(true);
		
		
		while(telaLoginUsuario.isVisible()) {
			email = telaLoginUsuario.getTextField().getText();
			password = telaLoginUsuario.getTextField_1().getText();
		}
		
		
		
		CreateJson JsonObject = new CreateJson("LOGIN_CANDIDATE", email, password);
		JsonObject jsonRequest = JsonObject.functLoginClient();	
		
		//System.out.println("AQUI - " + jsonRequest);
		
		out.println(jsonRequest.toJson());
		System.out.println("Enviado para o server: " + jsonRequest.toJson());
		String response = in.readLine();
		System.out.println("Resposta do servidor: " + response);
		
		//String email = (String) data.get("email");
		//JsonObject data = (JsonObject) jsonRequest.get("data");
		
		//JsonObject data = (JsonObject) jsonRequest.get("data");
		//String email = (String) data.get("email");
		
		JsonObject jsonCreate = (JsonObject) Jsoner.deserialize(response);
		JsonObject data = (JsonObject) jsonCreate.get("data");
		token = (String) data.get("token");
		String status = (String) jsonCreate.get("status");
		
		if(status.equals("SUCCESS")) {
			JsonObject jsonRequest2 = CreateJson.createRequest("GET_COMPANY");
			jsonRequest2.put("token", token);
			JsonObject data2 = new JsonObject();
			jsonRequest2.put("data", data2);
			
			out.println(jsonRequest2.toJson());
			System.out.println("Enviado para o server: " + jsonRequest2.toJson());
			String response2 = in.readLine();
			//System.out.println("AQUI - " + jsonRequest);
			System.out.println(response2);
		}
		
		
		//auxiliar.aux_setToken(token);
		
		//System.out.println("O TOKEN É - " + token);
		
		
        //JsonObject jsonCreate = (JsonObject) Jsoner.deserialize(response);
        //String operation = (String) jsonCreate.get("data");
        //token = operation;
        //System.out.println("O TOKEN É - " + operation);
		
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
		System.out.println("Enviado para o server: " + jsonRequest.toJson());
		String response = in.readLine();
		//System.out.println("AQUI - " + jsonRequest);
		System.out.println(response);
		
		
	}
	
	public void logoutCliente(BufferedReader in, PrintWriter out) throws IOException{
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("LOGOUT_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		
		CreateJson.sendRequestLogout(jsonRequest, out);
		
		System.out.println("Enviado para o servidor - " + jsonRequest);
		
		
		String jsonResponse = in.readLine();
		//jsonResponse.to
		
		token = "";
		System.out.println(jsonResponse);
	}
	
	public void verificarDadosClient(BufferedReader in, PrintWriter out) throws IOException {
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("LOOKUP_ACCOUNT_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		CreateJson.sendRequestLogout(jsonRequest, out);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = in.readLine();
		System.out.println(responseJson);
		
	}
	
	public void deleteClient(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("DELETE_ACCOUNT_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		
		
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		token = "";
		System.out.println(responseJson);
		
		//logoutCliente(in, out);
	}
	
	public void atualizarClient(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("novo email:");
		String novoEmail = reader.readLine();
		
		System.out.println("nova senha:");
		String novaSenha = reader.readLine();
		
		System.out.println("novo nome:");
		String novoNome = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("UPDATE_ACCOUNT_CANDIDATE");
		JsonObject data = new JsonObject();
		data.put("email", novoEmail);
		data.put("password", novaSenha);
		data.put("name", novoNome);
		jsonRequest.put("token", token);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o cliente: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}

	public String getToken() {
		return token;
	}
	
	


}
