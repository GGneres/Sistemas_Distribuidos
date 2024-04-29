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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import clienteServidor.EchoServer.JWTValidator;

public class CRUDServer{
	private static final String DATABASE_FILE = "user_database.txt";
	
	
	public void handleLogin(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
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
					String token = JWTValidator.generateToken(person.getEmail(), person.getEmail());
					JsonObject jsonResponse = CreateJson.createResponse("LOGIN_CANDIDATE", "SUCCESS", token);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}else {
					JsonObject jsonResponse = CreateJson.createResponse("LOGIN_CANDIDATE", "USER_NOT_FOUND", "");
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
			}
		}
		JsonObject jsonResponse = CreateJson.createResponse("LOGIN_CANDIDATE", "USER_NOT_FOUND", "");
		out.println(CreateJson.toJsonString(jsonResponse));		
	}
	
	public void handleRegistrar(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		JsonObject data = (JsonObject) jsonRequest.get("data");
		String email = (String) data.get("email");
		String password = (String) data.get("password");
		String name = (String) data.get("name");
		
		List<Person> listOfPersons = readPersonDatabase();
		for(Person person : listOfPersons) {
			if(person.getEmail().equals(email)) {
				JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_CANDIDATE", "USER_EXISTS", "");
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
		}
		
		Person createPerson = new Person(email, password, name);
		listOfPersons.add(createPerson);
		writePersonDataBase(listOfPersons);
		
		JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_CANDIDATE", "SUCCESS", "");
		out.println(CreateJson.toJsonString(jsonResponse));
	}

	private List<Person> readPersonDatabase() throws IOException {
		List<Person> listOfPersons = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE))){
			String line;
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Person person = new Person(parts[0], parts[1], parts[2]);
				listOfPersons.add(person);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfPersons;
	}
	
	private void writePersonDataBase(List<Person> listOfPersons) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE))){
			for (Person person : listOfPersons) {
				String personString = person.getEmail() + "," + person.getPassword() + "," + person.getName();
				bw.write(personString);
				bw.newLine();
			}
		}
	}


}
