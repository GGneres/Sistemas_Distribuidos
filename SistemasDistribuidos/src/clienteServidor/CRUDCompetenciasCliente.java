package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDCompetenciasCliente {
	
	public void include_skill(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException{
		CRUDClient auxToken = new CRUDClient();
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
		

		
		JsonObject jsonRequest = CreateJson.createRequest("INCLUDE_SKILL");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("skill", skill);
		data.put("experience", experience);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}
	
	public void lookupSkill(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
		
		CRUDClient auxToken = new CRUDClient();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		
		System.out.println("Qual a skill para ver:");
		//int id = reader.read();
		//String idAsString = String.valueOf(id);
		//String id = reader.readLine();
		//String regex = "[0-9]+";
		String skill = reader.readLine();
		//if(id.matches(regex)) {	
			JsonObject jsonRequest = CreateJson.createRequest("LOOKUP_SKILL");
			jsonRequest.put("token", token);
			JsonObject data = new JsonObject();
			data.put("skill", skill);
			jsonRequest.put("data", data);
			
			System.out.println("Enviado para o servidor: " + jsonRequest);
			String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
			System.out.println(responseJson);
		//}
		//else {
			//System.out.println("Coloque um valor valido (0-9)");
		//}
		
	}
	
	public void lookupSkillSet(BufferedReader in, PrintWriter out) throws IOException {
		CRUDClient auxToken = new CRUDClient();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("LOOKUP_SKILLSET");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		CreateJson.sendRequestLogout(jsonRequest, out);
		String responseJson = in.readLine();
		System.out.println(responseJson);
		
		
	}
	
	public void deleteSkill(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
		CRUDClient auxToken = new CRUDClient();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Qual a skill para deletar:");
		String skill = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("DELETE_SKILL");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("skill", skill);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}
	
	public void updateSkill(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException {
		CRUDClient auxToken = new CRUDClient();
		String token;
		token = auxToken.getToken();
		
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Qual a skill:");
		String skill = reader.readLine();
		
		System.out.println("nova skill:");
		String newSkill = reader.readLine();
		
		System.out.println("nova experiencia:");
		String novoExperience = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("UPDATE_SKILL");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("skill", skill);
		data.put("newSkill", newSkill);
		data.put("experience", novoExperience);
		jsonRequest.put("data", data);
	
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}
	
	

}
