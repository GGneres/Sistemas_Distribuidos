package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;

public class SearchProfileCliente {
	
	public void SearchProfile(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException, InterruptedException {
		
		System.out.println("-------Escolha-------");
		System.out.println("1- Somente skills");
		System.out.println("2- Somente experience");
		System.out.println("3- Os dois");
		
		int opcao;
		opcao = Integer.parseInt(reader.readLine());
		switch(opcao) {
		 case 1:
			 SearchProfileSkills(reader, out, in);
			 return;
		case 2: 
			 SearchProfileExperience(reader, out, in);
			 return;
		 case 3:
			 SearchProfileTwo(reader, out, in);
			 return;
		default:
		 System.out.println("Opção não reconhecida, escolha uma opção válida!");
		 break;
		}
	}
	
	public void SearchProfileSkills(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException, InterruptedException{
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Quantas skills tu quer procurar?");
		int opcao = Integer.parseInt(reader.readLine());
		
		List<String> searchsSkillsCandidates = new ArrayList<>();
		for(int aux = 1; aux <= opcao; aux++) {
			System.out.println("Digite a skill numero " + aux + ":");
			String skill = reader.readLine();
			
			JsonObject dataRegistro = new JsonObject();
			searchsSkillsCandidates.add(skill);
			
			dataRegistro.put("skill", searchsSkillsCandidates);
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("SEARCH_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("skill", searchsSkillsCandidates);
		jsonRequest.put("data", data);
	 
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}
	
	public void SearchProfileExperience(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException, InterruptedException {
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Qual experiencia:");
		String experience = reader.readLine();
		
		JsonObject jsonRequest = CreateJson.createRequest("SEARCH_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("experience", experience);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
		return;
	}
	
	public void SearchProfileTwo(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException, JsonException, InterruptedException{
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		System.out.println("Quantas skills tu quer procurar?");
		int opcao = Integer.parseInt(reader.readLine());
		
		List<String> searchsSkillsCandidates = new ArrayList<>();
		for(int aux = 1; aux <= opcao; aux++) {
			System.out.println("Digite a skill numero " + aux + ":");
			String skill = reader.readLine();
			
			JsonObject dataRegistro = new JsonObject();
			searchsSkillsCandidates.add(skill);
			
			dataRegistro.put("skill", searchsSkillsCandidates);
		}
		
		System.out.println("Qual experiencia:");
		String experience = reader.readLine();
		
		System.out.println("Qual filtro tu quer aplicar?");
		//System.out.println("1- AND");
		//System.out.println("2- OR");
		//int opcaoFilter = Integer.parseInt(reader.readLine());
		String filter = reader.readLine();
		
		if(!filter.equals("AND") && !filter.equals("OR")) {
			JsonObject jsonResponse = CreateJson.createResponse("SEARCH_CANDIDATE", "INVALID_FIELD", "");
			System.out.println("Mandando para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("SEARCH_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("skill", searchsSkillsCandidates);
		data.put("experience", experience);
		data.put("filter", filter);
		jsonRequest.put("data", data);
	 
		System.out.println("Enviado para o servidor: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
		return;
	}

}
