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

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.cliftonlabs.json_simple.JsonObject;

import clienteServidor.EchoServer.JWTValidator;


public class CRUDServerEmpresa {
	private static final String DATABASE_FILE_RECRUITER = "recruiter_database.txt";
	private static final String DATABASE_FILE = "user_database.txt";
	
	public void handleLogin(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido cliente: " + jsonRequest);
		
		JsonObject data = (JsonObject) jsonRequest.get("data");
		String email = (String) data.get("email");
		String password = (String) data.get("password");
		
		
		List<Recruiter> listOfRecruiters = readRecruiterDatabase();
		
		for (Recruiter Recruiter : listOfRecruiters) {
			if(Recruiter.getEmail().equals(email)) {
				if(Recruiter.getPassword().equals(password)) {
					
					String token = JWTValidator.generateToken(Recruiter.getId(), "RECRUITER");
					JsonObject jsonResponse = CreateJson.createResponse("LOGIN_RECRUITER", "SUCCESS", token);
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}else {
					JsonObject jsonResponse = CreateJson.createResponse("LOGIN_RECRUITER", "INVALID_LOGIN", "");
					System.out.println("Mandando para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
			}
		}
		JsonObject jsonResponse = CreateJson.createResponse("LOGIN_RECRUITER", "INVALID_LOGIN", "");
		System.out.println("Mandando para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));		
	}
	
	public void handleRegistrar(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido cliente: " + jsonRequest);
		
		JsonObject data = (JsonObject) jsonRequest.get("data");
		String email = (String) data.get("email");
		String password = (String) data.get("password");
		String name = (String) data.get("name");
		String industry = (String) data.get("industry");
		String description = (String) data.get("description");
		int id;
		
		List<Recruiter> listOfRecruiters = readRecruiterDatabase();
		List<Person> listOfPersons = readPersonDatabase();
		if(listOfRecruiters.size() == 0) {
			id = 1;
		}
		else {
			Recruiter Ultima = listOfRecruiters.get(listOfRecruiters.size() - 1);
			id = Ultima.getId() + 1;
		}
		
		for(Recruiter Recruiter : listOfRecruiters) {
			if(Recruiter.getEmail().equals(email)) {
				JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_RECRUITER", "USER_EXISTS", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
		}
		
		for(Person Persons : listOfPersons) {
			if(Persons.getEmail().equals(email)) {
				JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_RECRUITER", "USER_EXISTS", "");
				System.out.println("Mandando para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
		}
		
		Recruiter createRecruiter = new Recruiter(email, password, name, industry, description, id);
		//System.out.println("Recebido do Cliente:" + createRecruiter);
		listOfRecruiters.add(createRecruiter);
		writeRecruiterDataBase(listOfRecruiters);
		
		JsonObject dataResponse = new JsonObject();
		JsonObject jsonResponse = CreateJson.createResponse("SIGNUP_RECRUITER", "SUCCESS", "");
		jsonResponse.remove("data");
		jsonResponse.put("data", dataResponse);
		System.out.println("Mandando para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));
	}
	
	public void logout(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER","INVALID_EMAIL","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject dataResponse = new JsonObject();
			JsonObject jsonResponse = CreateJson.createResponse("LOGOUT_RECRUITER", "SUCCESS", new JsonObject());
			jsonResponse.put("data", dataResponse);
			
			System.out.println("Mandando para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			out.flush();
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
	}
	
	public void deletarEmpresa(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER","INVALID_EMAIL","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			for(Recruiter recruiter : listOfRecruiter) {
				if(recruiter.getId() == recruterId) {
					listOfRecruiter.remove(recruiter);
					writeRecruiterDataBase(listOfRecruiter);
					JsonObject dataResponse = new JsonObject();
					JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER", "SUCCESS", "");
					jsonResponse.remove("data");
					jsonResponse.put("data", dataResponse);
					jsonResponse.remove("token");
					jsonResponse.put("token", dataResponse);
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					
					//List<Person> persons = readPersonDatabase();
					
					 //auxiRequest = logout(jsonRequest, out);
					

					
					return;	
				}
			}
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
		JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER", "INVALID_LOGIN", "");
		System.out.println("Enviado para o cliente: " + jsonResponse);
		out.println(CreateJson.toJsonString(jsonResponse));
	}
	
	public void verificarDadosServer(JsonObject jsonRequest, PrintWriter out) throws IOException{
		
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER","INVALID_EMAIL","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			if(jsonRequest != null && jsonRequest.containsKey("token")) {
				
				System.out.println("Recebido do cliente: " + jsonRequest);
				
				int recruiterId = JWTValidator.getIdClaim(token);
				
				List<Recruiter> recruiter = readRecruiterDatabase();
				
				Optional<Recruiter> optionalRecruiter = recruiter.stream()
						.filter(Recruiter -> Recruiter.getId() == recruiterId)
						.findFirst();
				if(optionalRecruiter.isPresent()) {
					Recruiter recruiters = optionalRecruiter.get();
					
					JsonObject dataRegistro = new JsonObject();
					dataRegistro.put("email", recruiters.getEmail());
					dataRegistro.put("password", recruiters.getPassword());
					dataRegistro.put("name", recruiters.getName());
					dataRegistro.put("industry", recruiters.getIndustry());
					dataRegistro.put("description", recruiters.getDescription());
					JsonObject jsonResponse = CreateJson.createResponse("LOOKUP_ACCOUNT_RECRUITER", "SUCCESS", "");
					jsonResponse.put("data", dataRegistro);
					
					System.out.println("Enviado para o cliente: " + jsonResponse);
					out.println(CreateJson.toJsonString(jsonResponse));
					out.flush();
				}
			}
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
		
	}
	
	public void atualizarContaServer(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido do cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER","INVALID_EMAIL","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			int recruiterId = JWTValidator.getIdClaim(token);
			
			Optional<Recruiter> optionalRecruiter = listOfRecruiter.stream()
					.filter(Recruiter -> Recruiter.getId() == recruiterId)
					.findFirst();
			
			if(optionalRecruiter.isPresent()) {
				Recruiter recruiter = optionalRecruiter.get();
				boolean changes = false;
				
				if(data.containsKey("email")) {
					String changedEmail = (String) data.get("email");
					if(changedEmail.isEmpty() == false) {
						recruiter.setEmail(changedEmail);
						changes = true;
					}
				}
				
				if(data.containsKey("password")) {
					String changedSenha = (String) data.get("password");
					if(changedSenha.isEmpty() == false) {
						recruiter.setPassword(changedSenha);
						changes = true;
					}
				}
				
				if(data.containsKey("name")) {
					String changedName = (String) data.get("name");
					if(changedName.isEmpty() == false) {
						recruiter.setName(changedName);
						changes = true;
					}
				}
				
				if(data.containsKey("industry")) {
					String changedIndustry = (String) data.get("industry");
					if(changedIndustry.isEmpty() == false) {
						recruiter.setIndustry(changedIndustry);
						changes = true;
					}
				}
				
				if(data.containsKey("description")) {
					String changedDescription = (String) data.get("description");
					if(changedDescription.isEmpty() == false) {
						recruiter.setDescription(changedDescription);
						changes = true;
					}
				}
				
				if(changes == false) {
					JsonObject jsonResponse = CreateJson.createResponse("UPDATE_ACCOUNT_RECRUITER", "INVALID_EMAIL", new JsonObject());
					out.println(CreateJson.toJsonString(jsonResponse));
					return;
				}
				
				writeRecruiterDataBase(listOfRecruiter);
				token = "";
				JsonObject dataResponse = new JsonObject();
				JsonObject jsonResponse = CreateJson.createResponse("UPDATE_ACCOUNT_RECRUITER", "SUCCESS", "");
				jsonResponse.remove("data");
				jsonResponse.put("data", dataResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				
				return;
			}
			
			JsonObject jsonResponse = CreateJson.createResponse("UPDATE_ACCOUNT_RECRUITER", "INVALID_EMAIL", new JsonObject());
			out.println(CreateJson.toJsonString(jsonResponse));	
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("DELETE_ACCOUNT_RECRUITER", "INVALID_TOKEN", "");
			System.out.println("Enviado para o cliente: " + jsonResponse);
			out.println(CreateJson.toJsonString(jsonResponse));
			return;
		}
		
	}
	
	
	private List<Recruiter> readRecruiterDatabase() throws IOException {
		List<Recruiter> listOfRecruiters = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_RECRUITER))){
			String line;
			//System.out.println("AQUI!");
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Recruiter Recruiter = new Recruiter(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
				listOfRecruiters.add(Recruiter);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfRecruiters;
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
	
	private void writeRecruiterDataBase(List<Recruiter> listOfRecruiters) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE_RECRUITER))){
			for (Recruiter Recruiter : listOfRecruiters) {
				String RecruiterString = Recruiter.getEmail() + "," + Recruiter.getPassword() + "," + Recruiter.getName() + "," + Recruiter.getIndustry() + "," + Recruiter.getDescription() + "," + Recruiter.getId();
				bw.write(RecruiterString);
				bw.newLine();
			}
		}
	}

}
