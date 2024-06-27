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

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.cliftonlabs.json_simple.JsonObject;

import clienteServidor.EchoServer.JWTValidator;

public class ChooseCandidateServer {
	private static final String DATABASE_FILE_RECRUITER = "recruiter_database.txt";
	private static final String DATABASE_FILE_CHOOSE_CANDIDATE = "choose_candidate_database.txt";
	private static final String DATABASE_FILE = "user_database.txt";
	
	
	public void ChooseCandidate(JsonObject jsonRequest, PrintWriter out) throws IOException{
		System.out.println("Recebido cliente: " + jsonRequest);
		String token = (String) jsonRequest.get("token");
		
		try {
			int recruterId = JWTValidator.getIdClaim(token);
			List<Recruiter> listOfRecruiter = readRecruiterDatabase();
			if(!listOfRecruiter.stream().anyMatch(recruiter -> recruiter.getId() == recruterId)) {
				JsonObject jsonResponse = CreateJson.createResponse("CHOOSE_CANDIDATE","INVALID_TOKEN","");
				System.out.println("Enviado para o cliente: " + jsonResponse);
				out.println(CreateJson.toJsonString(jsonResponse));
				return;
			}
			
			JsonObject data = (JsonObject) jsonRequest.get("data");
			String id_user = (String) data.get("id_user");
			
			List<Person> listOfPersons = readPersonDatabase();
			List<ChooseCandidateDatabase> listOfChooseCandidateDatabases = readCooseCandidateDatabase();
			
			for(Person person : listOfPersons) {
				Integer auxIdPerson = person.getId();
				String auxIdPerson2 = (String) auxIdPerson.toString();
				if(auxIdPerson2.equals(id_user)) {
						
					Integer auxIdRecruiter = JWTValidator.getIdClaim(token);
					String auxIdRecruiter2 = (String) auxIdRecruiter.toString();
					
						ChooseCandidateDatabase createChooseCandidate = new ChooseCandidateDatabase(id_user, auxIdRecruiter2);
						//System.out.println("Recebido do Cliente:" + createPerson);
						listOfChooseCandidateDatabases.add(createChooseCandidate);
						writeChooseCandidateDatabase(listOfChooseCandidateDatabases);
					
						JsonObject jsonResponse = CreateJson.createResponse("CHOOSE_CANDIDATE", "SUCCESS", "");
						System.out.println("Enviado para o cliente: " + jsonResponse);
						out.println(CreateJson.toJsonString(jsonResponse));
						return;
					
				}
			}
			
			
		} catch (JWTVerificationException e) {
			JsonObject jsonResponse = CreateJson.createResponse("CHOOSE_CANDIDATE", "INVALID_TOKEN", "");
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
	
	private void writeChooseCandidateDatabase(List<ChooseCandidateDatabase> listOfChooseCandidates) throws IOException{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE_FILE_CHOOSE_CANDIDATE))){
			for (ChooseCandidateDatabase chooseCandidate : listOfChooseCandidates) {
				String jobString = chooseCandidate.getId_user() + "," + chooseCandidate.getId_recruiter();
				bw.write(jobString);
				bw.newLine();
			}
		}
	}
	
	private List<ChooseCandidateDatabase> readCooseCandidateDatabase() throws IOException {
		List<ChooseCandidateDatabase> listOfCooseCandidate = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE_CHOOSE_CANDIDATE))){
			String line;
			//System.out.println("AQUI!");
			while((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				ChooseCandidateDatabase ChooseCandidate = new ChooseCandidateDatabase(parts[0], parts[1]);
				listOfCooseCandidate.add(ChooseCandidate);
			}	
		} catch (FileNotFoundException e) {
			
		}
		return listOfCooseCandidate;
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

}
