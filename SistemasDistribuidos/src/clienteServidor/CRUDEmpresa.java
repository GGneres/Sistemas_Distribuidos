package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public class CRUDEmpresa {
	
	public static String token;
	
	public void loginEmpresa(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException{
		System.out.println("Insira o Email:");
		String email = reader.readLine();
		
		System.out.println("Insira a Senha:");
		String password = reader.readLine();
		
		CreateJson JsonObject = new CreateJson("LOGIN_RECRUITER", email, password);
		JsonObject jsonRequest = JsonObject.functLoginClient();	
		
		out.println(jsonRequest.toJson());
		System.out.println("Enviado para o server: " + jsonRequest.toJson());
		String response = in.readLine();
		System.out.println("Resposta do servidor: " + response);

		
		JsonObject jsonCreate = (JsonObject) Jsoner.deserialize(response);
		JsonObject data = (JsonObject) jsonCreate.get("data");
		token = (String) data.get("token");

	}
	
	public void registarEmpresa(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		System.out.println("Cadastre seu email:");
		String email = reader.readLine();
		
		System.out.println("Cadastre a senha:");
		String password = reader.readLine();
		
		System.out.println("Insira seu nome:");
		String name = reader.readLine();
		
		System.out.println("Insira sua Industria:");
		String industry = reader.readLine();
		
		System.out.println("Insira sua descrição:");
		String description = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("SIGNUP_RECRUITER");
		JsonObject data = new JsonObject();
		data.put("email", email);
		data.put("password", password);
		data.put("name", name);
		data.put("industry", industry);
		data.put("description", description);
		jsonRequest.put("data", data);
		
		out.println(jsonRequest.toJson());
		System.out.println("Enviado para o server: " + jsonRequest.toJson());
		String response = in.readLine();

		System.out.println(response);	
	}
	
	public void logoutEmpresa(BufferedReader in, PrintWriter out) throws IOException{
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("LOGOUT_RECRUITER");
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
	
	public void verificarDadosEmpresa(BufferedReader in, PrintWriter out) throws IOException {
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("LOOKUP_ACCOUNT_RECRUITER");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		CreateJson.sendRequestLogout(jsonRequest, out);
		
		System.out.println("Enviado para o cliente: " + jsonRequest);
		String responseJson = in.readLine();
		System.out.println(responseJson);
		
	}
	
	public void atualizarEmpresa(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
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
		
		System.out.println("nova industria:");
		String novaIndustria = reader.readLine();
		
		System.out.println("nova descrição:");
		String novaDescricao = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("UPDATE_ACCOUNT_RECRUITER");
		JsonObject data = new JsonObject();
		data.put("email", novoEmail);
		data.put("password", novaSenha);
		data.put("name", novoNome);
		data.put("industry", novaIndustria);
		data.put("description", novaDescricao);
		jsonRequest.put("token", token);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}
	
	public void deleteEmpresa(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("DELETE_ACCOUNT_RECRUITER");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		
		
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		token = "";
		System.out.println(responseJson);
		
		//logoutEmpresa(in, out);
	}
	
	public String getToken() {
		return token;
	}
	

}
