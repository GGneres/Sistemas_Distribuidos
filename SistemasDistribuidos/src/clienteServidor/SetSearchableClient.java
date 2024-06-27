package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonObject;

public class SetSearchableClient {
	
	public void setSearchable(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}		
		System.out.println("novo searchable:");
		String novoAvailable = reader.readLine();
		
		System.out.println("Qual id?");
		String id = reader.readLine();
		
		
		JsonObject jsonRequest = CreateJson.createRequest("SET_JOB_SEARCHABLE");
		JsonObject data = new JsonObject();
		data.put("searchable", novoAvailable);
		data.put("id", id);
		jsonRequest.put("token", token);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o cliente: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}

}
