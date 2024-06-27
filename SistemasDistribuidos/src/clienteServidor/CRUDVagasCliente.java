package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDVagasCliente {
	
	public void include_job(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException{
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Defina a skill:");
		String skill = reader.readLine();
		
		System.out.println("experiencia:");
		String experience = reader.readLine();
		
		System.out.println("Searchable:");
		String searchable = reader.readLine();
		
		System.out.println("avaiable:");
		String available = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("INCLUDE_JOB");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("skill", skill);
		data.put("experience", experience);
		data.put("searchable", searchable);
		data.put("available", available);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}
	
	public void lookupJob(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
		
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		
		System.out.println("Qual o id da skill para ver:");
		String id = reader.readLine();
		String regex = "[0-9]+";
		if(id.matches(regex)) {
			JsonObject jsonRequest = CreateJson.createRequest("LOOKUP_JOB");
			jsonRequest.put("token", token);
			JsonObject data = new JsonObject();
			data.put("id", id);
			jsonRequest.put("data", data);
			
			System.out.println("Enviado para o servidor: " + jsonRequest);
			String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
			System.out.println(responseJson);
		}

		
	}
	
	public void lookupJobSet(BufferedReader in, PrintWriter out) throws IOException {
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("LOOKUP_JOBSET");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		CreateJson.sendRequestLogout(jsonRequest, out);
		String responseJson = in.readLine();
		System.out.println(responseJson);
		
	}
	
	public void deleteJob(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Qual o id daskill para deletar:");
		String id = reader.readLine();
		String regex = "[0-9]+";
		
		if(id.matches(regex)) {
			
			JsonObject jsonRequest = CreateJson.createRequest("DELETE_JOB");
			jsonRequest.put("token", token);
			JsonObject data = new JsonObject();
			data.put("id", id);
			jsonRequest.put("data", data);
			
			System.out.println("Enviado para o servidor: " + jsonRequest);
			String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
			System.out.println(responseJson);
		}
	}
	
	public void updateJob(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("qual id:");
		String id = reader.readLine();
		
		System.out.println("Nova skill:");
		String skill = reader.readLine();
		
		System.out.println("nova experiencia:");
		String novoExperience = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("UPDATE_JOB");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("id", id);
		data.put("skill", skill);
		data.put("experience", novoExperience);
		jsonRequest.put("data", data);
	
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}

}
