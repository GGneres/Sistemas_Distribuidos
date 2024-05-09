package clienteServidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.Base64Variant.PaddingReadBehaviour;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import clienteServidor.EchoServer.JWTValidator;

public class CRUDServer{
	private static final String DATABASE_FILE = "user_database.txt";
	
	
	public void handleLogin(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido cliente: " + jsonRequest);
		
        /*JsonObject jsonCreate = (JsonObject) Jsoner.deserialize(jsonMenssage);
        String operation = (String) jsonCreate.get("operation"); */
		
		JsonObject data = (JsonObject) jsonRequest.get("data");
		String email = (String) data.get("email");
		String password = (String) data.get("password");
		
		//System.out.println("TESTE DO SERVER -" + email);
		//System.out.println("TESTE DO SERVER -" +password);
		
		List<Person> listOfPersons = readPersonDatabase();
		for (Person person : listOfPersons) {
			if(person.getEmail().equals(email)) {
				if(person.getPassword().equals(password)) {
					
					String token = JWTValidator.generateToken(person.getId(), "CANDIDATE");
					JsonObject jsonResponse = CreateJson.createResponse("LOGIN_CANDIDATE", "SUCCESS", token);
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}else {
					JsonObject jsonResponse = CreateJson.createResponse("LOGIN_CANDIDATE", "INVALID_LOGIN", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
			}
		}
		JsonObject jsonResponse = CreateJson.createResponse("LOGIN_CANDIDATE", "INVALID_LOGIN", "");
		System.out.println("Mandando para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));		
	}
	
	public void handleRegistrar(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido cliente: " + jsonRequest);
		
		JsonObject data = (JsonObject) jsonRequest.get("data");
		String email = (String) data.get("email");
		String password = (String) data.get("password");
		String name = (String) data.get("name");
		int id;
		
		List<Person> listOfPersons = readPersonDatabase();
		if(listOfPersons.size() == 0) {
			id = 1;
		}
		else {
			Person Ultima = listOfPersons.get(listOfPersons.size() - 1);
			id = Ultima.getId() + 1;
		}
		
		for(Person person : listOfPersons) {
			if(person.getEmail().equals(email)) {
				JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_CANDIDATE", "USER_EXISTS", "");
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
		}
		
		Person createPerson = new Person(email, password, name, id);
		//System.out.println("Recebido do Cliente:" + createPerson);
		listOfPersons.add(createPerson);
		writePersonDataBase(listOfPersons);
		
		JsonObject dataResponse = new JsonObject();
		JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_CANDIDATE", "SUCCESS", "");
		jsonResponse.remove("data");
		jsonResponse.put("data", dataResponse);
		System.out.println("Mandando para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));
	}
	
	public void logout(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		
		String token = (String) jsonRequest.get("token");
		
		JsonObject dataResponse = new JsonObject();
		JsonObject jsonResponse = CreateJson.createResponse("LOGOUT_CANDIDATE", "SUCCESS", new JsonObject());
		jsonResponse.put("data", dataResponse);
		
		System.out.println("Mandando para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));
		out.flush();
	}
	
	public void verificarDadosServer(JsonObject jsonRequest, PrintWriter out) throws IOException{
		if(jsonRequest != null && jsonRequest.containsKey("token")) {
			
			System.out.println("Recebido do cliente: " + jsonRequest);
			
			String token = (String) jsonRequest.get("token");
			
			int userId = JWTValidator.getIdClaim(token);
			
			List<Person> persons = readPersonDatabase();
			
			Optional<Person> optionalPerson = persons.stream()
												  .filter(Person -> Person.getId() == userId)
												  .findFirst();
			if(optionalPerson.isPresent()) {
				Person person = optionalPerson.get();
				
				JsonObject dataRegistro = new JsonObject();
				dataRegistro.put("email", person.getEmail());
				dataRegistro.put("password", person.getPassword());
				dataRegistro.put("name", person.getName());
				JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_ACCOUNT_CANDIDATE", "SUCCESS", "");
				jsonResponse.put("data", dataRegistro);
				
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				out.flush();
			}
		}
	}
	
	public void deletarServer(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int personId = JWTValidator.getIdClaim(token);
			List<Person> listOfPersons = readPersonDatabase();
			if(!listOfPersons.stream().anyMatch(person -> person.getId() == personId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_CANDIDATE","INVALID_EMAIL","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			for(Person person : listOfPersons) {
				if(person.getId() == personId) {
					listOfPersons.remove(person);
					writePersonDataBase(listOfPersons);
					JsonObject dataResponse = new JsonObject();
					JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_CANDIDATE", "SUCCESS", "");
					jsonResponse.remove("data");
					jsonResponse.put("data", dataResponse);
					jsonResponse.remove("token");
					jsonResponse.put("token", dataResponse);
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
			}
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_CANDIDATE", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
		JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_CANDIDATE", "INVALID_LOGIN", "");
		System.out.println("Enviado para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));
	}

	private List<Person> readPersonDatabase() throws IOException {
		List<Person> listOfPersons = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE))){
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Person person = new Person(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
				listOfPersons.add(person);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfPersons;
	}
	
	private void writePersonDataBase(List<Person> listOfPersons) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE))){
			for (Person person : listOfPersons) {
				String personString = person.getEmail() + "," + person.getPassword() + "," + person.getName() + "," + person.getId();
				bw.write(personString);
				bw.newLine();
			}
		}
	}
	



}
