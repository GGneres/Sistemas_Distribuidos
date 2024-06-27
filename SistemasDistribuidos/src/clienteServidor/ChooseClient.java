package clienteServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.cliftonlabs.json_simple.JsonObject;

public class ChooseClient {
	
	public void chooseCandidate(BufferedReader reader, PrintWriter out, BufferedReader in) throws IOException{
		
		System.out.println("Qual id do usuario?");
		String id = reader.readLine();
		
		CRUDEmpresa auxToken = new CRUDEmpresa();
		String token;
		token = auxToken.getToken();
		if(token == null || token.isEmpty()) {
			System.out.println("Faça o login antes!");
			return;
		}
		
		JsonObject jsonRequest = CreateJson.createRequest("CHOOSE_CANDIDATE");
		jsonRequest.put("token", token);
		JsonObject data = new JsonObject();
		data.put("id_user", id);
		jsonRequest.put("data", data);
		
		System.out.println("Enviado para o cliente: " + jsonRequest);
		String responseJson = CreateJson.sendRequest(jsonRequest, out, in);
		System.out.println(responseJson);
	}

}
